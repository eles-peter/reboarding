package hu.csapatnev.accentureonepre.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RoomParser {
    // TODO May not work in prod env
    private static final Path path = Path.of("src\\main\\resources\\seat-coordinates.json");

    public static void main(String[] args) {
        System.out.println(getCoordinatesOfOpenRooms());
    }

    public static List<Coordinates> getCoordinatesOfOpenRooms() {
        List<Coordinates> coordinates = new ArrayList<>();
        try {
            BufferedReader bufferedReader = Files.newBufferedReader(path);
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(bufferedReader);

            JSONArray rooms = (JSONArray) obj;

            rooms.forEach((room) -> {
                coordinates.addAll(parseRoom((JSONObject) room));
            });


        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return coordinates;
    }

    private static List<Coordinates> parseRoom(JSONObject room) {
        JSONArray seats = (JSONArray) room.get("seats");
        List<Coordinates> coordinates = new ArrayList<>();
        seats.forEach((seat) -> {
            int x = Integer.parseInt(((JSONObject) seat).get("x").toString());
            int y = Integer.parseInt(((JSONObject) seat).get("y").toString());
            coordinates.add(new Coordinates(x, y));
        });
        return coordinates;
    }
}

class Coordinates {
    public int x;
    public int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
