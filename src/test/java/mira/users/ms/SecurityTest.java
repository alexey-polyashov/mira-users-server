package mira.users.ms;

import com.fasterxml.jackson.databind.ObjectMapper;
import mira.users.ms.dto.JwtRequestDTO;
import mira.users.ms.dto.JwtResponseDTO;
import mira.users.ms.dto.NewUserDto;
import mira.users.ms.dto.RoleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private NewUserDto newUserDto;
    private RoleDto roleDto;
    private String[] rolesDto = {"newRole"};

    @BeforeEach
    public void init(){
        this.jwtRequestDTO = new JwtRequestDTO();
        this.jwtRequestDTO.setPassword("foo");
        this.jwtRequestDTO.setUsername("foo");

        this.jwtResponseDTO = new JwtResponseDTO();
        this.jwtResponseDTO.setToken("foo");

        this.newUserDto = new NewUserDto();
        this.newUserDto.setLogin("newUser");
        this.newUserDto.setPassword("newUser");
        this.newUserDto.setEmail("newuser@new.ru");

        this.roleDto = new RoleDto();
        this.roleDto.setName("newRole");

        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void securityAccessAllowedTest() throws Exception {

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
    public void securityAccessDeniedTest() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/v1/users/user1"))
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
        mockMvc.perform(delete("/api/v1/users/1/roles"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put("/api/v1/users/1/roles/add"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser(username="user", authorities = "USER")
    public void checkUserAccess() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andDo(print())
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDto)))
                .andDo(print())
                .andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/v1/users/1"))
                .andDo(print())
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/users/1/roles"))
                .andDo(print())
                .andExpect(status().isForbidden());
        mockMvc.perform(put("/api/v1/users/1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rolesDto)))
                .andDo(print())
                .andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/v1/users/1/roles/newROle"))
                .andDo(print())
                .andExpect(status().isForbidden());
        mockMvc.perform(put("/api/v1/users/1/roles/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rolesDto)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}
