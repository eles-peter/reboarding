package hu.csapatnev.accentureonepre.dto;


import java.time.LocalDate;

public class Query {

    private LocalDate day;
    private Long userId;

    public Query(LocalDate day, Long userId) {
        this.day = day;
        this.userId = userId;
    }

    public LocalDate getDay() {
        return day;
    }

    public Long getUserId() {
        return userId;
    }
}
