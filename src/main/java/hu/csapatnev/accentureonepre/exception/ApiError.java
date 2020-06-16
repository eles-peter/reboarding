package hu.csapatnev.accentureonepre.exception;

import org.springframework.http.HttpStatus;

public class ApiError {

    private String message;

    public ApiError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
