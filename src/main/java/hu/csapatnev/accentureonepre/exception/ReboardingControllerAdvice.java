package hu.csapatnev.accentureonepre.exception;

import hu.csapatnev.accentureonepre.controller.ReboardingController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collection;

import static java.util.stream.Collectors.joining;
import static org.springframework.core.log.LogMessage.format;

@ControllerAdvice(assignableTypes = {ReboardingController.class})
public class ReboardingControllerAdvice {

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException exception, WebRequest request) {
        String message = getMessage(exception.getConstraintViolations());
        ApiError apiError =  new ApiError(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

        private String getMessage(Collection<ConstraintViolation<?>> constraintViolations) {
        return constraintViolations.stream()
                .map(violation -> {
                    String parameter = violation.getPropertyPath().toString();
                    String invalidValue = violation.getInvalidValue().toString();
                    String validationMessage = violation.getMessage();
                    return format("'%s' %s: %s", parameter, invalidValue, validationMessage);
                })
                .collect(joining(", "));
    }

}
