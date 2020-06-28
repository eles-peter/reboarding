package hu.csapatnev.accentureonepre.controller;

import hu.csapatnev.accentureonepre.dto.Query;
import hu.csapatnev.accentureonepre.dto.Response;
import hu.csapatnev.accentureonepre.exception.ApiError;
import hu.csapatnev.accentureonepre.service.ReboardingService;
import hu.csapatnev.accentureonepre.validator.BeforeStepTo100;
import hu.csapatnev.accentureonepre.validator.NotBeforeStepTo10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

@Validated
@RestController
@RequestMapping("/api/reboarding")
public class ReboardingController {

    private static final String TODAY_OUT_OF_TRACKED = "today out of tracked day";

    private final ReboardingService reboardingService;

    @Autowired
    public ReboardingController(ReboardingService reboardingService) {
        this.reboardingService = reboardingService;
    }

    @PostMapping("/register")
    public ResponseEntity register(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "day", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @FutureOrPresent @NotBeforeStepTo10 @BeforeStepTo100 LocalDate day) {
        if (day == null) {
            day = LocalDate.now();
        }
        Query requestData = new Query(day, userId);
        Response response = new Response(requestData, reboardingService.register(requestData));
        return new ResponseEntity<>(response, HttpStatus.OK);
//        return response.getPayload() == null ?
//                new ResponseEntity<>(new ApiError(TODAY_OUT_OF_TRACKED), HttpStatus.BAD_REQUEST) :
//                new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity getStatus(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "day", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @FutureOrPresent @NotBeforeStepTo10 @BeforeStepTo100 LocalDate day) {
        if (day == null) {
            day = LocalDate.now();
        }
        Query requestData = new Query(day, userId);
        Response response = new Response(requestData, reboardingService.getStatus(requestData));
        return response.getPayload() == null ?
                new ResponseEntity<>(new ApiError(TODAY_OUT_OF_TRACKED), HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/entry/{userId}")
    public ResponseEntity isAccepted(@PathVariable Long userId) {
        @BeforeStepTo100 @NotBeforeStepTo10 LocalDate today = LocalDate.now();
        Query requestData = new Query(today, userId);
        Response response = new Response(requestData, reboardingService.entry(requestData));
        return response.getPayload() == null ?
                new ResponseEntity<>(new ApiError(TODAY_OUT_OF_TRACKED), HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/exit/{userId}")
    public ResponseEntity remove(@PathVariable Long userId) {
        @BeforeStepTo100 @NotBeforeStepTo10 LocalDate today = LocalDate.now();
        Query requestData = new Query(today, userId);
        Response response = new Response(requestData, reboardingService.remove(requestData));
        return response.getPayload() == null ?
                new ResponseEntity<>(new ApiError(TODAY_OUT_OF_TRACKED), HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/layout", produces = "image/jpg")
    public @ResponseBody
    byte[] getLayout(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "day", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @FutureOrPresent @NotBeforeStepTo10 @BeforeStepTo100 LocalDate day) {
        if (day == null) {
            day = LocalDate.now();
        }

        Query requestData = new Query(day, userId);

        byte[] image = reboardingService.getLayout(requestData);

        return image;
    }

}
