package hu.csapatnev.accentureonepre.service;

import java.util.ArrayList;
import java.util.List;

public class Block {

    private List<Point> vertices;

    public Block() {
        this.vertices = new ArrayList<>();
    }

    public Block(List<Point> vertices) {
        this.vertices = vertices;
    }

    public void addPoint(Point vertex) {
        this.vertices.add(vertex);
    }


}
