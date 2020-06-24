package hu.csapatnev.accentureonepre.service;

import java.util.*;
import java.util.stream.Collectors;

public class SeatAllocationGenerator {

    public static void main(String[] args) {

        SeatAllocationGenerator seatAllocationGenerator = new SeatAllocationGenerator();

        List<Point> test1 = seatAllocationGenerator.pointGeneratorIn_3_4Grid(200, 40);
        System.out.println(test1);
        System.out.println(test1.size());

//        List<Point> result1 = seatAllocationGenerator.bruteForceAllocationGenerator(test1, 5);
//        System.out.println(result1);
//        System.out.println(result1.size());

        List<Point> result1 = seatAllocationGenerator.allocationGeneratorWithGraph2(test1, 5);
        List<Point> result2 = seatAllocationGenerator.allocationGeneratorWithGraph(test1, 5);


    }


    //With Graph

    private List<Point> allocationGeneratorWithGraph2(List<Point> centerPoints, int socDist) {
        long startTime = System.nanoTime();
        Map<Point, List<Point>> badNeighborsGraph = createBadNeighborsGraph2(centerPoints, socDist);

        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("allocationGeneratorWithGraph2 running time: " + totalTime / 1000000 + "millisec");
        return null;
    }

    private Map<Point, List<Point>> createBadNeighborsGraph2(List<Point> centerPoints, int socDist) {
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

    private List<Point> allocationGeneratorWithGraph(List<Point> centerPoints, int socDist) {
        long startTime = System.nanoTime();
        Map<Point, List<Point>> badNeighborsGraph = createBadNeighborsGraph(centerPoints, socDist);

        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("allocationGeneratorWithGraph running time: " + totalTime / 1000000 + "millisec");
        return null;
    }

    private Map<Point, List<Point>> createBadNeighborsGraph(List<Point> centerPoints, int socDist) {
        Map<Point, List<Point>> badNeighborsGraph = new HashMap<>();
        
        List<Point> centerPointsOrderedByXCoord = centerPoints.stream()
                .sorted(Comparator.comparing(Point::getxCoord))
                .collect(Collectors.toList());
        List<Point> centerPointsOrderedByYCoord = centerPoints.stream()
                .sorted(Comparator.comparing(Point::getyCoord))
                .collect(Collectors.toList());

        for (Point point : centerPointsOrderedByXCoord) {
            List<Point> badNeighbors = getBadNeighbors(point, centerPointsOrderedByXCoord, centerPointsOrderedByYCoord, socDist);
            badNeighborsGraph.put(point, badNeighbors);
        }
        return badNeighborsGraph;
    }

    private List<Point> getBadNeighbors(Point point, List<Point> centerPointsOrderedByXCoord, List<Point> centerPointsOrderedByYCoord, int socDist) {

        List<Point> subListWithXLimit = createSubListWithXLimit(point, centerPointsOrderedByXCoord, socDist);
        List<Point> subListWithYLimit = createSubListWithYLimit(point, centerPointsOrderedByYCoord, socDist);

        return subListWithXLimit.stream()
                .filter(subListWithYLimit::contains)
                .filter(otherPoint -> otherPoint.distance(point) < socDist)
                .filter(otherPoint -> !otherPoint.equals(point))
                .collect(Collectors.toList());
    }

    private List<Point> createSubListWithXLimit(Point point, List<Point> centerPointsOrderedByXCoord, int socDist) {
        int indexXCoord = centerPointsOrderedByXCoord.indexOf(point);
        int indexXCoordMax = centerPointsOrderedByXCoord.size();
        int indexXCoordMin = 0;
        for (int i = indexXCoord; i < centerPointsOrderedByXCoord.size() - indexXCoord; i++) {
            if ((centerPointsOrderedByXCoord.get(i).getxCoord() - socDist) > point.getxCoord()) {
                indexXCoordMax = i;
                break;
            }
        }
        for (int i = indexXCoord; i >= 0 ; i--) {
            if ((centerPointsOrderedByXCoord.get(i).getxCoord() + socDist) < point.getxCoord()) {
                indexXCoordMin = i;
                break;
            }
        }
        return centerPointsOrderedByXCoord.subList(indexXCoordMin + 1, indexXCoordMax);
    }

    private List<Point> createSubListWithYLimit(Point point, List<Point> centerPointsOrderedByYCoord, int socDist) {
        int indexYCoord = centerPointsOrderedByYCoord.indexOf(point);
        int indexYCoordMax = centerPointsOrderedByYCoord.size();
        int indexYCoordMin = 0;
        for (int i = indexYCoord; i < centerPointsOrderedByYCoord.size() - indexYCoord; i++) {
            if ((centerPointsOrderedByYCoord.get(i).getyCoord() - socDist) > point.getyCoord()) {
                indexYCoordMax = i;
                break;
            }
        }
        for (int i = indexYCoord; i >= 0 ; i--) {
            if ((centerPointsOrderedByYCoord.get(i).getyCoord() + socDist) < point.getyCoord()) {
                indexYCoordMin = i;
                break;
            }
        }
        return centerPointsOrderedByYCoord.subList(indexYCoordMin + 1, indexYCoordMax);
    }

    //BrutForce Rekurzió

    private List<Point> bruteForceAllocationGenerator(List<Point> centerPoints, int socDist) {
        long startTime = System.nanoTime();

        List<Point> result = new ArrayList<>();
        List<Point> tempResult = new ArrayList<>();
        Point startPoint = centerPoints.get(0);
        //TODO megírni több startpontra!!!!
        bruteForceAllocationGeneratorRecursively(startPoint, centerPoints, tempResult, result, socDist);

        long endTime   = System.nanoTime();
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
            int x = random.nextInt(areaEndLimit-areaStartLimit);
            int y = random.nextInt(areaEndLimit-areaStartLimit);
            randomPointSet.add(new Point(areaStartLimit + x, areaStartLimit + y));
        }
        return new ArrayList<>(randomPointSet);
    }

    private List<Point> pointGeneratorIn_3_4Grid(int numberOfColumn, int numberOfRows) {
        List<Point> pointList = new ArrayList<>();
        for (int i = 0; i < numberOfColumn; i++) {
            for (int j = 0; j < numberOfRows; j++) {
                int x = i * 3 ;
                int y = j * 4;
                pointList.add(new Point(x, y));
            }
        }
        return pointList;
    }

}
