package hu.csapatnev.accentureonepre.service;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
