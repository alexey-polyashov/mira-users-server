package mira.users.ms.validators;

import mira.users.ms.exceptions.NotFoundException;
import mira.users.ms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUniqEmailValidator implements ConstraintValidator<UniqUserEmail, String> {


    @Autowired
    private UserService usersService;

    @Override
    public void initialize(UniqUserEmail uniqUserEmail) {

    }

    @Override
    public boolean isValid(String checkValue, ConstraintValidatorContext ctx) {
        try {
            usersService.findByEmail(checkValue);
            return false;
        } catch (NotFoundException e){
            return true;
        }
    }

}
