package africa.semicolon.promeescuous.dtos.requests;

import africa.semicolon.promeescuous.models.Reaction;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class MediaReactionRequest {
    private Reaction reaction;
    private Long mediaId;
    private Long userId;
}