package africa.semicolon.promeescuous.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Set;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Table(name = "users")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateOfBirth;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Address address;
    @Enumerated(STRING)
    private Gender gender;
    private String createdAt;
    private String firstName;

    private String lastName;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = STRING)
    private Set<Interest> interests;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true)
    private String phoneNumber;
    @Column(nullable = false)
    private String password;
    @Enumerated(value = STRING)
    private Role role;
    private boolean isActive;


    @PrePersist
    public void setCreatedAt(){
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        createdAt = currentTime.format(formatter);

    }

}
