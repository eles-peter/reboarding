package hu.csapatnev.accentureonepre.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class ReboardingDayDataTest {

    private ReboardingDayData reboardingDayData;
    private static final int DAILY_CAPACITY = 10;

    @BeforeEach
    public void init() {
       reboardingDayData = new ReboardingDayData(DAILY_CAPACITY);
    }

    @Test
    void testAdd_UnderDailyLimit() {
        String response = reboardingDayData.register(1L);
        Assertions.assertEquals(1, reboardingDayData.getSignedUserList().size());
        Assertions.assertEquals("accepted", response);
    }

    @Test
    void testAdd_OverDailyLimit() {
        for (long i = 2; i < DAILY_CAPACITY+2; i++) {
            reboardingDayData.register(i);
        }
        String response = reboardingDayData.register(1L);
        Assertions.assertEquals(DAILY_CAPACITY + 1, reboardingDayData.getSignedUserList().size());
        Assertions.assertEquals("1", response);
    }

    @Test
    void testAdd_DuplicatedUser() {
        reboardingDayData.register(1L);
        String response = reboardingDayData.register(1L);
        Assertions.assertEquals(1, reboardingDayData.getSignedUserList().size());
        Assertions.assertEquals("accepted", response);
    }

    @Test
    void testGetStatus_UnderDailyLimit() {
        for (long i = 1; i <= DAILY_CAPACITY; i++) {
            reboardingDayData.register(i);
        }
        Assertions.assertEquals("accepted", reboardingDayData.getStatus(DAILY_CAPACITY));
    }

    @Test
    void testGetStatus_OverDailyLimit() {
        for (long i = 1; i <= DAILY_CAPACITY + 1; i++) {
            reboardingDayData.register(i);
        }
        Assertions.assertEquals("1", reboardingDayData.getStatus(DAILY_CAPACITY + 1));
    }

    @Test
    void testGetStatus_NotInList() {
        Assertions.assertNull(reboardingDayData.getStatus(1L));
    }

    @Test
    void isAccepted() {
    }
}
