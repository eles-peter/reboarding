package hu.csapatnev.accentureonepre.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.configuration.IMockitoConfiguration;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SeatAllocationServiceTest {

    private SeatListService seatListServiceMock = mock(SeatListService.class);
    private SeatAllocationService seatAllocationService;

    @BeforeEach
    public void init() {
        seatAllocationService = new SeatAllocationService(seatListServiceMock);
        List<Point> mockPointList = new ArrayList<>();
        mockPointList.add(new Point(0, 0));
        mockPointList.add(new Point(0, 3));
        mockPointList.add(new Point(0, 6));
        mockPointList.add(new Point(4, 0));
        mockPointList.add(new Point(4, 3));
        mockPointList.add(new Point(4, 6));
        mockPointList.add(new Point(8, 0));
        mockPointList.add(new Point(8, 3));
        mockPointList.add(new Point(8, 6));
        List<List<Point>> mockPointListList = new ArrayList<>();
        mockPointListList.add(mockPointList);
        when(seatListServiceMock.getCopyOfCenterPointListList()).thenReturn(mockPointListList);
    }


    @Test
    void testGetSeatAllocation_With11SocialDistance() {
        Set<Point> seatAllocation = seatAllocationService.getSeatAllocation(11);
        Assertions.assertEquals(1, seatAllocation.size());
    }

    @Test
    void testGetSeatAllocation_With9SocialDistance() {
        Set<Point> seatAllocation = seatAllocationService.getSeatAllocation(9);
        Assertions.assertEquals(2, seatAllocation.size());
    }

    @Test
    void testGetSeatAllocation_With6SocialDistance() {
        Set<Point> seatAllocation = seatAllocationService.getSeatAllocation(6);
        Assertions.assertEquals(4, seatAllocation.size());
    }

    @Test
    void testGetSeatAllocation_With5SocialDistance() {
        Set<Point> seatAllocation = seatAllocationService.getSeatAllocation(5);
        Assertions.assertEquals(5, seatAllocation.size());
    }

    @Test
    void testGetSeatAllocation_With4SocialDistance() {
        Set<Point> seatAllocation = seatAllocationService.getSeatAllocation(4);
        Assertions.assertEquals(6, seatAllocation.size());
    }

    @Test
    void testGetSeatAllocation_With3SocialDistance() {
        Set<Point> seatAllocation = seatAllocationService.getSeatAllocation(3);
        Assertions.assertEquals(9, seatAllocation.size());
    }


}