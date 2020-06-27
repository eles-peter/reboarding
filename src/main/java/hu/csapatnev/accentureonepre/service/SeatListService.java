package hu.csapatnev.accentureonepre.service;

import hu.csapatnev.accentureonepre.AccentureOnePreApplication;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeatListService {

    private List<List<Point>> centerPointListList = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(AccentureOnePreApplication.class);

    public SeatListService() {
    }

    @PostConstruct
    public void init() {
        getCenterPointOfSeatsOfOpenRooms();

    }

    private void getCenterPointOfSeatsOfOpenRooms() {
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
                    centerPointListList.add(parseRoom(jsonRoom));
                }
            });

            logger.info("coordinates of seats loaded");

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private List<Point> parseRoom(JSONObject room) {
        JSONArray seats = (JSONArray) room.get("seats");
        List<Point> pointList = new ArrayList<>();
        seats.forEach((seat) -> {
            int x = Integer.parseInt(((JSONObject) seat).get("x").toString());
            int y = Integer.parseInt(((JSONObject) seat).get("y").toString());
            pointList.add(new Point(x, y));
        });
        return pointList;
    }

    public List<List<Point>> getCopyOfCenterPointListList() {
        List<List<Point>> CopyOfCenterPointListList = new ArrayList<>();
        for (List<Point>centerPointList : this.centerPointListList) {
            CopyOfCenterPointListList.add(new ArrayList<>(centerPointList));
        }
        return CopyOfCenterPointListList;
    }
}
