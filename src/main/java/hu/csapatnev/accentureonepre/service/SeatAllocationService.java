package hu.csapatnev.accentureonepre.service;

import org.springframework.stereotype.Service;

@Service
public class SeatAllocationService {

    private SeatService seatService;

    public SeatAllocationService(SeatService seatService) {
        this.seatService = seatService;
    }


}
