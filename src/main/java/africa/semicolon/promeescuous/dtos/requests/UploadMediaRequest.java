package africa.semicolon.promeescuous.dtos.requests;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UploadMediaRequest {
    private MultipartFile media;
}