package africa.semicolon.promeescuous.dtos.requests;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Sender {
    private String name;
    @NonNull
    private String email;
}
