package africa.semicolon.promeescuous.models;


import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, columnDefinition = "MEDIUMTEXT", length = 1000)
    private String content;
    private Long sender;
    @ManyToOne
    private User user;
}
