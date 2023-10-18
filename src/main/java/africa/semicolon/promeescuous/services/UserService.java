package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.requests.MediaReactionRequest;
import africa.semicolon.promeescuous.dtos.requests.RegisterUserRequest;
import africa.semicolon.promeescuous.dtos.requests.UpdateUserRequest;
import africa.semicolon.promeescuous.dtos.responses.*;
import africa.semicolon.promeescuous.exceptions.UserNotFoundException;
import africa.semicolon.promeescuous.models.User;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    RegisterUserResponse register(RegisterUserRequest registerUserRequest);
    ApiResponse<?> activateUserAccount(String token);

    User findUserById(Long id);

    GetUserResponse getUserById(Long id) throws UserNotFoundException;

    List<GetUserResponse> getAllUsers(int page, int pageSize);

    UpdateUserResponse updateProfile(UpdateUserRequest updateUserRequest, Long id) throws JsonPatchException;

    UploadMediaResponse uploadMedia(MultipartFile mediaToUpload);

    UploadMediaResponse uploadProfilePicture(MultipartFile mediaToUpload);

    ApiResponse<?> reactToMedia(MediaReactionRequest mediaReactionRequest);

    User getUserByEmail(String email);
}
