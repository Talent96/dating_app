package africa.semicolon.promeescuous.repositories;

import africa.semicolon.promeescuous.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void readByEmailTest() {
        userRepository.save(
                User.builder().
                        email("test@email.com")
                        .password("12345")
                        .build());
        userRepository.save(
                User.builder().
                        email("test1@email.com")
                        .password("12345")
                        .build());

        userRepository.save(
                User.builder().
                        email("test2@email.com")
                        .password("12345")
                        .build());

        Optional<User> foundUser =userRepository.readByEmail("test1@email.com");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).isNotNull();
    }
}