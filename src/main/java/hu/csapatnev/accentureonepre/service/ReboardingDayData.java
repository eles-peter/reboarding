package hu.csapatnev.accentureonepre.service;

import hu.csapatnev.accentureonepre.dto.Access;
import hu.csapatnev.accentureonepre.dto.Query;
import hu.csapatnev.accentureonepre.dto.Status;

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

    public Status register(Query requestData) {
        Status status;
        boolean registered = false;
        User user = findUserById(requestData.getUserId());

        if (user == null) {
            signedUserList.add(user = new User(requestData.getUserId()));
            registered = true;
        }

        int waitingListNumber = getWaitingListNumber(user);
        if (waitingListNumber > 0) {
            status = new Status("" + getWaitingListNumber(user), "You are already registered for " + requestData.getDay().toString());
        } else {
            status = new Status("accepted", "You are already registered for " + requestData.getDay().toString());
        }

        if (registered) {
            status.setMessage("Successfully registered");
        }

        return status;
    }

    public Status getStatus(Query requestData) {
        Status status;
        User user = findUserById(requestData.getUserId());
        int waitingListNumber = getWaitingListNumber(user);

        if (waitingListNumber == -1) {
            status = new Status("not_signed_up", "You are not signed up for " + requestData.getDay().toString());
        } else if (waitingListNumber == 0) {
            if (user.isCheckedIn()) {
                status = new Status("inside", "You are checked in");
            } else {
                status = new Status("accepted", "You are allowed to enter");
            }
        } else {
            status = new Status("" + waitingListNumber, "Your waiting-list number is: " + waitingListNumber);
        }
        return status;
    }

    private int getWaitingListNumber(User user) {
        int index = signedUserList.indexOf(user);
        if (index == -1) {
            return -1;
        }
        if (index + 1 <= dailyCapacity) {
            return 0;
        }
        return (index + 1 - dailyCapacity);
    }

    public Access exit(Query requestData) {
        Access access;
        User user = findUserById(requestData.getUserId());

        if (user != null && user.isCheckedIn()) {
            signedUserList.remove(user);
            access = new Access(true, "Successfully checked out");
        } else if (user == null) {
            access = new Access(false, "Can not exit, You are not signed up for " + requestData.getDay().toString());
        } else {
            access = new Access(false, "Can not exit, You are not checked in");
        }

        return access;
    }

    public Access entry(Query requestData) {
        User user = findUserById(requestData.getUserId());
        Access access;

        if (user != null) {
            if (!user.isCheckedIn()) {
                int index = signedUserList.indexOf(user);
                boolean accepted = index + 1 <= dailyCapacity;
                if (accepted) {
                    user.setCheckedIn(true);
                    access = new Access(true, "Entry granted");
                } else {
                    access = new Access(false, "You are in the waiting-list, your number is: " + getWaitingListNumber(user));
                }
            } else {
                access = new Access(false, "Can not enter, you are already checked in");
            }
        } else {
            access = new Access(false, "Can not enter, not signed up for " + requestData.getDay().toString());

        }
        return access;
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
