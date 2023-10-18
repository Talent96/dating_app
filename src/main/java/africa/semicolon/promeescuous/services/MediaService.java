package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.requests.MediaReactionRequest;
import africa.semicolon.promeescuous.dtos.responses.UploadMediaResponse;
import africa.semicolon.promeescuous.models.Media;
import africa.semicolon.promeescuous.models.User;
import org.springframework.web.multipart.MultipartFile;

public interface MediaService {
    UploadMediaResponse uploadProfilePicture(MultipartFile file);
    UploadMediaResponse uploadMedia(MultipartFile file, User user);
    String reactToMedia(MediaReactionRequest mediaReactionRequest);

    Media getMediaByUser(User user);
}