package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.requests.EmailNotificationRequest;
import africa.semicolon.promeescuous.dtos.responses.EmailNotificationResponse;

public interface MailService {
    EmailNotificationResponse send(EmailNotificationRequest emailNotificationRequest);
}
