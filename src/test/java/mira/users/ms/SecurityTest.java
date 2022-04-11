package mira.users.ms;

import com.fasterxml.jackson.databind.ObjectMapper;
import mira.users.ms.dto.JwtRequestDTO;
import mira.users.ms.dto.JwtResponseDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private JwtRequestDTO jwtRequestDTO;
    private JwtResponseDTO jwtResponseDTO;

    @BeforeEach
    private void init(){
        this.jwtRequestDTO = new JwtRequestDTO();
        this.jwtRequestDTO.setPassword("foo");
        this.jwtRequestDTO.setUsername("foo");

        this.jwtResponseDTO = new JwtResponseDTO();
        this.jwtResponseDTO.setToken("foo");

        this.objectMapper = new ObjectMapper();
    }

    @Test
    private void securityAccessAllowedTest() throws Exception {

        mockMvc.perform(post("/api/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jwtRequestDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/auth/checkcredentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jwtResponseDTO))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    private void securityAccessDeniedTest() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/v1/users/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/v1/users/"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put("/api/v1/users/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put("/api/v1/users/1/password"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/api/v1/users/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/v1/users/1/roles"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put("/api/v1/users/1/roles"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put("/api/v1/users/1/roles/add"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


}
