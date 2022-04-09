package mira.users.ms.dto;

import lombok.Data;

@Data
public class JwtRequestDTO {
    private String username;
    private String password;
}
