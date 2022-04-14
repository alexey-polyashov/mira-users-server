package mira.users.ms.restcontrollers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mira.users.ms.dto.*;
import mira.users.ms.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Api(value = "UsersController", tags = "Контролер для работы со списком пользователей")
public class UsersController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @ApiOperation(
            value = "Получение списка пользователей",
            notes = "Доступ - только администратор"
    )
    public List<UserDto> getUsers(){
        log.info("UsersController, invoke method: getUsers");
        return userService.findAll()
                .stream()
                .map(p->modelMapper.map(p, UserDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{userLogin}")
    @ApiOperation(
            value = "Получение данных пользователя по логину",
            notes = "Доступ - для авторизованных пользователей"
    )
    public UserDto getUser(@PathVariable String userLogin){
        log.info("UsersController, invoke method: getUser {}", userLogin);
        return modelMapper.map(userService.findByLogin(userLogin), UserDto.class);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ApiOperation(
            value = "Создание нового пользователя",
            notes = "Доступ - только администратор"
    )
    public Long newUser(@Valid @RequestBody NewUserDto userDto){
        log.info("UsersController, invoke method: newUser");
        return userService.newUser(userDto);
    }

    @PutMapping("/{userId}")
    @ApiOperation(
            value = "Сохранение данных пользователя",
            notes = "Доступ - для авторизованных пользователей"
    )
    public Long saveUser(@PathVariable Long userId, @Valid @RequestBody UserUpdateDto userDto){
        log.info("UsersController, invoke method: saveUser {}", userId);
        return userService.saveUser(userId, userDto);
    }

    @PutMapping("/{userId}/password")
    @ApiOperation(
            value = "Изменение пароля пользователя",
            notes = "Доступ - для авторизованных пользователей"
    )
    public Long changePasswordUsers(@PathVariable Long userId,@Valid @RequestBody SetPasswordDto passwordDto){
        log.info("UsersController, invoke method: changePasswordUsers {}", userId);
        return userService.setPassword(userId, passwordDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{userId}")
    @ApiOperation(
            value = "Удаление пользователя",
            notes = "Доступ - только администратор"
    )
    public void delUser(@PathVariable Long userId){
        log.info("UsersController, invoke method: delUser {}", userId);
        userService.deleteUser(userId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{userId}/roles")
    @ApiOperation(
            value = "Получение списка ролей пользователя",
            notes = "Доступ - только администратор"
    )
    public Set<RoleDto> getUserRoles(@PathVariable Long userId){
        log.info("UsersController, invoke method: getUserRoles {}", userId);
        return userService.getUserRoles(userId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{userId}/roles")
    @ApiOperation(
            value = "Изменение списка ролей пользователя",
            notes = "Доступ - только администратор"
    )
    public Set<RoleDto> saveRoles(@PathVariable Long userId, @RequestBody Set<String> roles){
        log.info("UsersController, invoke method: saveRoles {}", userId);
        return userService.setUserRoles(userId, roles);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{userId}/roles/add")
    @ApiOperation(
            value = "Добавление ролей пользователю",
            notes = "Доступ - только администратор"
    )
    public Set<RoleDto> addRoles(@PathVariable Long userId, @RequestBody Set<String> roles){
        log.info("UsersController, invoke method: addRole {}", userId);
        return userService.addUserRoles(userId, roles);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{userId}/roles/{roleName}")
    @ApiOperation(
            value = "Удаление роли у пользователя",
            notes = "Доступ - только администратор"
    )
    public Set<RoleDto> deleteUserRoles(@PathVariable Long userId, @PathVariable String roleName){
        log.info("UsersController, invoke method: deleteUserRoles {}", userId);
        return userService.deleteUserRole(userId, roleName);
    }
}
