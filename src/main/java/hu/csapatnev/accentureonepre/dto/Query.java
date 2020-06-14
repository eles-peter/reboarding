package hu.csapatnev.accentureonepre.dto;


import java.time.LocalDate;

public class Query {
    private LocalDate day;
    private long userId;

    public Query(LocalDate day, long userId) {
        this.day = day;
        this.userId = userId;
    }

    public LocalDate getDay() {
        return day;
    }

    public long getUserId() {
        return userId;
    }
}
