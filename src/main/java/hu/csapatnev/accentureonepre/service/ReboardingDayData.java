package hu.csapatnev.accentureonepre.service;

import hu.csapatnev.accentureonepre.dto.Access;
import hu.csapatnev.accentureonepre.dto.Query;
import hu.csapatnev.accentureonepre.dto.Status;
import hu.csapatnev.accentureonepre.dto.StatusType;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.concurrent.CopyOnWriteArrayList;

public class ReboardingDayData {

    private MessageSourceAccessor msg;

    private int dailyCapacity;
    private CopyOnWriteArrayList<User> signedUserList;

    public ReboardingDayData(int dailyCapacity, MessageSourceAccessor msg) {
        this.msg = msg;
        this.dailyCapacity = dailyCapacity;
        signedUserList = new CopyOnWriteArrayList<>();
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
            status = new Status(getWaitingListNumber(user), msg.getMessage("register.already", new Object[] {requestData.getDay()}));
        } else {
            status = new Status(StatusType.ACCEPTED, msg.getMessage("register.already", new Object[] {requestData.getDay()}));
        }

        if (registered) {
            status.setMessage(msg.getMessage("register.successful"));
        }

        return status;
    }

    public Status getStatus(Query requestData) {
        Status status;
        User user = findUserById(requestData.getUserId());
        int waitingListNumber = getWaitingListNumber(user);

        if (waitingListNumber == -1) {
            status = new Status(StatusType.NOT_SIGNED_UP, msg.getMessage("status.notSignedUp", new Object[] {requestData.getDay()}));
        } else if (waitingListNumber == 0) {
            if (user.isCheckedIn()) {
                status = new Status(StatusType.INSIDE, msg.getMessage("status.checkedIn"));
            } else {
                status = new Status(StatusType.ACCEPTED, msg.getMessage("status.accepted"));
            }
        } else {
            status = new Status(waitingListNumber, msg.getMessage("status.waitingList", new Object[] {waitingListNumber}));
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
            access = new Access(true, msg.getMessage("exit.successful"));
        } else if (user == null) {
            access = new Access(false, msg.getMessage("exit.notSignedUp", new Object[] {requestData.getDay()}));
        } else {
            access = new Access(false, msg.getMessage("exit.notCheckedIn"));
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
                    access = new Access(true, msg.getMessage("entry.granted"));
                } else {
                    access = new Access(false, msg.getMessage("entry.waitingList", new Object[] {getWaitingListNumber(user)}));
                }
            } else {
                access = new Access(false, msg.getMessage("entry.alreadyChecked"));
            }
        } else {
            access = new Access(false, msg.getMessage("entry.notSignedUp", new Object[] {requestData.getDay()}));
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
