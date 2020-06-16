package hu.csapatnev.accentureonepre.service;

import hu.csapatnev.accentureonepre.dto.Query;
import hu.csapatnev.accentureonepre.dto.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;

class ReboardingDayDataTest {

    private ReboardingDayData reboardingDayData;
    private MessageSourceAccessor messageSourceAccessorMock = mock(MessageSourceAccessor.class);
    private static final int DAILY_CAPACITY = 10;

    @BeforeEach
    public void init() {
       reboardingDayData = new ReboardingDayData(DAILY_CAPACITY, messageSourceAccessorMock);
    }

    @Test
    void testAdd_UnderDailyLimit() {
        Status response = reboardingDayData.register(new Query(LocalDate.now(), 1L));
        Assertions.assertEquals(1, reboardingDayData.getSignedUserList().size());
        Assertions.assertEquals("accepted", response.getStatus());
    }

    @Test
    void testAdd_OverDailyLimit() {
        for (long i = 2; i < DAILY_CAPACITY+2; i++) {
            reboardingDayData.register(new Query(LocalDate.now(), i));
        }
        Status response = reboardingDayData.register(new Query(LocalDate.now(), 1L));
        Assertions.assertEquals(DAILY_CAPACITY + 1, reboardingDayData.getSignedUserList().size());
        Assertions.assertEquals("1", response.getStatus());
    }

    @Test
    void testAdd_DuplicatedUser() {
        reboardingDayData.register(new Query(LocalDate.now(), 1L));
        Status response = reboardingDayData.register(new Query(LocalDate.now(), 1L));
        Assertions.assertEquals(1, reboardingDayData.getSignedUserList().size());
        Assertions.assertEquals("accepted", response.getStatus());
    }

    @Test
    void testGetStatus_UnderDailyLimit() {
        for (long i = 1; i <= DAILY_CAPACITY; i++) {
            reboardingDayData.register(new Query(LocalDate.now(), i));
        }
        Query requestData = new Query(LocalDate.now(), DAILY_CAPACITY);
        Assertions.assertEquals("accepted", reboardingDayData.getStatus(requestData).getStatus());
    }

    @Test
    void testGetStatus_OverDailyLimit() {
        for (long i = 1; i <= DAILY_CAPACITY + 1; i++) {
            reboardingDayData.register(new Query(LocalDate.now(), i));
        }
        Query requestData = new Query(LocalDate.now(), DAILY_CAPACITY + 1);
        Assertions.assertEquals("1", reboardingDayData.getStatus(requestData).getStatus());
    }

    @Test
    void testGetStatus_NotInList() {
        Assertions.assertEquals("not_signed_up", reboardingDayData.getStatus(new Query(LocalDate.now(), 1L)).getStatus());
    }

    @Test
    void testGetStatus_CheckedIn() {
        Query requestData = new Query(LocalDate.now(), 1L);
        reboardingDayData.register(requestData);
        reboardingDayData.findUserById(1L).setCheckedIn(true);
        Assertions.assertEquals("inside", reboardingDayData.getStatus(requestData).getStatus());
    }

    @Test
    void testExit_CheckedIn() {
        Query requestData = new Query(LocalDate.now(), 1L);

        reboardingDayData.register(requestData);
        reboardingDayData.findUserById(1L).setCheckedIn(true);
        Assertions.assertTrue(reboardingDayData.exit(requestData).isAccepted());
        Assertions.assertEquals(0, reboardingDayData.getSignedUserList().size());
    }

    @Test
    void testExit_NotCheckedIn() {
        Query requestData = new Query(LocalDate.now(), 1L);

        reboardingDayData.register(requestData);
        Assertions.assertFalse(reboardingDayData.exit(requestData).isAccepted());
        Assertions.assertEquals(1, reboardingDayData.getSignedUserList().size());
    }

    @Test
    void testExit_NotInList() {
        Assertions.assertFalse(reboardingDayData.exit(new Query(LocalDate.now(), 1L)).isAccepted());
    }

    @Test
    void testEntry_NotCheckedIn() {
        Query requestData = new Query(LocalDate.now(), 1L);
        reboardingDayData.register(requestData);
        Assertions.assertTrue(reboardingDayData.entry(requestData).isAccepted());
        Assertions.assertTrue(reboardingDayData.findUserById(1L).isCheckedIn());
    }

    @Test
    void testEntry_CheckedIn() {
        Query requestData = new Query(LocalDate.now(), 1L);
        reboardingDayData.register(requestData);
        reboardingDayData.findUserById(1L).setCheckedIn(true);
        Assertions.assertFalse(reboardingDayData.entry(requestData).isAccepted());
    }

    @Test
    void testEntry_OverDailyLimit() {
        for (long i = 1; i <= DAILY_CAPACITY; i++) {
            reboardingDayData.register(new Query(LocalDate.now(), i));
        }
        Query requestData = new Query(LocalDate.now(), DAILY_CAPACITY + 1);
        reboardingDayData.register(requestData);
        Assertions.assertFalse(reboardingDayData.entry(requestData).isAccepted());
    }

    @Test
    void testEntry_NotInlist() {
        Assertions.assertFalse(reboardingDayData.entry(new Query(LocalDate.now(), 1L)).isAccepted());
    }

    @Test
    void isAccepted() {
    }
}
