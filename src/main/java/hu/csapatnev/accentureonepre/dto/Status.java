package hu.csapatnev.accentureonepre.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("status")
public class Status implements Payload {
    private String status;
    private String message;

    public Status(StatusType status, String message) {
        this.status = status.getDisplayName();
        this.message = message;
    }

    public Status(int waitingListNumber, String message) {
        this.status = Integer.toString(waitingListNumber);
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
