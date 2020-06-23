package hu.csapatnev.accentureonepre.service;

import hu.csapatnev.accentureonepre.dto.Query;
import hu.csapatnev.accentureonepre.dto.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReboardingService {

    @Autowired
    private MessageSourceAccessor messageSourceAccessor;
    @Autowired
    private VipListService vipListService;

    @Value("${fullCapacity}")
    private int fullCapacity;

    @Value("${date.stepTo10}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate stepTo10;
    @Value("${date.stepTo20}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate stepTo20;
    @Value("${date.stepTo30}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate stepTo30;
    @Value("${date.stepTo50}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate stepTo50;
    @Value("${date.stepTo100}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate stepTo100;

    private Map<LocalDate, ReboardingDayData> reboardingDays;

    public ReboardingService() {
    }

    @PostConstruct
    public void init() {
        reboardingDays = createReboardingDays();
    }

    public ReboardingService(int fullCapacity, LocalDate stepTo10, LocalDate stepTo20, LocalDate stepTo30, LocalDate stepTo50, LocalDate stepTo100) {
        this.fullCapacity = fullCapacity;
        this.stepTo10 = stepTo10;
        this.stepTo20 = stepTo20;
        this.stepTo30 = stepTo30;
        this.stepTo50 = stepTo50;
        this.stepTo100 = stepTo100;
        reboardingDays = createReboardingDays();
    }

    //TODO átírni!!! Social distance-re!!!
    private Map<LocalDate, ReboardingDayData> createReboardingDays() {
        Map<LocalDate, ReboardingDayData> reboardingDays = new HashMap<>();
        for (LocalDate day = stepTo10; day.isBefore(stepTo100); day = day.plusDays(1)) {
            int actualDayCapacity = getActualDayCapacity(day);
            ReboardingDayData actualReboardingDayData = new ReboardingDayData(actualDayCapacity, messageSourceAccessor, vipListService);
            reboardingDays.put(day, actualReboardingDayData);
        }
        return reboardingDays;
    }

    public Payload register(Query requestData) {
        ReboardingDayData actualDayData = reboardingDays.get(requestData.getDay());
        if (actualDayData == null) {
            return null;
        }
        return actualDayData.register(requestData);
    }

    public Payload getStatus(Query requestData) {
        ReboardingDayData actualDayData = reboardingDays.get(requestData.getDay());
        if (actualDayData == null) {
            return null;
        }
        return actualDayData.getStatus(requestData);
    }

    public Payload remove(Query requestData) {
        ReboardingDayData actualDayData = reboardingDays.get(requestData.getDay());
        if (actualDayData == null) {
            return null;
        }
        return actualDayData.exit(requestData);
    }

    public Payload entry(Query requestData) {
        ReboardingDayData actualDayData = reboardingDays.get(requestData.getDay());
        if (actualDayData == null) {
            return null;
        }
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
