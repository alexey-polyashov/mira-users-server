package mira.users.ms.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUniqEmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqUserEmail {

    String message() default "Email is busy";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
