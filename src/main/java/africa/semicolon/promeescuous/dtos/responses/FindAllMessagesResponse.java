package africa.semicolon.promeescuous.dtos.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FindAllMessagesResponse {
    private List<String> message;
}
