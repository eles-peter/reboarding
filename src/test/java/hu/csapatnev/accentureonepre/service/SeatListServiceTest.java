package hu.csapatnev.accentureonepre.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SeatListServiceTest {

    private SeatListService seatListService = new SeatListService();

    @Test
    void testSeatListService() {
        seatListService.init();
        List<List<Point>> centerPointListList = seatListService.getCopyOfCenterPointListList();

        Assertions.assertEquals(2, centerPointListList.size());
        Assertions.assertEquals(9, centerPointListList.get(0).size());
        Assertions.assertEquals(4, centerPointListList.get(1).size());
        Assertions.assertFalse(centerPointListList.get(0).contains(new Point(100,100)));
        Assertions.assertFalse(centerPointListList.get(1).contains(new Point(100,100)));
    }
}