package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.requests.MediaReactionRequest;
import africa.semicolon.promeescuous.dtos.requests.RegisterUserRequest;
import africa.semicolon.promeescuous.dtos.responses.RegisterUserResponse;
import africa.semicolon.promeescuous.dtos.responses.UploadMediaResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static africa.semicolon.promeescuous.models.Reaction.LIKE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PromiscuousMediaServiceTest {
    @Autowired
    private MediaService mediaService;

    @Autowired
    private UserService userService;


    @Test
    void testToUploadProfilePicture(){
        String profilePicturePath = "C:\\Users\\USER\\Desktop\\spring\\promiscuous\\src\\main\\resources\\images\\L2u9aY5uuAOp1STLEzjhy3ttStLVC00wR7cpxesT.jpg";
        Path testPath = Paths.get(profilePicturePath);
        try(InputStream inputStream = Files.newInputStream(testPath)){
            MultipartFile multipartFile = new MockMultipartFile("test",inputStream);
            UploadMediaResponse uploadProfilePicture = mediaService.uploadProfilePicture(multipartFile);
            assertThat(uploadProfilePicture).isNotNull();
        } catch (IOException exception){
            throw new RuntimeException("Media upload failed");
        }
    }
    @Test
    void testVideoCannotBeUploadedWhereProfilePictureRequired(){
        String videoPath = "C:\\Users\\USER\\Desktop\\spring\\promiscuous\\src\\main\\resources\\images\\WhatsApp.mp4";
        Path testPath = Paths.get(videoPath);
        try(InputStream inputStream = Files.newInputStream(testPath)){
            MultipartFile multipartFile = new MockMultipartFile("test",inputStream);
            assertThrows(RuntimeException.class,()->mediaService.uploadProfilePicture(multipartFile));
        } catch (IOException exception){
            throw new RuntimeException("Media upload failed");
        }
    }

    @Test
    void testToUploadVideo(){
        String videoPath = "C:\\Users\\USER\\Desktop\\spring\\promiscuous\\src\\test\\resources\\images\\WhatsApp1.mp4";
        Path testPath = Paths.get(videoPath);
        try(InputStream inputStream = Files.newInputStream(testPath)){
            MultipartFile multipartFile = new MockMultipartFile("test",inputStream);
            UploadMediaResponse uploadedVideo = mediaService.uploadMedia(multipartFile, null);
            assertThat(uploadedVideo).isNotNull();
        } catch (IOException exception){
            throw new RuntimeException("Media upload failed");
        }
    }

    @Test
    void testToUploadImage(){
        String image = "C:\\Users\\USER\\Desktop\\spring\\promiscuous\\src\\main\\resources\\images\\plane.jpg";
        Path testPath = Paths.get(image);
        try(InputStream inputStream = Files.newInputStream(testPath)){
            MultipartFile multipartFile = new MockMultipartFile("test",inputStream);
            UploadMediaResponse uploadedImage = mediaService.uploadMedia(multipartFile, null);
            assertThat(uploadedImage).isNotNull();
        } catch (IOException exception){
            throw new RuntimeException("Media upload failed");
        }
    }

    @Test
    void testToLikeMedia(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail("hello@gmail.com");
        registerUserRequest.setPassword("password");
        RegisterUserResponse registerUserResponse = userService.register(registerUserRequest);
        assertNotNull(registerUserResponse);
        assertNotNull(registerUserResponse.getMessage());

        String response = mediaService.reactToMedia(new MediaReactionRequest(LIKE, 500L, 501L));
        assertThat(response).isNotNull();
    }


}