package hu.csapatnev.accentureonepre.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    void testDistance() {
        Point firstPoint = new Point(0, 0);
        Point secondPoint = new Point(3, 4);

        double distance = firstPoint.distance(secondPoint);

        Assertions.assertEquals(5, distance);
    }


}