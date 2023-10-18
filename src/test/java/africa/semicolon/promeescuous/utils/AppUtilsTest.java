package africa.semicolon.promeescuous.utils;

import africa.semicolon.promeescuous.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static africa.semicolon.promeescuous.utils.AppUtil.generateActivationLink;
import static africa.semicolon.promeescuous.utils.JwtUtil.generateVerificationToken;
import static org.assertj.core.api.Assertions.*;


@Slf4j
class AppUtilsTest {

    @Autowired
    private AppConfig appConfig;

    @Test
    public void testGenerateActivationLink(){
        String activationLink = generateActivationLink(appConfig.getBaseUrl(), "test@email.com");
        log.info("activation link -->{}", activationLink);
        assertThat(activationLink).isNotNull();
        assertThat(activationLink).contains("http://localhost:8080/activate");
    }


    @Test
    public void generateTokenTest() {
        String email = "test@email.com";
        String token = generateVerificationToken(email);
        log.info("generated token--->{}", token);
        assertThat(token).isNotNull();
    }
}