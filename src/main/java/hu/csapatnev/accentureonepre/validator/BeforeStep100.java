package hu.csapatnev.accentureonepre.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BeforeStep100Validator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface BeforeStep100 {
    String message() default "is after tracked days";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
