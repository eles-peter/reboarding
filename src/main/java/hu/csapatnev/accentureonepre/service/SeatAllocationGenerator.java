package hu.csapatnev.accentureonepre.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SeatAllocationGenerator {

    private static SeatListService seatListService;

    public static void main(String[] args) {

        SeatAllocationGenerator seatAllocationGenerator = new SeatAllocationGenerator();

        List<List<Point>> centerPointListList = seatAllocationGenerator.getCenterPointOfSeatsOfOpenRooms();


//        List<Point> test1 = seatAllocationGenerator2.pointGeneratorIn_3_2Grid(5, 3);
//        List<Point> test2 = seatAllocationGenerator2.pointGeneratorIn_3_4Grid(16,5 );
//        System.out.println(test2);
//        List<Point> test3 = seatAllocationGenerator2.randomPointGenerator(80, 0, 50);
//        System.out.println(test3);
//        List<Point> test4 = seatAllocationGenerator2.randomPointGenerator(8000, 0, 90);




        List<Point> result = seatAllocationGenerator.allocationGenerator(centerPointListList, 50);
        System.out.println(result);
        System.out.println(result.size());
//        List<Point> result3 = seatAllocationGenerator2.allocationGenerator(test3, 5);
//        System.out.println(result3);
//        System.out.println(result3.size());

    }


    private List<Point> allocationGenerator(List<List<Point>> centerPointListList, int socDist) {
        long startTime = System.nanoTime();

        List<List<Point>> separatedPointListList = new ArrayList<>();

        for (List<Point> centerPoints : centerPointListList) {
            List<List<Point>> separatedPointSubListList = getSeparatedPointListList(centerPoints, socDist);
            separatedPointListList.addAll(separatedPointSubListList);
        }


        List<Point> result = new ArrayList<>();

        for (List<Point> separatedPointList : separatedPointListList) {
            List<Point> subResult = separatedPointListAllocationGenerator(separatedPointList, socDist);
            result.addAll(subResult);
        }

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("allocationGeneratorWithGraph running time: " + totalTime / 1000000 + "millisec");

        return result;
    }

    private List<Point> separatedPointListAllocationGenerator(List<Point> separatedPointList, int socDist) {

        Map<Point, List<Point>> godNeighborsGraph = createGodNeighborsGraph(separatedPointList, socDist);

        return getBiggestMaximalCliques(godNeighborsGraph);
    }



    public List<Point> getBiggestMaximalCliques(Map<Point, List<Point>> godNeighborsGraph) {

        List<Set<Point>> cliques = getAllMaximalCliques(godNeighborsGraph);

        int maximum = 0;
        Set<Point> biggestClique = new HashSet<>();
        for (Set<Point> clique : cliques) {
            if (maximum < clique.size()) {
                maximum = clique.size();
                biggestClique = clique;
            }
        }
        return new ArrayList<>(biggestClique);
    }

    public List<Set<Point>> getAllMaximalCliques(Map<Point, List<Point>> godNeighborsGraph) {

        List<Set<Point>> cliques = new ArrayList<>();
        List<Point> potentialClique = new ArrayList<>();
        List<Point> alreadyFound = new ArrayList<>();
        List<Point> candidates = new ArrayList<>(godNeighborsGraph.keySet());

        findCliques(potentialClique, candidates, alreadyFound, cliques, godNeighborsGraph);

        return cliques;
    }

    private void findCliques(List<Point> potentialClique, List<Point> candidates, List<Point> alreadyFound, List<Set<Point>> cliques, Map<Point, List<Point>> godNeighborsGraph) {

        List<Point> candidatesArray = new ArrayList<>(candidates);
        if (!isEnd(candidates, alreadyFound, godNeighborsGraph)) {
            for (Point candidate : candidatesArray) {
                List<Point> newCandidates = new ArrayList<>();
                List<Point> newAlreadyFound = new ArrayList<>();

                potentialClique.add(candidate);
                candidates.remove(candidate);

                for (Point new_candidate : candidates) {
                    if (godNeighborsGraph.get(candidate).contains(new_candidate)) {
                        newCandidates.add(new_candidate);
                    }
                }
                for (Point new_found : alreadyFound) {
                    if (godNeighborsGraph.get(candidate).contains(new_found)) {
                        newAlreadyFound.add(new_found);
                    }
                }
                if (newCandidates.isEmpty() && newAlreadyFound.isEmpty()) {

                    cliques.add(new HashSet<>(potentialClique));
                }
                else {
                    findCliques(
                            potentialClique,
                            newCandidates,
                            newAlreadyFound,
                            cliques,
                            godNeighborsGraph);
                }
                alreadyFound.add(candidate);
                potentialClique.remove(candidate);
            }
        }
    }

    private boolean isEnd(List<Point> candidates, List<Point> alreadyFound, Map<Point, List<Point>> godNeighborsGraph) {

        boolean isEnd = false;
        int edgeCounter;
        for (Point found : alreadyFound) {
            edgeCounter = 0;
            for (Point candidate : candidates) {
                if (godNeighborsGraph.get(found).contains(candidate)) {
                    edgeCounter++;
                }
            }
            if (edgeCounter == candidates.size()) {
                isEnd = true;
            }
        }
        return isEnd;
    }

    private Map<Point, List<Point>> createGodNeighborsGraph(List<Point> pointList, int socDist) {
        Map<Point, List<Point>> godNeighborsGraph = new HashMap<>();

        for (Point point : pointList) {
            List<Point> badNeighbors = pointList.stream()
                    .filter(otherPoint -> otherPoint.distance(point) >= socDist)
                    .collect(Collectors.toList());
            godNeighborsGraph.put(point, badNeighbors);
        }
        return godNeighborsGraph;
    }

    private List<List<Point>> getSeparatedPointListList(List<Point> centerPoints, int socDist) {

        Map<Point, List<Point>> badNeighborsGraph = createBadNeighborsGraph(centerPoints, socDist);

        List<List<Point>> separatedPointListList = new ArrayList<>();

        while (!centerPoints.isEmpty()) {
            Set<Point> separatedPointSet = new HashSet<>();
            Point startPoint = centerPoints.get(0);
            getSeparatedPointSetRecursively(centerPoints, badNeighborsGraph, startPoint, separatedPointSet);
            separatedPointListList.add(new ArrayList<>(separatedPointSet));
        }

        return separatedPointListList;
    }

    private void getSeparatedPointSetRecursively(
            List<Point> centerPoints, Map<Point, List<Point>> badNeighborsGraph, Point point, Set<Point> separatedPointSet) {

        separatedPointSet.add(point);
        centerPoints.remove(point);

        List<Point> badNeighbors = badNeighborsGraph.get(point);
        if (badNeighbors != null) {
            for (Point badNeighbor : badNeighbors) {
                if (centerPoints.contains(badNeighbor)) {
                    getSeparatedPointSetRecursively(centerPoints, badNeighborsGraph, badNeighbor, separatedPointSet);
                }
            }
        }
    }

    private Map<Point, List<Point>> createBadNeighborsGraph(List<Point> pointList, int socDist) {
        Map<Point, List<Point>> badNeighborsGraph = new HashMap<>();

        for (Point point : pointList) {
            List<Point> badNeighbors = pointList.stream()
                    .filter(otherPoint -> otherPoint.distance(point) < socDist)
                    .filter(otherPoint -> !otherPoint.equals(point))
                    .collect(Collectors.toList());
            badNeighborsGraph.put(point, badNeighbors);
        }
        return badNeighborsGraph;
    }


    // Generate points for testing

    private List<Point> randomPointGenerator(int numberOfSeats, int areaStartLimit, int areaEndLimit) {
        Set<Point> randomPointSet = new HashSet<>();
        Random random = new Random();
        for (int i = 0; i < numberOfSeats; i++) {
            int x = random.nextInt(areaEndLimit - areaStartLimit);
            int y = random.nextInt(areaEndLimit - areaStartLimit);
            randomPointSet.add(new Point(areaStartLimit + x, areaStartLimit + y));
        }
        return new ArrayList<>(randomPointSet);
    }

    private List<Point> pointGeneratorIn_3_4Grid(int numberOfColumn, int numberOfRows) {
        List<Point> pointList = new ArrayList<>();
        for (int i = 0; i < numberOfColumn; i++) {
            for (int j = 0; j < numberOfRows; j++) {
                int x = i * 3;
                int y = j * 4;
                pointList.add(new Point(x, y));
            }
        }
        return pointList;
    }

    private List<Point> pointGeneratorIn_3_2Grid(int numberOfColumn, int numberOfRows) {
        List<Point> pointList = new ArrayList<>();
        for (int i = 0; i < numberOfColumn; i++) {
            for (int j = 0; j < numberOfRows; j++) {
                int x = i * 3;
                int y = j * 2;
                pointList.add(new Point(x, y));
            }
        }
        return pointList;
    }


    //Seat Service

    private List<List<Point>> getCenterPointOfSeatsOfOpenRooms() {

        List<List<Point>> centerPointList = new ArrayList<>();

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
                    centerPointList.add(parseRoom(jsonRoom));
                }
            });

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return centerPointList;
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

}
