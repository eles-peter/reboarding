package hu.csapatnev.accentureonepre.service;

import java.util.Objects;

public class Seat {

    private Point center;
    private User user;

    public Seat(int xCoord, int yCoord) {
        this.center = new Point(xCoord, yCoord);
        this.user = null;
    }

    public Seat(Point center) {
        this.center = center;
        this.user = null;
    }

    public boolean isSocialDistance(Seat otherSeat, int socialDistance) {
        double distance = distance(otherSeat);
        return socialDistance <= distance;
    }

    public double distance(Seat otherSeat) {
        return center.distance(otherSeat.getCenter());
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return center.equals(seat.center);
    }

    @Override
    public int hashCode() {
        return Objects.hash(center);
    }

    @Override
    public String toString() {
        return "Seat{" +
                "center=" + center +
                ", user=" + user +
                '}';
    }
}
