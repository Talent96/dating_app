package africa.semicolon.promeescuous.services.cloud;

import africa.semicolon.promeescuous.dtos.responses.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CloudService {
    String upload(MultipartFile file);
}
