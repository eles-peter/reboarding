package hu.csapatnev.accentureonepre.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ReboardingDayData {

    private int dailyCapacity;
    private CopyOnWriteArrayList<User> signedUserList;

    public ReboardingDayData(int dailyCapacity) {
        this.dailyCapacity = dailyCapacity;
        signedUserList = new CopyOnWriteArrayList<>();
    }

    public ReboardingDayData(int dailyCapacity, List<User> signedUserList) {
        this.dailyCapacity = dailyCapacity;
        this.signedUserList = new CopyOnWriteArrayList<>(signedUserList);
    }

    public String register(long userId) {
        User user = findUserById(userId);

        if (user == null) {
            signedUserList.add(new User(userId));
        }

        return getStatus(userId);
    }

    // TODO: incloud checkedIn state
    public String getStatus(long userId) {
        String status = null;
        User user = findUserById(userId);

        if (user != null) {
            int index = signedUserList.indexOf(user) + 1;
            if (index <= dailyCapacity) {
                status = "accepted";
            } else {
                status = Integer.toString(index - dailyCapacity);
            }
        }

        return status;
    }

    public boolean exit(long userId) {
        User user = findUserById(userId);

        if (user != null && user.isCheckedIn()) {
            signedUserList.remove(user);
            return true;
        }

        return false;
    }

    public User findUserById(long userId) {
        User user = null;

        for (User singedUser : signedUserList) {
            if (singedUser.getId() == userId) {
                user = singedUser;
                break;
            }
        }

        return user;
    }

    public boolean entry(long userId) {
        User user = findUserById(userId);

        if (user != null && !user.isCheckedIn()) {
            int index = signedUserList.indexOf(user);
            boolean accepted = index + 1 <= dailyCapacity;
            if (accepted) {
                user.setCheckedIn(true);
            }
            return accepted;
        } else {
            return false;
        }
    }

    public int getDailyCapacity() {
        return dailyCapacity;
    }

    public CopyOnWriteArrayList<User> getSignedUserList() { //TODO ezt átgondolni, hogy sima ArrayList-et kapjunk vissza
        return signedUserList;
    }

    @Override
    public String toString() {
        return "dailyCapacity=" + dailyCapacity + ", signedUserList=" + signedUserList;
    }
}
