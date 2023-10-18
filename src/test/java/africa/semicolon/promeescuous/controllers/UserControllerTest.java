package africa.semicolon.promeescuous.controllers;


import africa.semicolon.promeescuous.dtos.requests.RegisterUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testRegister(){
        try {
            RegisterUserRequest request = new RegisterUserRequest();
            request.setEmail("bibeni9669@touchend.com");
            request.setPassword("password");
            String json=mapper.writeValueAsString(request);
            mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/v1/user")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status()
                                             .is(HttpStatus.CREATED.value()))
                    .andDo(print());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    public void testGetUserById(){
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/1"))
                    .andExpect(status().is(HttpStatus.OK.value()))
                    .andDo(print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
