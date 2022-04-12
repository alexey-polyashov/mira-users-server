package mira.users.ms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mira.users.ms.dto.JwtRequestDTO;
import mira.users.ms.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
public class AuthControllerTest{

    @SpyBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    public void authTest() throws Exception {

        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        String encodePassword= encoder.encode("password");
        UserDetails userDetails = new User("user", encodePassword, roles);

        Mockito.doReturn(userDetails).when(userService).loadUserByUsername("user");
        Mockito.doReturn(userDetails).when(userService).getUserDetail("user");

        ObjectMapper objectMapper = new ObjectMapper();

        JwtRequestDTO jwtRequestDTO;
        jwtRequestDTO = new JwtRequestDTO();
        jwtRequestDTO.setPassword("password");
        jwtRequestDTO.setUsername("user");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jwtRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists());

    }

}
