package hu.csapatnev.accentureonepre.service;

import hu.csapatnev.accentureonepre.AccentureOnePreApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
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

        for (List<Point> separatedPointList : separatedPointListList) {
            List<Point> subResult = separatedPointListAllocationGenerator(separatedPointList, socDist);
            result.addAll(subResult);
        }

        long endTime = System.nanoTime();
        long totalTimeMilliSec = (endTime - startTime) / 1000000;
        logger.info(result.size() + " seats added to seat allocation with " +
                socDist/10 + " m social distance in " + totalTimeMilliSec + " ms");
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


    private List<Point> separatedPointListAllocationGenerator(List<Point> separatedPointList, int socDist) {
        Map<Point, List<Point>> godNeighborsGraph = createGodNeighborsGraph(separatedPointList, socDist);
        return getBiggestMaximalCliques(godNeighborsGraph);
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
                    findCliques(potentialClique, newCandidates, newAlreadyFound, cliques, godNeighborsGraph);
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


}
