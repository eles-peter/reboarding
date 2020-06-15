package hu.csapatnev.accentureonepre;

import hu.csapatnev.accentureonepre.config.PostmanTestContextInitializer;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class AccentureOnePreApplication implements ApplicationRunner {
//
//	@Value("${date.stepTo10}")
//	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//	private static LocalDate stepTo10;
//	@Value("${date.stepTo20}")
//	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//	private static LocalDate stepTo20;
//	@Value("${date.stepTo30}")
//	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//	private static LocalDate stepTo30;
//	@Value("${date.stepTo50}")
//	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//	private static LocalDate stepTo50;
//	@Value("${date.stepTo100}")
//	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//	private static LocalDate stepTo100;

	private static final Logger logger = LoggerFactory.getLogger(AccentureOnePreApplication.class);

	@Autowired
	private Environment env;

	public static void main(String[] args) {

		if (args.length ==1 && args[0].equals("postmanTest")) {
			new SpringApplicationBuilder(AccentureOnePreApplication.class)
					.initializers(new PostmanTestContextInitializer())
					.run();
		} else {
//			if (args.length > 0) {
//				validateArgumentsBeforeRun(args);
//			}
			SpringApplication.run(AccentureOnePreApplication.class, args);
		}
	}

//	private static void validateArgumentsBeforeRun(String[] args) {
//		for (String arg : args) {
//			if (arg.startsWith("--date.stepTo100")) {
//				stepTo100 = getDateFromArg(arg);
//			} else if (arg.startsWith("--date.stepTo20")) {
//				stepTo20 = getDateFromArg(arg);
//			} else if (arg.startsWith("--date.stepTo30")) {
//				stepTo30 = getDateFromArg(arg);
//			} else if (arg.startsWith("--date.stepTo50")) {
//				stepTo50 = getDateFromArg(arg);
//			} else if (arg.startsWith("--date.stepTo10")) {
//				stepTo10 = getDateFromArg(arg);
//			}
//		}
//		List<LocalDate> stepsLocalDates = new ArrayList<>(Arrays.asList(stepTo10, stepTo20, stepTo30, stepTo50, stepTo100));
//		validateStepsDateOrder(stepsLocalDates);
//	}

	private static void validateStepsDateOrder(List<LocalDate> stepsLocalDates) {
		List<LocalDate> checkList = new ArrayList<>(stepsLocalDates);
		Collections.sort(checkList);
		if (!stepsLocalDates.equals(checkList)) {
			throw new IllegalArgumentException("Dates must be in order");
		}
	}
//
//	private static LocalDate getDateFromArg(String arg) {
//		String dateString = arg.substring(arg.length() - 10);
//		return LocalDate.parse(dateString);
//	}

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

//
//	public static List<LocalDate> getStepsLocalDatesOrThrow(String[] args) {
//
//		if (args.length != 5) {
//			throw new IllegalArgumentException();
//		}
//
//		List<LocalDate> stepsLocalDates = new ArrayList<>();
//		try {
//			for (String arg : args) {
//				stepsLocalDates.add(LocalDate.parse(arg));
//			}
//		} catch (DateTimeParseException e) {
//			throw new IllegalArgumentException();
//		}
//
//		validateStepsDateOrder(stepsLocalDates);
//
//		return stepsLocalDates;
//	}


}
