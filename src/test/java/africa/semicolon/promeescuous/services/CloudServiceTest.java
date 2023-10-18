package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.responses.ApiResponse;
import africa.semicolon.promeescuous.services.cloud.CloudService;
import africa.semicolon.promeescuous.utils.AppUtil;
import lombok.extern.slf4j.Slf4j;
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

import static africa.semicolon.promeescuous.utils.AppUtil.TEST_IMAGE_LOCATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
public class CloudServiceTest {
    @Autowired
    private CloudService cloudService;

    @Test
    public void testUploadFile()  {
        Path path = Path.of(TEST_IMAGE_LOCATION);
        try(InputStream inputStream = Files.newInputStream(path)) {
            MultipartFile file = new MockMultipartFile("testImage", inputStream);
            String response = cloudService.upload(file);
            assertNotNull(response);
            assertThat(response).isNotNull();
        }catch (IOException exception){
            throw new RuntimeException(":(");
        }
    }

}
