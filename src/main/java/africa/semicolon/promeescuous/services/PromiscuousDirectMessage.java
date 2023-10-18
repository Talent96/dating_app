package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.requests.DirectMessageRequest;
import africa.semicolon.promeescuous.dtos.responses.DirectMessageResponse;
import africa.semicolon.promeescuous.dtos.responses.FindAllMessagesResponse;
import africa.semicolon.promeescuous.exceptions.PromiscuousBaseException;
import africa.semicolon.promeescuous.exceptions.UserNotFoundException;
import africa.semicolon.promeescuous.models.DirectMessage;
import africa.semicolon.promeescuous.models.User;
import africa.semicolon.promeescuous.repositories.DirectMessageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static africa.semicolon.promeescuous.exceptions.ExceptionMessage.INVALID_CREDENTIALS_EXCEPTION;
import static africa.semicolon.promeescuous.exceptions.ExceptionMessage.USER_NOT_FOUND_EXCEPTION;

@Service
@AllArgsConstructor
@Slf4j
public class PromiscuousDirectMessage implements DirectMessageService {
    private final UserService userService;
    private final DirectMessageRepository directMessageRepository;

    @Override
    public DirectMessageResponse send(DirectMessageRequest request, Long senderId, Long receiverId) {
        String message = request.getMessage();

        if (message == null){
            throw new PromiscuousBaseException(INVALID_CREDENTIALS_EXCEPTION.getMessage());
        }

        User receiverFound = userService.findUserById(receiverId);
        if (receiverFound == null){
            throw new UserNotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage());
        }

        DirectMessage directMessage = new DirectMessage();
        directMessage.setSenderId(senderId);
        directMessage.setUser(receiverFound);
        directMessage.setMessage(message);

        directMessageRepository.save(directMessage);

        User senderFound = userService.findUserById(senderId);

        return DirectMessageResponse.builder()
                .firstName(senderFound.getFirstName())
                .lastName(senderFound.getLastName())
                .message(directMessage.getMessage())
                .build();
    }

    @Override
    public FindAllMessagesResponse findMessagesByIds(long senderId, long receiverId) {
        List<DirectMessage> allMessages = directMessageRepository.findAll();
        List<String> specificMessages = new ArrayList<>();

        for (int i = 0; i < allMessages.size(); i++) {
            DirectMessage message = allMessages.get(i);

            if (message.getSenderId().equals(senderId)
                    && message.getUser().getId().equals(receiverId)
                    || message.getSenderId().equals(receiverId)
                    && message.getUser().getId().equals(senderId)){

               specificMessages.add(message.getMessage());

            }
        }
        return FindAllMessagesResponse.builder()
                .message(specificMessages)
                .build();
    }


}
