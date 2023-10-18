package africa.semicolon.promeescuous.services;


import africa.semicolon.promeescuous.dtos.requests.LoginRequest;
import africa.semicolon.promeescuous.dtos.requests.RegisterUserRequest;
import africa.semicolon.promeescuous.dtos.requests.UpdateUserRequest;
import africa.semicolon.promeescuous.dtos.responses.ApiResponse;
import africa.semicolon.promeescuous.dtos.responses.GetUserResponse;
import africa.semicolon.promeescuous.dtos.responses.LoginResponse;
import africa.semicolon.promeescuous.dtos.responses.UpdateUserResponse;
import africa.semicolon.promeescuous.exceptions.BadCredentialsException;
import africa.semicolon.promeescuous.exceptions.PromiscuousBaseException;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static africa.semicolon.promeescuous.utils.AppUtil.BLANK_SPACE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@Sql(scripts = {"/db/insert.sql"})
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void testThatUserCanRegister(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail("rofime9859@royalka.com");
        registerUserRequest.setPassword("password");
        var registerUserResponse = userService.register(registerUserRequest);
        assertNotNull(registerUserResponse);
        assertNotNull(registerUserResponse.getMessage());
    }
    @Test
    public void testActivateUserAccount(){
        ApiResponse<?> activateUserAccountResponse =
                userService.activateUserAccount("abc1234.erytuuoi.67t75646");
       assertThat(activateUserAccountResponse).isNotNull();
    }



    @Test
    public void getUserByIdTest(){
        GetUserResponse response = userService.getUserById(500L);
        assertThat(response).isNotNull();
    }

    @Test
    public void getAllUsers(){
        List<GetUserResponse> users = userService.getAllUsers(1, 5);
        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(5);
    }

    @Test
    public void testThatUserCanUpdateAccount() throws JsonPatchException {
        UpdateUserRequest updateUserRequest = buildUpdateRequest();
        UpdateUserResponse response = userService.updateProfile(updateUserRequest, 500L);
        assertThat(response).isNotNull();
        GetUserResponse userResponse = userService.getUserById(500L);

        String fullName = userResponse.getFullName();
        String expectedFullName = new StringBuilder().append(updateUserRequest.getFirstName())
                                                     .append(BLANK_SPACE)
                                                     .append(updateUserRequest.getLastName())
                                                     .toString();
        assertThat(fullName).isEqualTo(expectedFullName);

    }

    private UpdateUserRequest buildUpdateRequest() {
        Set<String> interests = Set.of("swimming", "sports", "cooking");
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setDateOfBirth(LocalDate.of(2005, Month.NOVEMBER.ordinal(), 25));
        updateUserRequest.setFirstName("Sheriff");
        updateUserRequest.setLastName("Awofiranye");
        updateUserRequest.setPassword("password");
        MultipartFile testImage = getTestImage();
        updateUserRequest.setProfileImage(testImage);
        updateUserRequest.setInterests(interests);
        updateUserRequest.setCountry("Ghana");
        return updateUserRequest;
    }

    private MultipartFile getTestImage(){
        //obtain a path that points to test image
        Path path = Paths.get("C:\\Users\\semicolon\\Documents\\spring_projects\\prom-scuous\\src\\test\\resources\\images\\puppy_flex.jpg");
        //create stream that can read from file pointed to by path
        try(InputStream inputStream = Files.newInputStream(path)) {
           //create a MultipartFile using bytes from file pointed to by path
            MultipartFile image = new MockMultipartFile("test_image", inputStream);
            return image;
        }catch (Exception exception){
            throw new PromiscuousBaseException(exception.getMessage());
        }
    }

}



