package africa.semicolon.promeescuous.dtos.responses;


import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GetUserResponse {
    private Long id;
    private String email;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String profileImage;
}
