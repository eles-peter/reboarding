package hu.csapatnev.accentureonepre.service;

import java.util.Objects;

public class Point {

    private int xCoord;
    private int yCoord;

    public Point() {
    }

    public Point(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public double distance(Point otherPoint) {
        int otherPointXCoord = otherPoint.getxCoord();
        int otherPointYCoord = otherPoint.getyCoord();
        return Math.sqrt(Math.pow((xCoord - otherPointXCoord), 2) + Math.pow((yCoord - otherPointYCoord), 2));
    }

    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return xCoord == point.xCoord &&
                yCoord == point.yCoord;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xCoord, yCoord);
    }

    @Override
    public String toString() {
        return  "P: xCoord=" + xCoord +
                ", yCoord=" + yCoord;
    }
}
