package hu.csapatnev.accentureonepre.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("access")
public class Access implements Payload {
    private boolean isAccepted;
    private String message;

    public Access(boolean isAccepted, String message) {
        this.isAccepted = isAccepted;
        this.message = message;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
