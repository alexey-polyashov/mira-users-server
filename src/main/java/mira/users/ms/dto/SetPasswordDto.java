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
public class SetPasswordDto {

    @NotEmpty(message = "New password is empty")
    private String newPassword = "";
    @NotEmpty(message = "Old password is empty")
    private String oldPassword = "";

}
