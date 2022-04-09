package mira.users.ms.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mira.users.ms.validators.UniqUserEmail;
import mira.users.ms.validators.UniqUserName;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Data
public class NewUserDto {

    @UniqUserName
    @NotBlank(message="Login is empty")
    private String login = "";

    @NotBlank(message="Password is empty")
    private String password = "";

    @NotBlank(message="Email is empty")
    @Pattern(
            regexp = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$",
            message = "Email is incorrect"
    )
    @UniqUserEmail
    private String email = "";

}
