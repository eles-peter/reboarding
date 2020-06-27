package hu.csapatnev.accentureonepre;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class AccentureOnePreApplicationTest {

    private AccentureOnePreApplication accentureOnePreApplication;

//
//    @Test
//    void TestvalidateOrThrowStepsDate_WithValidArguments() {
//        LocalDate stepTo10 = LocalDate.of(2020,5,15);
//        LocalDate stepTo20 = LocalDate.of(2020,5,31);
//        LocalDate stepTo30 = LocalDate.of(2020,6,15);
//        LocalDate stepTo50 = LocalDate.of(2020,6,30);
//        LocalDate stepTo100 = LocalDate.of(2020,7,15);
//
//        Assertions.assertDoesNotThrow(() -> {
//            AccentureOnePreApplication.validateOrThrowStepsDate(stepTo10, stepTo20, stepTo30, stepTo50, stepTo100);
//        });
//    }
//
//    @Test
//    void TestvalidateOrThrowStepsDate_WithEqualsDates() {
//        LocalDate stepTo10 = LocalDate.of(2020,5,15);
//        LocalDate stepTo20 = LocalDate.of(2020,5,15);
//        LocalDate stepTo30 = LocalDate.of(2020,5,15);
//        LocalDate stepTo50 = LocalDate.of(2020,5,15);
//        LocalDate stepTo100 = LocalDate.of(2020,5,15);
//
//        Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            AccentureOnePreApplication.validateOrThrowStepsDate(stepTo10, stepTo20, stepTo30, stepTo50, stepTo100);
//        });
//    }
//
//    @Test
//    void TestvalidateOrThrowStepsDate_WithInvalidOrder() {
//        LocalDate stepTo10 = LocalDate.of(2020,5,15);
//        LocalDate stepTo20 = LocalDate.of(2020,5,31);
//        LocalDate stepTo30 = LocalDate.of(2020,6,15);
//        LocalDate stepTo50 = LocalDate.of(2020,4,30);
//        LocalDate stepTo100 = LocalDate.of(2020,7,15);
//
//        Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            accentureOnePreApplication.validateOrThrowStepsDate(stepTo10, stepTo20, stepTo30, stepTo50, stepTo100);
//        });
//    }



}