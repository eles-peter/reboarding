package hu.csapatnev.accentureonepre.controller;

import hu.csapatnev.accentureonepre.dto.Query;
import hu.csapatnev.accentureonepre.dto.Response;
import hu.csapatnev.accentureonepre.service.ReboardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Response> register(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "day", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @FutureOrPresent LocalDate day) {
        if (day == null) {
            day = LocalDate.now();
        }
        Query requestData = new Query(day, userId);
        Response response = new Response(requestData, reboardingService.register(requestData));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<Response> getStatus(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "day", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @FutureOrPresent LocalDate day) {
        if (day == null) {
            day = LocalDate.now();
        }
        Query requestData = new Query(day, userId);
        Response response = new Response(requestData, reboardingService.getStatus(requestData));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/entry/{userId}")
    public ResponseEntity<Response> isAccepted(@PathVariable Long userId) {
        Query requestData = new Query(LocalDate.now(), userId);
        Response response = new Response(requestData, reboardingService.entry(requestData));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/exit/{userId}")
    public ResponseEntity<Response> remove(@PathVariable Long userId) {
        //TODO ennek valami visszatérési értéket?
        Query requestData = new Query(LocalDate.now(), userId);
        Response response = new Response(requestData, reboardingService.remove(requestData));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
