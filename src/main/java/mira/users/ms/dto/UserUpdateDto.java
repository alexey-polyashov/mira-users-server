package mira.users.ms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
public class UserUpdateDto {

    @NotEmpty(message = "Login is empty")
    private String login = "";
    @NotEmpty(message = "Email is empty")
    private String email = "";

}
