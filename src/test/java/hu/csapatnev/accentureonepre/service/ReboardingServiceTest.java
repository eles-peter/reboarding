package hu.csapatnev.accentureonepre.service;

import hu.csapatnev.accentureonepre.dto.Access;
import hu.csapatnev.accentureonepre.dto.Query;
import hu.csapatnev.accentureonepre.dto.Status;
import hu.csapatnev.accentureonepre.dto.StatusType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReboardingServiceTest {

    private static final long TEST_USER = 1L;
    private static final LocalDate TEST_DAY = LocalDate.of(2020, 7, 13);

    private ReboardingService reboardingService;

    private ReboardingDayData reboardingDayDataMock = mock(ReboardingDayData.class);
    private SeatAllocationService seatAllocationServiceMock = mock(SeatAllocationService.class);


    @Test
    void testConstructor() {
        Set<Point> seatAllocationMock = new HashSet<>();
        seatAllocationMock.add(new Point(0, 0));
        when(seatAllocationServiceMock.getSeatAllocation(anyInt())).thenReturn(seatAllocationMock);
        reboardingService = new ReboardingService(
                LocalDate.of(2020,6, 30),
                LocalDate.of(2020,7, 15),
                LocalDate.of(2020,7, 30),
                LocalDate.of(2020,8, 15),
                LocalDate.of(2020,8, 30),
                LocalDate.of(2020,9, 15),
                seatAllocationServiceMock
        );

        LocalDate testDay1 = LocalDate.of(2020, 6, 29);
        ReboardingDayData testResult1 = reboardingService.getReboardingDays().get(testDay1);
        Assertions.assertNull(testResult1);

        LocalDate testDay2 = LocalDate.of(2020, 6, 30);
        ReboardingDayData testResult2 = reboardingService.getReboardingDays().get(testDay2);
        Assertions.assertNotNull(testResult2);
        Assertions.assertTrue(testResult2.getAvailableSeats().contains(new Seat(0,0)));

        LocalDate testDay3 = LocalDate.of(2020, 8, 29);
        ReboardingDayData testResult3 = reboardingService.getReboardingDays().get(testDay3);
        Assertions.assertNotNull(testResult3);
        Assertions.assertTrue(testResult3.getAvailableSeats().contains(new Seat(0,0)));

        LocalDate testDay4 = LocalDate.of(2020, 9, 15);
        ReboardingDayData testResult4 = reboardingService.getReboardingDays().get(testDay4);
        Assertions.assertNull(testResult4);

        Assertions.assertEquals(77, reboardingService.getReboardingDays().size());
    }

    void injectReboardingDayDataMock() {
        reboardingService = new ReboardingService();
        Map<LocalDate, ReboardingDayData> reboardingDaysMock = new HashMap<>();
        reboardingDaysMock.put(TEST_DAY, reboardingDayDataMock);
        reboardingService.setReboardingDays(reboardingDaysMock);
    }

    @Test
    void testRegister() {
        injectReboardingDayDataMock();
        Query requestData = new Query(TEST_DAY, TEST_USER);
        Status responseStatus = new Status(StatusType.ACCEPTED, "Successfully registered", null);
        when(reboardingDayDataMock.register(requestData)).thenReturn(responseStatus);

        Status response = (Status) reboardingService.register(requestData);

        Assertions.assertEquals("accepted", response.getStatus());
        Assertions.assertEquals("Successfully registered", response.getMessage());
    }

    @Test
    void testGetStatus() {
        injectReboardingDayDataMock();
        Query requestData = new Query(TEST_DAY, TEST_USER);
        Status responseStatus = new Status(StatusType.ACCEPTED, "You are allowed to enter", null);
        when(reboardingDayDataMock.getStatus(requestData)).thenReturn(responseStatus);

        Status response = (Status) reboardingService.getStatus(requestData);

        Assertions.assertEquals("accepted", response.getStatus());
        Assertions.assertEquals("You are allowed to enter", response.getMessage());
    }

    @Test
    void testRemove() {
        injectReboardingDayDataMock();
        Query requestData = new Query(TEST_DAY, TEST_USER);
        Access responseStatus = new Access(true, "Successfully checked out");
        when(reboardingDayDataMock.exit(requestData)).thenReturn(responseStatus);

        Access response = (Access) reboardingService.remove(requestData);

        Assertions.assertTrue(response.isAccepted());
        Assertions.assertEquals("Successfully checked out", response.getMessage());
    }

    @Test
    void testEntry() {
        injectReboardingDayDataMock();
        Query requestData = new Query(TEST_DAY, TEST_USER);
        Access responseStatus = new Access(true, "Entry granted");
        when(reboardingDayDataMock.entry(requestData)).thenReturn(responseStatus);

        Access response = (Access) reboardingService.entry(requestData);

        Assertions.assertTrue(response.isAccepted());
        Assertions.assertEquals("Entry granted", response.getMessage());
    }

 }
