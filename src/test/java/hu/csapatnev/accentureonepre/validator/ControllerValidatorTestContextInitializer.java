package hu.csapatnev.accentureonepre.validator;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;

public class ControllerValidatorTestContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        System.setProperty("fullCapacity", "250");
        System.setProperty("date.stepTo10", LocalDate.now().minusDays(30).toString());
        System.setProperty("date.stepTo20", LocalDate.now().minusDays(15).toString());
        System.setProperty("date.stepTo30", LocalDate.now().toString());
        System.setProperty("date.stepTo50", LocalDate.now().plusDays(15).toString());
        System.setProperty("date.stepTo100", LocalDate.now().plusDays(30).toString());
    }

}
