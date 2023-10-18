package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.requests.NotificationRequest;
import africa.semicolon.promeescuous.dtos.responses.NotificationResponse;

public interface NotificationService {
    NotificationResponse sendNotification(NotificationRequest notificationRequest);
}
