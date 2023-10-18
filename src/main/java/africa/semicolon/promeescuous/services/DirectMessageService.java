package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.requests.DirectMessageRequest;
import africa.semicolon.promeescuous.dtos.responses.DirectMessageResponse;
import africa.semicolon.promeescuous.dtos.responses.FindAllMessagesResponse;
import africa.semicolon.promeescuous.models.DirectMessage;

import java.util.List;
import java.util.Optional;

public interface DirectMessageService {
    DirectMessageResponse send(DirectMessageRequest request, Long senderId, Long receiverId);

    FindAllMessagesResponse findMessagesByIds(long sender, long receiver);
}
