package mira.users.ms;

import mira.users.ms.entity.UserModel;
import mira.users.ms.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("dev")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findAllTest(){
        List<UserModel> users = userRepository.findAll();
        Assertions.assertTrue(users.size()>=2);
    }

    @Test
    public void findByLogin(){
        List<UserModel> users = userRepository.findAll();
        String login = users.get(0).getLogin();
        Optional<UserModel> user = userRepository.findByLogin(login);
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(user.get().getLogin(), login);
        userRepository.delete(user.get());
        user = userRepository.findByLogin(users.get(0).getLogin());
        Assertions.assertFalse(user.isPresent());
    }

    @Test
    public void findByEmail(){
        List<UserModel> users = userRepository.findAll();
        String email = users.get(0).getEmail();
        Optional<UserModel> user = userRepository.findByEmail(email);
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(user.get().getEmail(), email);
        userRepository.delete(user.get());
        user = userRepository.findByEmail(users.get(0).getLogin());
        Assertions.assertFalse(user.isPresent());
    }

}
