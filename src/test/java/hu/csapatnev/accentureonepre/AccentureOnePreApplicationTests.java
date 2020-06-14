package hu.csapatnev.accentureonepre;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static hu.csapatnev.accentureonepre.AccentureOnePreApplication.getStepsLocalDatesOrThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


class AccentureOnePreApplicationTests {

    @Test
    void testGetStepsLocalDatesOrThrow_WithValidArguments() {
        String[] args = {"2020-06-30", "2020-07-15", "2020-07-30", "2020-08-15", "2020-08-30"};

        List<LocalDate> stepsLocalDates = getStepsLocalDatesOrThrow(args);

        Assertions.assertEquals(5, stepsLocalDates.size());
    }

    @Test
    void testGetStepsLocalDatesOrThrow_WithInvalidAmountOfArguments() {
        String[] args = {"2020-06-30", "2020-07-15", "2020-07-30", "2020-08-15"};

        assertThrows(IllegalArgumentException.class, () -> getStepsLocalDatesOrThrow(args));
    }

    @Test
    void testGetStepsLocalDatesOrThrow_WithInvalidOrderOfArguments() {
        String[] args = {"2020-06-30", "2020-07-30", "2020-07-15", "2020-08-15", "2020-08-30"};

        assertThrows(IllegalArgumentException.class, () -> getStepsLocalDatesOrThrow(args));
    }

    @Test
    void testGetStepsLocalDatesOrThrow_WithInvalidArguments() {
        String[] args = {"2020-6-30", "2020-07-30", "2020-07-15", "2020-08-15", "2020-08-80"};

        assertThrows(IllegalArgumentException.class, () -> getStepsLocalDatesOrThrow(args));
    }


}
