package hu.csapatnev.accentureonepre.controller;

import hu.csapatnev.accentureonepre.service.ReboardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/reboarding")
public class ReboardingController {

    private final ReboardingService reboardingService;

    @Autowired
    public ReboardingController(ReboardingService reboardingService) {
        this.reboardingService = reboardingService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "day", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @FutureOrPresent LocalDate day) {
        if (day == null) {
            day = LocalDate.now();
        }
        String status = reboardingService.register(day, userId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "day", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @FutureOrPresent LocalDate day) {
        if (day == null) {
            day = LocalDate.now();
        }
        String status = reboardingService.getStatus(day, userId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @GetMapping("/entry/{userId}")
    public ResponseEntity<Boolean> isAccepted(@PathVariable Long userId) {
        Boolean isAccepted = reboardingService.entry(LocalDate.now(), userId);
        return new ResponseEntity<>(isAccepted, HttpStatus.OK);
    }

    @GetMapping("/exit/{userId}")
    public ResponseEntity<Void> remove(@PathVariable Long userId) {
        //TODO ennek valami visszatérési értéket?
        reboardingService.remove(LocalDate.now(), userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
