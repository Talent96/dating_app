package africa.semicolon.promeescuous.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.AUTO;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MediaReaction {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @Enumerated(value = STRING)
    private Reaction reaction;
    private Long User;
}
