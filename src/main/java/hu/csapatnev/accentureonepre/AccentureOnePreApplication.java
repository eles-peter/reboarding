package hu.csapatnev.accentureonepre;

import hu.csapatnev.accentureonepre.service.ReboardingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class AccentureOnePreApplication {



	public static void main(String[] args) {

		ConfigurableApplicationContext applicationContext = SpringApplication.run(AccentureOnePreApplication.class, args);
		ReboardingService reboardingService = applicationContext.getBean(ReboardingService.class);

		if (args.length > 0) {
			List<LocalDate> stepsLocalDates = getStepsLocalDatesOrThrow(args);
			reboardingService.setReboardingService(
					stepsLocalDates.get(0),
					stepsLocalDates.get(1),
					stepsLocalDates.get(2),
					stepsLocalDates.get(3),
					stepsLocalDates.get(4));

			System.out.println(reboardingService);
		} else {
			reboardingService.setReboardingService();
			System.out.println(reboardingService);
		}
	}

	public static List<LocalDate> getStepsLocalDatesOrThrow(String[] args) {
		if (args.length != 5) {
			throw new IllegalArgumentException();
		}

		List<LocalDate> stepsLocalDates = new ArrayList<>();
		try {
			for (String arg : args) {
				stepsLocalDates.add(LocalDate.parse(arg));
			}
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException();
		}

		List<LocalDate> checkList = new ArrayList<>(stepsLocalDates);
		Collections.sort(checkList);
		if (!stepsLocalDates.equals(checkList)) {
			throw new IllegalArgumentException();
		}

		return stepsLocalDates;
	}


}
