package hu.csapatnev.accentureonepre.service;

public class User {
    private long id;
    private boolean checkedIn = false;

    public User(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }
}
