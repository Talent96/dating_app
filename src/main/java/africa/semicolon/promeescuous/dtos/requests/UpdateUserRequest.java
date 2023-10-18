package africa.semicolon.promeescuous.dtos.requests;

import africa.semicolon.promeescuous.models.Interest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class UpdateUserRequest {
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String password;
  private String gender;
  private Set<String> interests;
  private MultipartFile profileImage;
  private String phoneNumber;

  private String houseNumber;

  private String street;

  private String state;

  private String country;

}
