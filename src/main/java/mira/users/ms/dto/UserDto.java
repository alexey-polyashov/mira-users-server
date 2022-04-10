package mira.users.ms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    private String login = "";
    private String email = "";
    private Long id;

    private Collection<RoleDto> roles = new ArrayList<>();

}
