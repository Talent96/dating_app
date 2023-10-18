package africa.semicolon.promiscuous.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindUserRequest {
    private long id;
    private int page;
    private int pageSize;
}
