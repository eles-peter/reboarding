package hu.csapatnev.accentureonepre;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class AccentureOnePreApplicationTest {

    private AccentureOnePreApplication accentureOnePreApplication;


    @Test
    void TestvalidateOrThrowStepsDate_WithValidArguments() {
        LocalDate stepTo5 = LocalDate.of(2020,5,15);
        LocalDate stepTo4 = LocalDate.of(2020,5,31);
        LocalDate stepTo3 = LocalDate.of(2020,6,15);
        LocalDate stepTo2 = LocalDate.of(2020,6,30);
        LocalDate stepTo1 = LocalDate.of(2020,7,15);
        LocalDate endOfPeriod = LocalDate.of(2020,7,30);

        Assertions.assertDoesNotThrow(() -> {
            AccentureOnePreApplication.validateOrThrowStepsDate(stepTo5, stepTo4, stepTo3, stepTo2, stepTo1, endOfPeriod);
        });
    }

    @Test
    void TestvalidateOrThrowStepsDate_WithEqualsDates() {
        LocalDate stepTo5 = LocalDate.of(2020,5,15);
        LocalDate stepTo4 = LocalDate.of(2020,5,15);
        LocalDate stepTo3 = LocalDate.of(2020,5,15);
        LocalDate stepTo2 = LocalDate.of(2020,5,15);
        LocalDate stepTo1 = LocalDate.of(2020,5,15);
        LocalDate endOfPeriod = LocalDate.of(2020,5,15);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AccentureOnePreApplication.validateOrThrowStepsDate(stepTo5, stepTo4, stepTo3, stepTo2, stepTo1, endOfPeriod);
        });
    }

    @Test
    void TestvalidateOrThrowStepsDate_WithInvalidOrder() {
        LocalDate stepTo5 = LocalDate.of(2020,5,15);
        LocalDate stepTo4 = LocalDate.of(2020,5,31);
        LocalDate stepTo3 = LocalDate.of(2020,6,15);
        LocalDate stepTo2 = LocalDate.of(2020,4,30);
        LocalDate stepTo1 = LocalDate.of(2020,7,15);
        LocalDate endOfPeriod = LocalDate.of(2020,7,10);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            accentureOnePreApplication.validateOrThrowStepsDate(stepTo5, stepTo4, stepTo3, stepTo2, stepTo1, endOfPeriod);
        });
    }



}