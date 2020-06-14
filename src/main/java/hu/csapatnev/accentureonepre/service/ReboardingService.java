package hu.csapatnev.accentureonepre.service;

import hu.csapatnev.accentureonepre.dto.Query;
import hu.csapatnev.accentureonepre.dto.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReboardingService {

    private int fullCapacity = 250;
    private LocalDate stepTo10 = LocalDate.of(2020, 6,30);
    private LocalDate stepTo20 = LocalDate.of(2020, 7,15);
    private LocalDate stepTo30 = LocalDate.of(2020, 7,30);
    private LocalDate stepTo50 = LocalDate.of(2020, 8,15);
    private LocalDate stepTo100 = LocalDate.of(2020, 8,30);

    private Map<LocalDate, ReboardingDayData> reboardingDays;

    public ReboardingService() {
    }

    public void setReboardingService() {
        reboardingDays = createReboardingDays();
    }

    public void setReboardingService(int fullCapacity, LocalDate stepTo10, LocalDate stepTo20, LocalDate stepTo30, LocalDate stepTo50, LocalDate stepTo100) {
        validOrThrow(fullCapacity, stepTo10, stepTo20, stepTo30, stepTo50, stepTo100);
        this.fullCapacity = fullCapacity;
        this.stepTo10 = stepTo10;
        this.stepTo20 = stepTo20;
        this.stepTo30 = stepTo30;
        this.stepTo50 = stepTo50;
        this.stepTo100 = stepTo100;
        reboardingDays = createReboardingDays();
    }

    private Map<LocalDate, ReboardingDayData> createReboardingDays() {
        Map<LocalDate, ReboardingDayData> reboardingDays = new HashMap<>();
        for (LocalDate day = stepTo10; day.isBefore(stepTo100); day = day.plusDays(1)) {
            int actualDayCapacity = getActualDayCapacity(day);
            ReboardingDayData actualReboardingDayData = new ReboardingDayData(actualDayCapacity);
            reboardingDays.put(day, actualReboardingDayData);
        }
        return reboardingDays;
    }

    public Payload register(Query requestData) {
        //TODO day validation
        ReboardingDayData actualDayData = reboardingDays.get(requestData.getDay());
        return actualDayData.register(requestData);
    }

    public Payload getStatus(Query requestData) {
        //TODO day validation
        ReboardingDayData actualDayData = reboardingDays.get(requestData.getDay());
        return actualDayData.getStatus(requestData);
    }

    public Payload remove(Query requestData) {
        //TODO itt akarunk valamit validálni???
        ReboardingDayData actualDayData = reboardingDays.get(requestData.getDay());
        return actualDayData.exit(requestData);
    }

    public Payload entry(Query requestData) {
        //TODO itt akarunk valamit validálni???
        ReboardingDayData actualDayData = reboardingDays.get(requestData.getDay());
        return actualDayData.entry(requestData);
    }

    private int getActualDayCapacity(LocalDate actualDay) {
        int actualDayCapacity;
        if (actualDay.isBefore(stepTo20)) {
            actualDayCapacity = (int)Math.round(fullCapacity * 0.1);
        } else if (actualDay.isBefore(stepTo30)) {
            actualDayCapacity = (int)Math.round(fullCapacity * 0.2);
        } else if (actualDay.isBefore(stepTo50)) {
            actualDayCapacity = (int) Math.round(fullCapacity * 0.3);
        } else {
            actualDayCapacity = (int)Math.round(fullCapacity * 0.5);
        }
        return actualDayCapacity;
    }

    private void validOrThrow(int fullCapacity, LocalDate stepTo10, LocalDate stepTo20, LocalDate stepTo30, LocalDate stepTo50, LocalDate stepTo100) {
        //TODO megírni a validálást, hogy  a napok jó sorrendben vannak-e...
    }

    public Map<LocalDate, ReboardingDayData> getReboardingDays() {
        return reboardingDays;
    }

    public void setReboardingDays(Map<LocalDate, ReboardingDayData> reboardingDays) {
        this.reboardingDays = reboardingDays;
    }

    @Override
    public String toString() {
        StringBuilder reboardingScheduleToString = new StringBuilder();
        for (Map.Entry<LocalDate, ReboardingDayData> entry : reboardingDays.entrySet()) {
            reboardingScheduleToString.append(entry.getKey()).append(" : ");
            reboardingScheduleToString.append(entry.getValue().toString());
            reboardingScheduleToString.append("\n");
        }
        return reboardingScheduleToString.toString();
    }
}
