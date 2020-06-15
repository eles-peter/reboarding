package hu.csapatnev.accentureonepre;

import hu.csapatnev.accentureonepre.service.ReboardingService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Main {

    public static void main(String[] args) {
        System.out.println(LocalDate.now().minusDays(30));
        System.out.println(LocalDate.now().minusDays(15));
                System.out.println(LocalDate.now());
                        System.out.println(LocalDate.now().plusDays(15));
                                System.out.println(LocalDate.now().plusDays(30));
    }
}
