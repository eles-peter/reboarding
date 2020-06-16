package hu.csapatnev.accentureonepre.dto;

public enum StatusType {
    ACCEPTED("accepted"),
    NOT_SIGNED_UP("not_signed_up"),
    INSIDE("inside"),
    ;

    private String displayName;

    StatusType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
