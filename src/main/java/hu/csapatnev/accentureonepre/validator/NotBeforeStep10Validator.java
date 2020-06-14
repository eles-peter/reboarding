package hu.csapatnev.accentureonepre.validator;

import hu.csapatnev.accentureonepre.service.ReboardingService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class NotBeforeStep10Validator implements ConstraintValidator<NotBeforeStep10, LocalDate> {

   private ReboardingService reboardingService;

   public NotBeforeStep10Validator(ReboardingService reboardingService) {
      this.reboardingService = reboardingService;
   }

   @Override
   public void initialize(NotBeforeStep10 constraint) {
   }

   @Override
   public boolean isValid(LocalDate day, ConstraintValidatorContext context) {
      return (!day.isBefore(reboardingService.getStepTo10()));
   }
}
