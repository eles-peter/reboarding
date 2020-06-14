package hu.csapatnev.accentureonepre;

import hu.csapatnev.accentureonepre.service.ReboardingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Arrays;

@SpringBootApplication
public class AccentureOnePreApplication {



	public static void main(String[] args) {

		ConfigurableApplicationContext applicationContext = SpringApplication.run(AccentureOnePreApplication.class, args);
		ReboardingService reboardingService = applicationContext.getBean(ReboardingService.class);

		if (args.length > 0) {
			// Külöm metódusba kiemelni!!!!

//			if (args[0].equals("a")) {
//
//			} else if (args[0].equals("c")) {
////				validateOrThrow(args);
//			} else {
//				throw new IllegalArgumentException();
//			}

			reboardingService.setReboardingService(250, LocalDate.of(2020,8,3),LocalDate.of(2020,8,10),LocalDate.of(2020,8,20),LocalDate.of(2020,8,30),LocalDate.of(2020,9,3) );
			System.out.println(reboardingService);
		} else {
			reboardingService.setReboardingService();
			System.out.println(reboardingService);
		}




	}


}
