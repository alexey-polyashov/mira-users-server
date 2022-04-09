package mira.users.ms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mira.users.ms.validators.UniqUserEmail;
import mira.users.ms.validators.UniqUserName;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
@Setter
public class UserUpdateDto {

    @UniqUserName
    @NotEmpty(message = "Login is empty")
    private String login = "";

    @NotEmpty(message = "Email is empty")
    @Pattern(
            regexp = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$",
            message = "Email is incorrect"
    )
    @UniqUserEmail
    private String email = "";

}
