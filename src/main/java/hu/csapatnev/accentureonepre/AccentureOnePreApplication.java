package hu.csapatnev.accentureonepre;

import hu.csapatnev.accentureonepre.config.ApiTestContextInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
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

		if (args.length ==1 && args[0].equals("apitest")) {
			new SpringApplicationBuilder(AccentureOnePreApplication.class)
					.initializers(new ApiTestContextInitializer())
					.run();
		} else {
			SpringApplication.run(AccentureOnePreApplication.class, args);
		}
	}

	@Override
	public void run(ApplicationArguments args) {
		int fullCapacity = Integer.parseInt(env.getProperty("fullCapacity"));
		if (fullCapacity < 1) {
			throw new IllegalArgumentException("Full capacity must be positive number");
		}

		LocalDate stepTo10 = LocalDate.parse(env.getProperty("date.stepTo10"));
		LocalDate stepTo20 = LocalDate.parse(env.getProperty("date.stepTo20"));
		LocalDate stepTo30 = LocalDate.parse(env.getProperty("date.stepTo30"));
		LocalDate stepTo50 = LocalDate.parse(env.getProperty("date.stepTo50"));
		LocalDate stepTo100 = LocalDate.parse(env.getProperty("date.stepTo100"));

		List<LocalDate> stepsLocalDates = new ArrayList<>(Arrays.asList(stepTo10, stepTo20, stepTo30, stepTo50, stepTo100));
		validateStepsDateOrder(stepsLocalDates);

		String appStartInfoLog = "Application started with following parameters: " + "full capacity: " + fullCapacity +
				", step to 10 percent: " + stepTo10 +
				", step to 20 percent: " + stepTo20 +
				", step to 30 percent: " + stepTo30 +
				", step to 50 percent: " + stepTo50 +
				", step to 100 percent: " + stepTo100;
		logger.info(appStartInfoLog);

	}

	private static void validateStepsDateOrder(List<LocalDate> stepsLocalDates) {
		List<LocalDate> checkList = new ArrayList<>(stepsLocalDates);
		Collections.sort(checkList);
		if (!stepsLocalDates.equals(checkList)) {
			throw new IllegalArgumentException("Dates must be in order");
		}
	}



}
