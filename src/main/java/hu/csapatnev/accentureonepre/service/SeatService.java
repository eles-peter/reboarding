package hu.csapatnev.accentureonepre.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeatService {

    private List<List<Seat>> seatList = new ArrayList<>();

    public SeatService() {
    }

    @PostConstruct
    public void init() {
        getSeatsOfOpenRooms();
    }

    private void getSeatsOfOpenRooms() {
        File file = new File(
                getClass().getClassLoader().getResource("seat-coordinates.json").getFile()
        );

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(bufferedReader);

            JSONArray rooms = (JSONArray) obj;

            rooms.forEach((room) -> {
                JSONObject jsonRoom = (JSONObject) room;
                if ((boolean) jsonRoom.get("isOpen")) {
                    seatList.add(parseRoom(jsonRoom));
                }
            });

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private List<Seat> parseRoom(JSONObject room) {
        JSONArray seats = (JSONArray) room.get("seats");
        List<Seat> seatList = new ArrayList<>();
        seats.forEach((seat) -> {
            int x = Integer.parseInt(((JSONObject) seat).get("x").toString());
            int y = Integer.parseInt(((JSONObject) seat).get("y").toString());
            seatList.add(new Seat(x, y));
        });
        return seatList;
    }
}
