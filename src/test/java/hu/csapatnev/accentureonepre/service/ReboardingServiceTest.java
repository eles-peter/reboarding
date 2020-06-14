package hu.csapatnev.accentureonepre.service;

import hu.csapatnev.accentureonepre.dto.Query;
import hu.csapatnev.accentureonepre.dto.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


class ReboardingServiceTest {

    private ReboardingService reboardingService;

    @BeforeEach
    public void init() {
        reboardingService = new ReboardingService();
        reboardingService.setReboardingService(
                250,
                LocalDate.of(2020,6, 30),
                LocalDate.of(2020,7, 15),
                LocalDate.of(2020,7, 30),
                LocalDate.of(2020,8, 15),
                LocalDate.of(2020,8, 30)
        );
    }


    @Test
    void testConstructor() {
        //TODO some Assertions....
        Assertions.assertEquals(61, reboardingService.getReboardingDays().size());
    }

    @Test
    void testRegister_UnderDailyLimit() {
        long userId = 1L;
        LocalDate testDay = LocalDate.of(2020, 7, 13);
        Query requestData = new Query(testDay, userId);
        Status response = (Status) reboardingService.register(requestData);

        Assertions.assertEquals(1, reboardingService.getReboardingDays().get(testDay).getSignedUserList().size());
        Assertions.assertEquals("accepted", response.getStatus());
    }

    @Test
    void getStatus() {
    }

    @Test
    void remove() {
    }

    @Test
    void isAccepted() {
    }

    /*@Test
    void testAdd_MultipleUserSigningWithMultipleThreads() {
        List<Thread> threads = new ArrayList<>();
        LocalDate testDay = LocalDate.of(2020, 7, 1);
        for (int i = 0; i < 10; i++) {
            Thread worker = new Thread(new MultipleAdd(i, testDay));
            worker.start();
            threads.add(worker);
        }
        boolean isRunning;
        do {
            isRunning = false;
            for (Thread thread : threads) {
                if (thread.isAlive()) {
                    isRunning = true;
                }
            }
        } while (isRunning);
        int signedListLength = reboardingService.getReboardingDays().get(testDay).getSignedUserList().size();
        Assertions.assertEquals(1000, signedListLength );
    }

    private class MultipleAdd implements Runnable {
        private final int count;
        private final LocalDate testDay;

        MultipleAdd(int count, LocalDate testDay) {
            this.count = count;
            this.testDay = testDay;
        }

        @Override
        public void run() {
            for (int i = count * 100; i < (count + 1) * 100; i++) {
                reboardingService.register(testDay, i);
            }
        }
    }*/
}
