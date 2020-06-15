package hu.csapatnev.accentureonepre.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class NotBeforeStepTo10Validator implements ConstraintValidator<NotBeforeStepTo10, LocalDate> {

   @Value("${date.stepTo10}")
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private LocalDate stepTo10;

   public NotBeforeStepTo10Validator() {

   }

   @Override
   public void initialize(NotBeforeStepTo10 constraint) {
   }

   @Override
   public boolean isValid(LocalDate day, ConstraintValidatorContext context) {
      return (!day.isBefore(stepTo10));
   }

   public LocalDate getStepTo10() {
      return stepTo10;
   }

   public void setStepTo10(LocalDate stepTo10) {
      this.stepTo10 = stepTo10;
   }
}
