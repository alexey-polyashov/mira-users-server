package mira.users.ms.restcontrollers;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mira.users.ms.dto.*;
import mira.users.ms.services.UserService;
import org.modelmapper.ModelMapper;
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

    @GetMapping
    public List<UserDto> getUsers(){
        log.info("UsersController, invoke method: getUsers");
        return userService.findAll()
                .stream()
                .map(p->modelMapper.map(p, UserDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId){
        log.info("UsersController, invoke method: getUser {}", userId);
        return modelMapper.map(userService.findById(userId), UserDto.class);
    }

    @PostMapping
    public Long newUser(@Valid @RequestBody NewUserDto userDto){
        log.info("UsersController, invoke method: newUser");
        return userService.newUser(userDto);
    }

    @PutMapping("/{userId}")
    public Long saveUser(@PathVariable Long userId, @Valid @RequestBody UserUpdateDto userDto){
        log.info("UsersController, invoke method: saveUser {}", userId);
        return userService.saveUser(userId, userDto);
    }

    @PutMapping("/{userId}/password")
    public Long changePasswordUsers(@PathVariable Long userId,@Valid @RequestBody SetPasswordDto passwordDto){
        log.info("UsersController, invoke method: changePasswordUsers {}", userId);
        return userService.setPassword(userId, passwordDto);
    }

    @DeleteMapping("/{userId}")
    public void delUser(@PathVariable Long userId){
        log.info("UsersController, invoke method: delUser {}", userId);
        userService.deleteUser(userId);
    }

    @GetMapping("/{userId}/roles")
    public Set<RoleDto> getUserRoles(@PathVariable Long userId){
        log.info("UsersController, invoke method: getUserRoles {}", userId);
        return userService.getUserRoles(userId);
    }

    @PutMapping("/{userId}/roles")
    public Set<RoleDto> saveRoles(@PathVariable Long userId, @RequestBody List<String> roles){
        log.info("UsersController, invoke method: saveRoles {}", userId);
        return userService.setUserRoles(userId, roles);
    }

    @PutMapping("/{userId}/roles/add/")
    public Set<RoleDto> addRoles(@PathVariable Long userId, @RequestBody List<String> roles){
        log.info("UsersController, invoke method: addRole {}", userId);
        return userService.addUserRoles(userId, roles);
    }

    @DeleteMapping("/{userId}/roles/{roleName}")
    public Set<RoleDto> deleteUserRoles(@PathVariable Long userId, @PathVariable String roleName){
        log.info("UsersController, invoke method: deleteUserRoles {}", userId);
        return userService.deleteUserRole(userId, roleName);
    }
}
