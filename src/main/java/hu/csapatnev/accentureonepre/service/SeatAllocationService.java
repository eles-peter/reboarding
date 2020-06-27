package hu.csapatnev.accentureonepre.service;

import hu.csapatnev.accentureonepre.AccentureOnePreApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class SeatAllocationService {

    private SeatListService seatListService;
    private static final Logger logger = LoggerFactory.getLogger(AccentureOnePreApplication.class);

    private Map<Integer, Set<Point>> seatAllocationList;

    public SeatAllocationService(SeatListService seatListService) {
        this.seatListService = seatListService;
        this.seatAllocationList = new HashMap<>();
    }

    public Set<Point> getSeatAllocation(int socDist) {
        Set<Point> seatAllocation = seatAllocationList.get(socDist);
        if (seatAllocation == null) {
            List<List<Point>> centerPointListList = seatListService.getCopyOfCenterPointListList();
            seatAllocation = createSeatAllocation(centerPointListList, socDist);
            seatAllocationList.put(socDist, seatAllocation);
        }
        return seatAllocation;
    }

    private Set<Point> createSeatAllocation(List<List<Point>> centerPointListList, int socDist) {
        long startTime = System.nanoTime();

        List<List<Point>> separatedPointListList = new ArrayList<>();

        for (List<Point> centerPoints : centerPointListList) {
            List<List<Point>> separatedPointSubListList = getSeparatedPointListList(centerPoints, socDist);
            separatedPointListList.addAll(separatedPointSubListList);
        }

        Set<Point> result = new HashSet<>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future<List<Point>>> subResultFutureList = new ArrayList<>();

        for (List<Point> separatedPointList : separatedPointListList) {
            Future<List<Point>> subResult = executorService.submit(new SeatAllocationGenerator(separatedPointList, socDist));
            subResultFutureList.add(subResult);
        }

        for (Future<List<Point>>subResultFuture : subResultFutureList) {
            try {
                result.addAll(subResultFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.nanoTime();
        long totalTimeMilliSec = (endTime - startTime) / 1000000;
        logger.info(result.size() + " seats added to seat allocation with " +
                socDist / 10 + " m social distance in " + totalTimeMilliSec + " ms");
        return result;
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


}
