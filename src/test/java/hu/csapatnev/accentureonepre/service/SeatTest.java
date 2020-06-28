package hu.csapatnev.accentureonepre.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeatTest {

    @Test
    void testIsSocialDistance_withTrue() {
        int socialDistance = 3;
        Seat firstSeat = new Seat(0, 0);
        Seat secondSeat = new Seat(3, 4);

        Assertions.assertTrue(firstSeat.isSocialDistance(secondSeat,socialDistance));
    }

    @Test
    void testIsSocialDistance_withFalse() {
        int socialDistance = 6;
        Seat firstSeat = new Seat(0, 0);
        Seat secondSeat = new Seat(3, 4);

        Assertions.assertFalse(firstSeat.isSocialDistance(secondSeat,socialDistance));
    }

    @Test
    void testDistance() {
        Seat firstSeat = new Seat(0, 0);
        Seat secondSeat = new Seat(3, 4);

        double distance = firstSeat.distance(secondSeat);

        Assertions.assertEquals(5, distance);
    }
}