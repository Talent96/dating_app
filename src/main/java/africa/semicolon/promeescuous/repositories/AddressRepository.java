package africa.semicolon.promeescuous.repositories;

import africa.semicolon.promeescuous.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
