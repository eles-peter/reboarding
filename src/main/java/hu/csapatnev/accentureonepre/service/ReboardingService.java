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
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReboardingService {

    @Autowired
    private MessageSourceAccessor messageSourceAccessor;
    @Autowired
    private VipListService vipListService;
    @Autowired
    private SeatAllocationService seatAllocationService;

    @Value("${date.stepTo5}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate stepTo5;
    @Value("${date.stepTo4}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate stepTo4;
    @Value("${date.stepTo3}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate stepTo3;
    @Value("${date.stepTo2}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate stepTo2;
    @Value("${date.stepTo1}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate stepTo1;
    @Value("${date.endOfPeriod}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endOfPeriod;

    private Map<LocalDate, ReboardingDayData> reboardingDays;

    public ReboardingService() {
    }

    @PostConstruct
    public void init() {
        reboardingDays = createReboardingDays();
    }

    public ReboardingService(LocalDate stepTo5, LocalDate stepTo4, LocalDate stepTo3, LocalDate stepTo2, LocalDate stepTo1) {
        this.stepTo5 = stepTo5;
        this.stepTo4 = stepTo4;
        this.stepTo3 = stepTo3;
        this.stepTo2 = stepTo2;
        this.stepTo1 = stepTo1;
        reboardingDays = createReboardingDays();
    }

    private Map<LocalDate, ReboardingDayData> createReboardingDays() {
        Map<LocalDate, ReboardingDayData> reboardingDays = new HashMap<>();
        for (LocalDate day = stepTo5; day.isBefore(endOfPeriod); day = day.plusDays(1)) {
            int dailySocialDistance = getDailySocialDistance(day);
            Set<Point> seatAllocation = seatAllocationService.getSeatAllocation(dailySocialDistance);
            Set<Seat> seats = seatAllocation.stream().map(Seat::new).collect(Collectors.toSet());
            ReboardingDayData actualReboardingDayData = new ReboardingDayData(dailySocialDistance, seats, messageSourceAccessor, vipListService);
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

    private int getDailySocialDistance(LocalDate actualDay) {
        int dailySocialDistance;
        if (actualDay.isBefore(stepTo4)) {
            dailySocialDistance = 50;
        } else if (actualDay.isBefore(stepTo3)) {
            dailySocialDistance = 40;
        } else if (actualDay.isBefore(stepTo2)) {
            dailySocialDistance = 30;
        } else if (actualDay.isBefore(stepTo1)) {
            dailySocialDistance = 20;
        } else {
            dailySocialDistance = 10;
        }
        return dailySocialDistance;
    }

    public byte[] getLayout(LocalDate day) {
        ReboardingDayData actualDayData = reboardingDays.get(day);
        if (actualDayData == null) {
            return null;
        }
        return actualDayData.getImage();
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
