package africa.semicolon.promeescuous.repositories;

import africa.semicolon.promeescuous.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
