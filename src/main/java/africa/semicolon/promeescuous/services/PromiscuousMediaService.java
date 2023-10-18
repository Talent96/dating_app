package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.requests.MediaReactionRequest;
import africa.semicolon.promeescuous.dtos.responses.UploadMediaResponse;
import africa.semicolon.promeescuous.exceptions.PromiscuousBaseException;
import africa.semicolon.promeescuous.models.Media;
import africa.semicolon.promeescuous.models.MediaReaction;
import africa.semicolon.promeescuous.models.User;
import africa.semicolon.promeescuous.repositories.MediaRepository;
import africa.semicolon.promeescuous.services.cloud.CloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static africa.semicolon.promeescuous.dtos.responses.ResponseMessage.SUCCESS;
import static africa.semicolon.promeescuous.exceptions.ExceptionMessage.MEDIA_NOT_FOUND;
import static africa.semicolon.promeescuous.exceptions.ExceptionMessage.RESOURCE_NOT_FOUND;


@Service
public class PromiscuousMediaService implements MediaService{
    private final CloudService cloudService;
    private final MediaRepository mediaRepository;

    @Autowired
    public PromiscuousMediaService(CloudService cloudService, MediaRepository mediaRepository){
        this.cloudService = cloudService;
        this.mediaRepository = mediaRepository;
    }
    @Override
    public UploadMediaResponse uploadMedia(MultipartFile file, User user) {
        String url = cloudService.upload(file);
        Media media = new Media();
        media.setUrl(url);
        media.setUser(user);
        mediaRepository.save(media);
        UploadMediaResponse uploadMediaResponse = new UploadMediaResponse();
        uploadMediaResponse.setMessage(url);
        return uploadMediaResponse;
    }

    @Override
    public UploadMediaResponse uploadProfilePicture(MultipartFile file) {
        cloudService.upload(file);
        UploadMediaResponse response = new UploadMediaResponse();
        response.setMessage("Profile picture updated");
        return response;
    }

    @Override
    public String reactToMedia(MediaReactionRequest mediaReactionRequest) {
        Media media=mediaRepository.findById(mediaReactionRequest.getMediaId())
                                   .orElseThrow(()->
                new PromiscuousBaseException(MEDIA_NOT_FOUND.name()));
        MediaReaction reaction = new MediaReaction();
        reaction.setReaction(mediaReactionRequest.getReaction());
        reaction.setUser(mediaReactionRequest.getUserId());
        media.getReactions().add(reaction);
        mediaRepository.save(media);
        return SUCCESS.name();
    }

    @Override
    public Media getMediaByUser(User user) {
        return mediaRepository.findByUser(user).orElseThrow(
                ()->new PromiscuousBaseException(RESOURCE_NOT_FOUND.name())
        );
    }


}