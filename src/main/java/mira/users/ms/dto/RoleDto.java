package mira.users.ms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;


@Getter
@Setter
@NoArgsConstructor
public class RoleDto{

    @NotEmpty(message = "Name is empty")
    private String name= "";

}