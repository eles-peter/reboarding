package hu.csapatnev.accentureonepre.service;

import java.util.*;
import java.util.stream.Collectors;

public class SeatAllocationGenerator {

    public static void main(String[] args) {

        SeatAllocationGenerator seatAllocationGenerator = new SeatAllocationGenerator();

        List<Point> test1 = seatAllocationGenerator.pointGeneratorIn_3_2Grid(5, 3);
        System.out.println(test1);
        List<Point> test2 = seatAllocationGenerator.pointGeneratorIn_3_4Grid(3, 3);
//        List<Point> test2 = seatAllocationGenerator.randomPointGenerator(8000, 0, 90);


//        List<Point> result1 = seatAllocationGenerator.bruteForceAllocationGenerator(test1, 5);
//        System.out.println(result1);
//        System.out.println(result1.size());


        List<Point> result2 = seatAllocationGenerator.allocationGeneratorWithGraph(test1, 5);
        System.out.println(result2);
        System.out.println(result2.size());

//        List<Point> result4 = seatAllocationGenerator.allocationGeneratorWithGraph(test2, 5);


    }


    //With Graph

    private List<Point> allocationGeneratorWithGraph(List<Point> centerPoints, int socDist) {
        long startTime = System.nanoTime();

        Map<Point, List<Point>> badNeighborsGraph = createBadNeighborsGraph(centerPoints, socDist);

        List<Point> result = new ArrayList<>();
        List<Point> tempResult = new ArrayList<>();
        Point startPoint = centerPoints.get(0);

        allocationGeneratorWithGraphRecursively(startPoint, badNeighborsGraph, tempResult, result, socDist);

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("allocationGeneratorWithGraph running time: " + totalTime / 1000000 + "millisec");
        System.out.println(" a maradék: " + badNeighborsGraph);
        return result;
    }

    private void allocationGeneratorWithGraphRecursively(Point point, Map<Point, List<Point>> badNeighborsGraph, List<Point> tempResult, List<Point> result, int socDist) {
        tempResult.add(point);
        if (tempResult.size() > result.size()) {
            result.clear();
            result.addAll(tempResult);
        }

        List<Point> badNeighbors = badNeighborsGraph.get(point);
        if (badNeighbors == null) {
            return;
        }

        Set<Point> closeGoodNeighbors = new HashSet<>();
        for (Point badNeighbor : badNeighbors) {
            List<Point> badNeighborsOfBadNeighbor = badNeighborsGraph.get(badNeighbor);
            if (badNeighborsOfBadNeighbor != null) {
                for (Point badNeighborOfBadNeighbor : badNeighborsOfBadNeighbor) {
                    if (point.distance(badNeighborOfBadNeighbor) >= socDist) {
                        closeGoodNeighbors.add(badNeighborOfBadNeighbor);
                    }
                }
            }
        }

        Map<Point, List<Point>> removedGraphItems = new HashMap<>();
        badNeighborsGraph.remove(point);
        removedGraphItems.put(point, badNeighbors);
        for (Point badNeighbor : badNeighbors) {
            List<Point> badNeighborsOfBadNeighbor = badNeighborsGraph.remove(badNeighbor);
            if (badNeighborsOfBadNeighbor != null) {
                removedGraphItems.put(badNeighbor, badNeighborsOfBadNeighbor);
            }
        }



        for (Point closeGoodNeighbor : closeGoodNeighbors) {
            allocationGeneratorWithGraphRecursively(closeGoodNeighbor, badNeighborsGraph, tempResult, result, socDist);
        }

//        badNeighborsGraph.putAll(removedGraphItems);
        tempResult.remove(point);

    }


    private Map<Point, List<Point>> createBadNeighborsGraph(List<Point> centerPoints, int socDist) {
        Map<Point, List<Point>> badNeighborsGraph = new HashMap<>();

        for (Point point : centerPoints) {
            List<Point> badNeighbors = centerPoints.stream()
                    .filter(otherPoint -> otherPoint.distance(point) < socDist)
                    .filter(otherPoint -> !otherPoint.equals(point))
                    .collect(Collectors.toList());
            badNeighborsGraph.put(point, badNeighbors);
        }
        return badNeighborsGraph;
    }


    //BrutForce Rekurzió

    private List<Point> bruteForceAllocationGenerator(List<Point> centerPoints, int socDist) {
        long startTime = System.nanoTime();

        List<Point> result = new ArrayList<>();
        List<Point> tempResult = new ArrayList<>();
        Point startPoint = centerPoints.get(0);
        //TODO megírni több startpontra!!!!
        bruteForceAllocationGeneratorRecursively(startPoint, centerPoints, tempResult, result, socDist);

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("bruteForceAllocationGenerator running time: " + totalTime / 1000000 + "millisec");

        return result;
    }

    private void bruteForceAllocationGeneratorRecursively(Point point, List<Point> centerPoints, List<Point> tempResult, List<Point> result, int socDist) {
        tempResult.add(point);
        if (tempResult.size() > result.size()) {
            result.clear();
            result.addAll(tempResult);
        }

        TreeMap<Integer, Point> notSafePoints = notSafePointsFromList(point, centerPoints, socDist);
        safetyRemove(centerPoints, notSafePoints);

        for (int i = 0; i < centerPoints.size(); i++) {
            Point currentPoint = centerPoints.get(i);
            bruteForceAllocationGeneratorRecursively(currentPoint, centerPoints, tempResult, result, socDist);
        }

        safetyRevertRemove(centerPoints, notSafePoints);
        tempResult.remove(point);

    }

    private void safetyRevertRemove(List<Point> centerPoints, TreeMap<Integer, Point> notSafePoints) {

        for (Map.Entry<Integer, Point> entry : notSafePoints.entrySet()) {
            int indexOfNotSafetyPoint = entry.getKey();
            Point notSafetyPoint = entry.getValue();
            centerPoints.add(indexOfNotSafetyPoint, notSafetyPoint);
        }
    }

    private void safetyRemove(List<Point> centerPoints, TreeMap<Integer, Point> notSafePoints) {

        for (Integer descendingKey : notSafePoints.descendingKeySet()) {
            int indexOfNotSafetyPoint = descendingKey;
            centerPoints.remove(indexOfNotSafetyPoint);
        }
    }

    private TreeMap<Integer, Point> notSafePointsFromList(Point currentPoint, List<Point> centerPoints, int socDist) {
        TreeMap<Integer, Point> notSafePoints = new TreeMap<>();
        for (int i = 0; i < centerPoints.size(); i++) {
            Point checkedPoint = centerPoints.get(i);
            if (currentPoint.distance(checkedPoint) < socDist) {
                notSafePoints.put(i, checkedPoint);
            }
        }
        return notSafePoints;
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

}
