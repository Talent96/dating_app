package africa.semicolon.promeescuous.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static africa.semicolon.promeescuous.utils.AppUtil.APP_EMAIL;
import static africa.semicolon.promeescuous.utils.AppUtil.APP_NAME;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class EmailNotificationRequest {
    private final Sender sender = new Sender(APP_NAME, APP_EMAIL);
    //to
    @JsonProperty("to")
    private List<Recipient> recipients;
    //cc
    @JsonProperty("cc")
    private List<String> copiedEmails;

    //htmlContent
    @JsonProperty("htmlContent")
    private String mailContent;

    private String textContent;

    private String subject;
}
