package hu.csapatnev.accentureonepre.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BeforeStepTo100Validator implements ConstraintValidator<BeforeStepTo100, LocalDate> {

    @Value("${date.stepTo100}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate stepTo100;

    public BeforeStepTo100Validator() {
    }

    @Override
    public void initialize(BeforeStepTo100 constraint) {
    }

    @Override
    public boolean isValid(LocalDate day, ConstraintValidatorContext context) {
        return (day.isBefore(stepTo100));
    }

    public LocalDate getStepTo100() {
        return stepTo100;
    }

    public void setStepTo100(LocalDate stepTo100) {
        this.stepTo100 = stepTo100;
    }
}