package hu.csapatnev.accentureonepre.service;

import hu.csapatnev.accentureonepre.dto.Access;
import hu.csapatnev.accentureonepre.dto.Query;
import hu.csapatnev.accentureonepre.dto.Status;
import hu.csapatnev.accentureonepre.dto.StatusType;
import org.springframework.context.support.MessageSourceAccessor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
            status = new Status(StatusType.ACCEPTED, msg.getMessage("register.vip", new Object[]{requestData.getDay()}), null);
        } else if (findSeatByUserId(userId) != null) {
            //regisztrált már, van széke?
            status = new Status(StatusType.ACCEPTED, msg.getMessage("register.already", new Object[]{requestData.getDay()}), getLayoutUrl(requestData));
        } else if (getWaitingListNumber(userId) > 0) {
            //regisztrált már, várólistán van?
            status = new Status(getWaitingListNumber(userId), msg.getMessage("register.already", new Object[]{requestData.getDay()}));
        } else {
            User user = new User(userId);
            boolean isSeatReservationSuccessful = reserveSeat(user);
            if (isSeatReservationSuccessful) {
                //sikerült beregisztrálni üres székre?
                status = new Status(StatusType.ACCEPTED, msg.getMessage("register.successful"), getLayoutUrl(requestData));
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

    private Seat findSeatByUserId(Long userId) {
        Seat result = null;
        Iterator<Seat> seatIterator = availableSeats.iterator();
        while (seatIterator.hasNext()) {
            Seat seat = seatIterator.next();
            if (seat.getUser() != null && userId == seat.getUser().getId()) {
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
            if (seat.getUser() != null && userId == seat.getUser().getId()) {
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
            status = new Status(StatusType.ACCEPTED, msg.getMessage("status.vip"), null);
        } else if (findSeatByUserId(userId) != null) {
            //regisztrált már, van széke?
            User user = findUserById(userId);
            if (user.isCheckedIn()) {
                status = new Status(StatusType.INSIDE, msg.getMessage("status.checkedIn"), getLayoutUrl(requestData));
            } else {
                status = new Status(StatusType.ACCEPTED, msg.getMessage("status.accepted"), getLayoutUrl(requestData));
            }
        } else if (getWaitingListNumber(userId) > 0) {
            //regisztrált már, várólistán van?
            int waitingListNumber = getWaitingListNumber(userId);
            status = new Status(waitingListNumber, msg.getMessage("status.waitingList", new Object[]{waitingListNumber}));

        } else {
            //még nem regisztrált
            status = new Status(StatusType.NOT_SIGNED_UP, msg.getMessage("status.notSignedUp", new Object[]{requestData.getDay()}), null);
        }

        return status;
    }

    public String getLayoutUrl(Query query) {
        return "/api/reboarding/layout?day=" + query.getDay() + "&userId=" + query.getUserId();
    }

    public Access exit(Query requestData) {
        Access access;
        long userId = requestData.getUserId();
        User user = findUserById(userId);

        // ha VIP, akkor ne csináljon semmit????

        if (user != null && user.isCheckedIn() && findSeatByUserId(userId) != null) {
            access = new Access(true, msg.getMessage("exit.successful"));
            Seat seat = findSeatByUserId(userId);
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
        } else if (findSeatByUserId(userId) != null) {
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

    public byte[] getImage(Query requestData){
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        try {
            File file = new File(
                    getClass().getClassLoader().getResource("office_map.jpg").getFile()
            );
            BufferedImage bufferedImage = ImageIO.read(file);
            Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (requestData.getUserId() != null) {
                Status status = getStatus(requestData);

                if (!status.getStatus().equals(StatusType.NOT_SIGNED_UP.getDisplayName())) {
                    Seat seat = findSeatByUserId(requestData.getUserId());

                    graphics.setColor(Color.GREEN);
                    int radius = 7;
                    Shape circle = new Ellipse2D.Double(
                            seat.getCenter().getxCoord() - radius,
                            seat.getCenter().getyCoord() - radius,
                            2.0 * radius,
                            2.0 * radius
                    );
                    graphics.fill(circle);
                }

            } else {
                for (Seat seat : availableSeats) {
                    Color color = getColor(seat.getUser());
                    graphics.setColor(color);
                    int radius = 5;
                    Shape circle = new Ellipse2D.Double(seat.getCenter().getxCoord() - radius, seat.getCenter().getyCoord() - radius, 2.0 * radius, 2.0 * radius);
                    graphics.fill(circle);
                }

            }

            ImageIO.write(bufferedImage,"jpg",bao);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return bao.toByteArray();
    }

    private Color getColor(User user) {
        Color color;
        if (user == null){
            color = Color.GREEN;
        }else if (user.isCheckedIn()){
            color = Color.RED;
        }else {
            color = Color.YELLOW;
        }
        return color;
    }

    public int getDailySocialDistance() {
        return dailySocialDistance;
    }

    public Set<Seat> getAvailableSeats() {
        return availableSeats;
    }

    public CopyOnWriteArrayList<User> getWaitingList() {
        return waitingList;
    }
}
