package africa.semicolon.promeescuous.dtos.responses;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ApiResponse <T>{
    private T data;
}
