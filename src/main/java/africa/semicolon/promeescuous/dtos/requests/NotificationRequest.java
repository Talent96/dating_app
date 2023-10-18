package africa.semicolon.promeescuous.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static africa.semicolon.promeescuous.utils.AppUtil.APP_EMAIL;
import static africa.semicolon.promeescuous.utils.AppUtil.APP_NAME;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private Long sender_id;

    @JsonProperty("to")
    private Long recipients_id;

    @JsonProperty("htmlContent")
    private String subject;
}
