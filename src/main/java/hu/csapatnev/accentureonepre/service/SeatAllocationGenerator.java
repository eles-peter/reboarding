package hu.csapatnev.accentureonepre.service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class SeatAllocationGenerator implements Callable<List<Point>> {

    private List<Point> basePointList;
    private int socDist;
    private Map<Point, List<Point>> godNeighborsGraph;

    public SeatAllocationGenerator(List<Point> basePointList, int socDist) {
        this.basePointList = basePointList;
        this.socDist = socDist;
        this.godNeighborsGraph = createGodNeighborsGraph();
    }

    @Override
    public List<Point> call() throws Exception {
        return getBiggestMaximalCliques();
    }

    private Map<Point, List<Point>> createGodNeighborsGraph() {
        Map<Point, List<Point>> godNeighborsGraph = new HashMap<>();

        for (Point point : basePointList) {
            List<Point> badNeighbors = basePointList.stream()
                    .filter(otherPoint -> otherPoint.distance(point) >= socDist)
                    .collect(Collectors.toList());
            godNeighborsGraph.put(point, badNeighbors);
        }
        return godNeighborsGraph;
    }

    public List<Point> getBiggestMaximalCliques() {

        List<Set<Point>> cliques = getAllMaximalCliques();

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

    public List<Set<Point>> getAllMaximalCliques() {

        List<Set<Point>> cliques = new ArrayList<>();
        List<Point> potentialClique = new ArrayList<>();
        List<Point> alreadyFound = new ArrayList<>();
        List<Point> candidates = new ArrayList<>(godNeighborsGraph.keySet());

        findCliques(potentialClique, candidates, alreadyFound, cliques);

        return cliques;
    }

    private void findCliques(List<Point> potentialClique, List<Point> candidates, List<Point> alreadyFound, List<Set<Point>> cliques) {

        List<Point> candidatesArray = new ArrayList<>(candidates);
        if (!isEnd(candidates, alreadyFound)) {
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
                    findCliques(potentialClique, newCandidates, newAlreadyFound, cliques);
                }
                alreadyFound.add(candidate);
                potentialClique.remove(candidate);
            }
        }
    }

    private boolean isEnd(List<Point> candidates, List<Point> alreadyFound) {
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
