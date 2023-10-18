package africa.semicolon.promeescuous.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DirectMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, columnDefinition = "MEDIUMTEXT", length = 1000)
    private String message;
    private Long senderId;
    @ManyToOne()
    private User user;
}
