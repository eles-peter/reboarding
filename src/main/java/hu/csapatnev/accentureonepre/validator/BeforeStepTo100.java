package hu.csapatnev.accentureonepre.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BeforeStepTo100Validator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface BeforeStepTo100 {
    String message() default "is after tracked days";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
