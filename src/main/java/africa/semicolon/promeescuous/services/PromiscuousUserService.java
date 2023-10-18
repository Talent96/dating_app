package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.config.AppConfig;
import africa.semicolon.promeescuous.dtos.requests.*;
import africa.semicolon.promeescuous.dtos.responses.*;
import africa.semicolon.promeescuous.exceptions.AccountActivationFailedException;
import africa.semicolon.promeescuous.exceptions.PromiscuousBaseException;
import africa.semicolon.promeescuous.exceptions.UserNotFoundException;
import africa.semicolon.promeescuous.models.*;
import africa.semicolon.promeescuous.repositories.UserRepository;
import africa.semicolon.promeescuous.services.cloud.CloudService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.github.fge.jsonpatch.ReplaceOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static africa.semicolon.promeescuous.dtos.responses.ResponseMessage.*;
import static africa.semicolon.promeescuous.exceptions.ExceptionMessage.*;
import static africa.semicolon.promeescuous.models.Role.CUSTOMER;
import static africa.semicolon.promeescuous.utils.AppUtil.*;
import static africa.semicolon.promeescuous.utils.JwtUtil.extractEmailFrom;
import static africa.semicolon.promeescuous.utils.JwtUtil.isValidJwt;

@Service
@Slf4j
@AllArgsConstructor
public class PromiscuousUserService implements UserService {
    private final UserRepository userRepository;
    private final MailService mailService;
    private final AppConfig appConfig;
    private final CloudService cloudService;
    private final MediaService mediaService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegisterUserResponse register(RegisterUserRequest registerUserRequest) {
        String email = registerUserRequest.getEmail();
        String password = registerUserRequest.getPassword();
        String passwordHash = passwordEncoder.encode(password);
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordHash);
        user.setAddress(new Address());
        user.setRole(CUSTOMER);
        User savedUser = userRepository.save(user);
        EmailNotificationRequest request = buildEmailRequest(savedUser);
        mailService.send(request);
        RegisterUserResponse registerUserResponse = new RegisterUserResponse();
        registerUserResponse.setMessage(USER_REGISTRATION_SUCCESSFUL.name());
        return registerUserResponse;
    }




    @Override
    public ApiResponse<?> activateUserAccount(String token) {
        boolean isTestToken = token.equals(appConfig.getTestToken());
        if (isTestToken) return activateTestAccount();
        boolean isValidJwt = isValidJwt(token);
        if (isValidJwt) return activateAccount(token);
        throw new AccountActivationFailedException(
                ACCOUNT_ACTIVATION_FAILED_EXCEPTION.getMessage());
    }

    @Override
    public GetUserResponse getUserById(Long id) throws UserNotFoundException{
        Optional<User> foundUser = userRepository.findById(id);
        User user = foundUser.orElseThrow(
                ()->new UserNotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage())
        );
        Media media = mediaService.getMediaByUser(user);
        GetUserResponse getUserResponse = buildUserResponse(user);
        getUserResponse.setProfileImage(media.getUrl());
        return getUserResponse;
    }

    @Override
    public List<GetUserResponse> getAllUsers(int page, int pageSize) {
        Pageable pageable = buildPageRequest(page, pageSize);
        Page<User> usersPage = userRepository.findAll(pageable);
        List<User> foundUsers = usersPage.getContent();
        return foundUsers.stream()
                         .map(user-> buildUserResponse(user))
                         .toList();
    }



    @Override
    public UpdateUserResponse updateProfile(UpdateUserRequest updateUserRequest, Long id) throws JsonPatchException {
        ModelMapper modelMapper = new ModelMapper();
        User user = findUserById(id);

        mediaService.uploadMedia(updateUserRequest.getProfileImage(), user);

        Set<String> userInterests = updateUserRequest.getInterests();
        Set<Interest> interests = parseInterestsFrom(userInterests);
        user.setInterests(interests);


        Address userAddress = user.getAddress();
        modelMapper.map(updateUserRequest, userAddress);
        user.setAddress(userAddress);
        JsonPatch updatePatch = buildUpdatePatch(updateUserRequest);
        return applyPatch(updatePatch, user);
    }

    @Override
    public UploadMediaResponse uploadMedia(MultipartFile mediaToUpload) {
        return null;
    }

    @Override
    public UploadMediaResponse uploadProfilePicture(MultipartFile mediaToUpload) {
        return mediaService.uploadProfilePicture(mediaToUpload);
    }

    @Override
    public ApiResponse<?> reactToMedia(MediaReactionRequest mediaReactionRequest) {
        String response = mediaService.reactToMedia(mediaReactionRequest);
        return ApiResponse.builder().data(response).build();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.readByEmail(email).orElseThrow(
                ()->new UserNotFoundException(
                        String.format(USER_WITH_EMAIL_NOT_FOUND_EXCEPTION.getMessage(), email)
                )
        );
    }

    private String uploadImage(MultipartFile profileImage) {
        boolean isFormWithProfileImage = profileImage !=null;
        if (isFormWithProfileImage) return cloudService.upload(profileImage);
        throw new RuntimeException("image upload failed");
    }

    private static Set<Interest> parseInterestsFrom(Set<String> interests){
        Set<Interest> userInterests =interests.stream()
                                              .map(interest->Interest.valueOf(interest.toUpperCase()))
                                              .collect(Collectors.toSet());
        return userInterests;
    }

    private UpdateUserResponse applyPatch(JsonPatch updatePatch, User user) throws JsonPatchException {
        ObjectMapper objectMapper = new ObjectMapper();
        //1. Convert user to JsonNode
        JsonNode userNode = objectMapper.convertValue(user, JsonNode.class);

            //2. Apply patch to JsonNode from step 1
            JsonNode updatedNode = updatePatch.apply(userNode);
            //3. Convert updatedNode to user
            user = objectMapper.convertValue(updatedNode, User.class);
            log.info("user-->{}", user);
            //4. Save updatedUser from step 3 in the DB
            var savedUser=userRepository.save(user);
            log.info("user-->{}", savedUser);
            return new UpdateUserResponse(PROFILE_UPDATE_SUCCESSFUL.name());

    }

    private JsonPatch buildUpdatePatch(UpdateUserRequest updateUserRequest) {
        Field[] fields = updateUserRequest.getClass().getDeclaredFields();
        List<ReplaceOperation> operations = Arrays.stream(fields)
                                                  .filter(field ->  validateField(updateUserRequest, field))
                                                  .map(field->buildReplaceOperation(updateUserRequest, field))
                                                  .toList();
        List<JsonPatchOperation> patchOperations = new ArrayList<>(operations);
        return new JsonPatch(patchOperations);
    }

    private static boolean validateField(UpdateUserRequest updateUserRequest, Field field) {
        List<String> list = List.of("interests", "street", "houseNumber", "country", "state", "gender", "profileImage");
        field.setAccessible(true);
        try {
            return field.get(updateUserRequest)!=null &&!list.contains(field.getName());
        } catch (IllegalAccessException e) {
            throw new PromiscuousBaseException(e.getMessage());
        }
    }

    private static ReplaceOperation buildReplaceOperation(UpdateUserRequest updateUserRequest, Field field) {
        field.setAccessible(true);
        try {
            String path = JSON_PATCH_PATH_PREFIX+field.getName();
            JsonPointer pointer = new JsonPointer(path);
            var value = field.get(updateUserRequest);
            TextNode node = new TextNode(value.toString());
            return new ReplaceOperation(pointer, node);
        } catch (Exception exception) {
            throw new PromiscuousBaseException(exception.getMessage());
        }
    }

    public User findUserById(Long id){
        Optional<User> foundUser = userRepository.findById(id);
        User user = foundUser.orElseThrow(()->new UserNotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage()));
        return user;
    }


    private Pageable buildPageRequest(int page, int pageSize) {
        if (page<1&&pageSize<1)return PageRequest.of(0, 10);
        if (page<1)return PageRequest.of(0, pageSize);
        if (pageSize<1) return PageRequest.of(page, pageSize);
        return PageRequest.of(page-1, pageSize);
    }


    private ApiResponse<?> activateAccount(String token) {
        String email = extractEmailFrom(token);
        Optional<User> user = userRepository.readByEmail(email);
        User foundUser = user.orElseThrow(()->new UserNotFoundException(
                String.format(USER_WITH_EMAIL_NOT_FOUND_EXCEPTION.getMessage(), email)
        ));
        foundUser.setActive(true);
        User savedUser = userRepository.save(foundUser);
        GetUserResponse userResponse = buildUserResponse(savedUser);
        var activateUserResponse = buildActivateUserResponse(userResponse);
        return ApiResponse.builder().data(activateUserResponse).build();
    }

    private static ActivateAccountResponse buildActivateUserResponse(GetUserResponse userResponse) {
        return ActivateAccountResponse.builder()
                .message(ACCOUNT_ACTIVATION_SUCCESSFUL.name())
                .user(userResponse)
                .build();
    }

    private static GetUserResponse buildUserResponse(User savedUser) {
        return GetUserResponse.builder()
                .id(savedUser.getId())
                .address(savedUser.getAddress().toString())
                .fullName(getFullName(savedUser))
                .phoneNumber(savedUser.getPhoneNumber())
                .email(savedUser.getEmail())
                .build();
    }

    private static String getFullName(User savedUser) {
        return savedUser.getFirstName() + BLANK_SPACE + savedUser.getLastName();
    }

    private static ApiResponse<?> activateTestAccount() {
        return ApiResponse.builder()
                .build();
    }


    private EmailNotificationRequest buildEmailRequest(User savedUser){
        EmailNotificationRequest request =new EmailNotificationRequest();
        List<Recipient> recipients = new ArrayList<>();
        Recipient recipient = new Recipient(savedUser.getEmail());
        recipients.add(recipient);
        request.setRecipients(recipients);
        request.setSubject(WELCOME_MAIL_SUBJECT);
        String activationLink =
                generateActivationLink(appConfig.getBaseUrl(), savedUser.getEmail());
        String emailTemplate = getMailTemplate();
        String mailContent = String.format(emailTemplate, activationLink);
        request.setMailContent(mailContent);
        return request;
    }



}
