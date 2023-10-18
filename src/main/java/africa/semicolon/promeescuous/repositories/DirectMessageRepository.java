package africa.semicolon.promeescuous.repositories;

import africa.semicolon.promeescuous.models.DirectMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectMessageRepository extends JpaRepository<DirectMessage, Long> {
}
