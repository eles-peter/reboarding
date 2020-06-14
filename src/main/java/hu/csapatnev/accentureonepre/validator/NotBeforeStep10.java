package hu.csapatnev.accentureonepre.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotBeforeStep10Validator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBeforeStep10 {
    String message() default "is before tracked days";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
