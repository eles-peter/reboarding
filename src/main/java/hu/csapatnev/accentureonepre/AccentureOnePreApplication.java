package hu.csapatnev.accentureonepre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class AccentureOnePreApplication implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(AccentureOnePreApplication.class);

    @Autowired
    private Environment env;

    public static void main(String[] args) {

        SpringApplication.run(AccentureOnePreApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {

        LocalDate stepTo5 = LocalDate.parse(env.getProperty("date.stepTo5"));
        LocalDate stepTo4 = LocalDate.parse(env.getProperty("date.stepTo4"));
        LocalDate stepTo3 = LocalDate.parse(env.getProperty("date.stepTo3"));
        LocalDate stepTo2 = LocalDate.parse(env.getProperty("date.stepTo2"));
        LocalDate stepTo1 = LocalDate.parse(env.getProperty("date.stepTo1"));

        validateOrThrowStepsDate(stepTo5, stepTo4, stepTo3, stepTo2, stepTo2);

        String appStartInfoLog = "Application started with following parameters: " +
                " step to 5 meter social distance: " + stepTo5 +
                ", step to 4 meter social distance: " + stepTo4 +
                ", step to 3 meter social distance: " + stepTo3 +
                ", step to 2 meter social distance: " + stepTo2 +
                ", step to 1 meter social distance: " + stepTo1;
        logger.info(appStartInfoLog);
    }

    public static void validateOrThrowStepsDate(
            LocalDate stepTo5, LocalDate stepTo4, LocalDate stepTo3, LocalDate stepTo2, LocalDate stepTo1) {

        if (stepTo5.equals(stepTo1)) {
            throw new IllegalArgumentException("Start date and end date can not be equals");
        }

        List<LocalDate> stepsLocalDates = new ArrayList<>(Arrays.asList(stepTo5, stepTo4, stepTo3, stepTo2, stepTo1));
        List<LocalDate> checkList = new ArrayList<>(stepsLocalDates);
        Collections.sort(checkList);
        if (!stepsLocalDates.equals(checkList)) {
            throw new IllegalArgumentException("The dates of the steps must be in chronological order");
        }

    }


}
