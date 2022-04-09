package mira.users.ms.validators;

import mira.users.ms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUniqUserValidator implements ConstraintValidator<UniqUserName, String> {


    @Autowired
    private UserService usersService;

    @Override
    public void initialize(UniqUserName uniqUserName) {

    }

    @Override
    public boolean isValid(String checkValue, ConstraintValidatorContext ctx) {
        try {
            usersService.findByLogin(checkValue);
            return false;
        } catch (UsernameNotFoundException e){
            return true;
        }
    }

}
