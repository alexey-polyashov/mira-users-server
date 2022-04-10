package mira.users.ms;

import mira.users.ms.dto.NewUserDto;
import mira.users.ms.dto.SetPasswordDto;
import mira.users.ms.dto.UserUpdateDto;
import mira.users.ms.entity.UserModel;
import mira.users.ms.exceptions.BadRequestException;
import mira.users.ms.exceptions.NotFoundException;
import mira.users.ms.repositories.UserRepository;
import mira.users.ms.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("dev")
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void newUserTest(){

        List<UserModel> users1 = userService.findAll();

        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setEmail("newuser@test.ru");
        newUserDto.setPassword("password");
        newUserDto.setLogin("newusertest");
        Long userId = userService.newUser(newUserDto);

        List<UserModel> users2 = userService.findAll();
        Assertions.assertEquals(users1.size()+1, users2.size());

        UserModel newUser = userService.findById(userId);

        Assertions.assertTrue(
                newUser.getLogin().equals(newUserDto.getLogin())
                && newUser.getEmail().equals(newUserDto.getEmail())
        );

        Assertions.assertTrue(passwordEncoder.matches(newUserDto.getPassword(), newUser.getPassword()));

    }

    @Test
    public void findAllTest(){

        List<UserModel> users1 = userService.findAll();
        Assertions.assertTrue(users1.size()>=2);

        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setEmail("findall@test.ee");
        newUserDto.setPassword("password");
        newUserDto.setLogin("findalltest");
        Long userId = userService.newUser(newUserDto);

        List<UserModel> users2 = userService.findAll();
        Assertions.assertEquals(users1.size()+1, users2.size());

    }

    @Test
    public void findById(){

        List<UserModel> users = userService.findAll();
        Long id = users.get(0).getId();
        UserModel user;
        Assertions.assertDoesNotThrow(()-> userService.findById(id));
        user = userService.findById(id);
        Assertions.assertEquals(user.getId(), id);
        userService.deleteUser(id);
        Assertions.assertThrows(NotFoundException.class,()->userService.findById(id));

    }

    @Test
    public void saveUserTest(){


        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setEmail("save@user.test");
        newUserDto.setPassword("password");
        newUserDto.setLogin("savelogin");
        Long userId = userService.newUser(newUserDto);

        List<UserModel> users1 = userService.findAll();

        UserUpdateDto updateUserDto = new UserUpdateDto();
        updateUserDto.setEmail("save2@user2.test");
        updateUserDto.setLogin("newlogin");

        UserModel newUser = userService.findById(userId);
        String oldPassword = newUser.getPassword();

        Long savedUserId = userService.saveUser(userId, updateUserDto);

        List<UserModel> users2 = userService.findAll();
        UserModel savedUser = userService.findById(userId);

        Assertions.assertEquals(userId, savedUserId);
        Assertions.assertEquals(users1.size(), users2.size());

        Assertions.assertTrue(
                savedUser.getLogin().equals(updateUserDto.getLogin())
                        && savedUser.getEmail().equals(updateUserDto.getEmail())
                        && oldPassword.equals(savedUser.getPassword())
         );

    }

    @Test
    public void deleteUserTest(){

        List<UserModel> users1 = userService.findAll();
        Long id = users1.get(1).getId();

        userService.deleteUser(id);

        List<UserModel> users2 = userService.findAll();

        Assertions.assertEquals(users1.size()-1, users2.size());
        Assertions.assertThrows(NotFoundException.class,()->userService.findById(id));

    }

    @Test
    public void setPasswordTest(){

        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setEmail("ee@ee.ee");
        newUserDto.setPassword("password");
        newUserDto.setLogin("login");

        SetPasswordDto setPasswordDto = new SetPasswordDto();
        setPasswordDto.setNewPassword("password2");
        setPasswordDto.setOldPassword("password3");

        Long userId = userService.newUser(newUserDto);

        Assertions.assertThrows(BadRequestException.class, ()->userService.setPassword(userId, setPasswordDto));

        setPasswordDto.setOldPassword("password");
        Assertions.assertDoesNotThrow(()->userService.setPassword(userId, setPasswordDto));

        UserModel newUser = userService.findById(userId);

        Assertions.assertTrue(passwordEncoder.matches(setPasswordDto.getNewPassword(), newUser.getPassword()));

    }

}
