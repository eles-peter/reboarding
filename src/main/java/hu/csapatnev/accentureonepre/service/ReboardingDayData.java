package hu.csapatnev.accentureonepre.service;

import hu.csapatnev.accentureonepre.dto.Access;
import hu.csapatnev.accentureonepre.dto.Query;
import hu.csapatnev.accentureonepre.dto.Status;
import hu.csapatnev.accentureonepre.dto.StatusType;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class ReboardingDayData {

    private MessageSourceAccessor msg;
    private VipListService vipListService;

    private int dailySocialDistance;
    private Set<Seat> availableSeats;
    private CopyOnWriteArrayList<User> waitingList;

    public ReboardingDayData(int dailySocialDistance, MessageSourceAccessor msg, VipListService vipListService) {
        this.msg = msg;
        this.vipListService = vipListService;
        this.dailySocialDistance = dailySocialDistance;
        availableSeats = new HashSet<>();
        waitingList = new CopyOnWriteArrayList<>();
    }

    public ReboardingDayData(int dailySocialDistance, Set<Seat> seats, MessageSourceAccessor msg, VipListService vipListService) {
        this.msg = msg;
        this.vipListService = vipListService;
        this.dailySocialDistance = dailySocialDistance;
        this.availableSeats = seats;
        waitingList = new CopyOnWriteArrayList<>();
    }

    public Status register(Query requestData) {
        long userId = requestData.getUserId();
        Status status;

        if (vipListService.isContains(userId)) {
            //VIP user
            status = new Status(StatusType.ACCEPTED, msg.getMessage("register.vip", new Object[]{requestData.getDay()}));
        } else if (getRegisteredSeat(userId) != null) {
            //regisztrált már, van széke?
            status = new Status(StatusType.ACCEPTED, msg.getMessage("register.already", new Object[]{requestData.getDay()}));
        } else if (getWaitingListNumber(userId) > 0) {
            //regisztrált már, várólistán van?
            status = new Status(getWaitingListNumber(userId), msg.getMessage("register.already", new Object[]{requestData.getDay()}));
        } else {
            User user = new User(userId);
            boolean isSeatReservationSuccessful = reserveSeat(user);
            if (isSeatReservationSuccessful) {
                //sikerült beregisztrálni üres székre?
                status = new Status(StatusType.ACCEPTED, msg.getMessage("register.successful"));
            } else {
                //nincs már üres szék, várólistára rakni...
                waitingList.add(user);
                status = new Status(getWaitingListNumber(userId), msg.getMessage("register.successful"));
            }
        }
        return status;
    }

    private synchronized boolean reserveSeat(User user) { //ez van most szinkronizálva....
        boolean isSeatReservationSuccessful = false;
        Iterator<Seat> seatIterator = availableSeats.iterator();
        while (seatIterator.hasNext()) {
            Seat seat = seatIterator.next();
            if (seat.getUser() == null) {
                seat.setUser(user);
                isSeatReservationSuccessful = true;
                break;
            }
        }
        return isSeatReservationSuccessful;
    }

    private Seat getRegisteredSeat(long userId) {
        Seat result = null;
        Iterator<Seat> seatIterator = availableSeats.iterator();
        while (seatIterator.hasNext()) {
            Seat seat = seatIterator.next();
            if (userId == seat.getUser().getId()) {
                result = seat;
                break;
            }
        }
        return result;
    }

    private int getWaitingListNumber(long userId) {
        int waitingListNumber = 0;
        for (int i = 0; i < waitingList.size(); i++) {
            User user = waitingList.get(i);
            if (user.getId() == userId) {
                waitingListNumber = i + 1;
            }
        }
        return waitingListNumber;
    }

    public User findUserById(long userId) {
        User user = null;
        Iterator<Seat> seatIterator = availableSeats.iterator();
        while (seatIterator.hasNext()) {
            Seat seat = seatIterator.next();
            if (userId == seat.getUser().getId()) {
                user = seat.getUser();
                break;
            }
        }
        return user;
    }

    public Status getStatus(Query requestData) {
        long userId = requestData.getUserId();
        Status status;

        if (vipListService.isContains(userId)) {
            //VIP user
            status = new Status(StatusType.ACCEPTED, msg.getMessage("status.vip"));
        } else if (getRegisteredSeat(userId) != null) {
            //regisztrált már, van széke?
            User user = findUserById(userId);
            if (user.isCheckedIn()) {
                status = new Status(StatusType.INSIDE, msg.getMessage("status.checkedIn"));
            } else {
                status = new Status(StatusType.ACCEPTED, msg.getMessage("status.accepted"));
            }
        } else if (getWaitingListNumber(userId) > 0) {
            //regisztrált már, várólistán van?
            int waitingListNumber = getWaitingListNumber(userId);
            status = new Status(waitingListNumber, msg.getMessage("status.waitingList", new Object[]{waitingListNumber}));

        } else {
            //még nem regisztrált
            status = new Status(StatusType.NOT_SIGNED_UP, msg.getMessage("status.notSignedUp", new Object[]{requestData.getDay()}));
        }

        return status;
    }

    public Access exit(Query requestData) {
        Access access;
        long userId = requestData.getUserId();
        User user = findUserById(userId);

        // ha VIP, akkor ne csináljon semmit????

        if (user != null && user.isCheckedIn() && getRegisteredSeat(userId) != null) {
            access = new Access(true, msg.getMessage("exit.successful"));
            Seat seat = getRegisteredSeat(userId);
            seat.setUser(null);

            //Én ezeket hagynám a picsába....
        } else if (user == null) {
            access = new Access(false, msg.getMessage("exit.notSignedUp", new Object[]{requestData.getDay()}));
        } else {
            access = new Access(false, msg.getMessage("exit.notCheckedIn"));
        }

        return access;
    }

    public Access entry(Query requestData) {
        long userId = requestData.getUserId();
        Access access;

        if (vipListService.isContains(userId)) {
            //VIP user
            access= new Access(true, msg.getMessage("entry.vip"));
        } else if (getRegisteredSeat(userId) != null) {
            //regisztrált már, van széke?
            User user = findUserById(userId);
            if (user.isCheckedIn()) {
                access = new Access(false, msg.getMessage("entry.alreadyChecked"));
            } else {
                user.setCheckedIn(true);
                access = new Access(true, msg.getMessage("entry.granted"));
            }
        } else if (getWaitingListNumber(userId) > 0) {
            //regisztrált már, várólistán van?
            int waitingListNumber = getWaitingListNumber(userId);
            access = new Access(false, msg.getMessage("entry.waitingList", new Object[]{waitingListNumber}));

        } else {
            //még nem regisztrált
            access = new Access(false, msg.getMessage("entry.notSignedUp", new Object[]{requestData.getDay()}));
        }
        return access;
    }


}
