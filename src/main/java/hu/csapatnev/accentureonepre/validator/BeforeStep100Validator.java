package hu.csapatnev.accentureonepre.validator;

import hu.csapatnev.accentureonepre.service.ReboardingService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BeforeStep100Validator implements ConstraintValidator<BeforeStep100, LocalDate> {

    private ReboardingService reboardingService;

    public BeforeStep100Validator(ReboardingService reboardingService) {
        this.reboardingService = reboardingService;
    }

    @Override
    public void initialize(BeforeStep100 constraint) {
    }

    @Override
    public boolean isValid(LocalDate day, ConstraintValidatorContext context) {
        return (day.isBefore(reboardingService.getStepTo100()));
    }
}