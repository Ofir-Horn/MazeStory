package algorithms.search;

import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Queue;

public class BreadthFirstSearch extends ASearchingAlgorithm{
    private Queue<AState> queueState;

    public BreadthFirstSearch(){
        queueState = new LinkedList<>();
    }

    protected BreadthFirstSearch(Queue<AState> queueState) {
        this.queueState = queueState;
    }


    @Override
    public Solution solve(ISearchable domain) {
        if (domain == null){
            return null;
        }
        ArrayList<AState> path = BFSAlgo(domain);
        return new Solution(path);
    }

    @Override
    public Object getNumberOfNodesEvaluates() {
        return this.NumberOfVisitedNodes;
    }


    private ArrayList<AState> BFSAlgo(ISearchable domain) {
        if (domain == null)
            return null;

        AState startPosition = domain.getStartState();
        AState goalPosition = domain.getGoalState();

        AState currentState;

        HashSet<String> visitedStates = new HashSet<>();
//        ArrayDeque<AState> queue = new ArrayDeque<>();
//        Queue<AState> queueState = new LinkedList<>();


        visitedStates.add(startPosition.getString());
        queueState.add(startPosition);
        this.NumberOfVisitedNodes++;

        //while the queue is not empty
        while (!queueState.isEmpty()) {
            currentState = queueState.poll();

            if (currentState.equals(goalPosition)) {
                this.NumberOfVisitedNodes = visitedStates.size();
                ArrayList<AState> solutionList = new ArrayList<>();
                solutionList.add(currentState);
                return solutionList;
            }
//            LinkedList<AState> neighbors = domain.getAllPossibleStates(currentState);
//            Collections.shuffle(neighbors);
            for (AState neighbor : domain.getAllPossibleStates(currentState)) {
                String neighborString = neighbor.getString();

                // If the neightbor has not been visited
                if (visitedStates.add(neighborString)) {
                    queueState.add(neighbor);
                    this.NumberOfVisitedNodes++; // Count
                    neighbor.setCameFrom(currentState);

                    if (neighbor.equals(goalPosition)) {
                        return backtracePath(neighbor);
                    }
                }
            }
        }
        this.NumberOfVisitedNodes = visitedStates.size();
        return new ArrayList<>();

        // If the algorithm reaches here, it means it couldn't find a path to the goal state
//        System.out.println("Could not find a solution path");
//        return new ArrayList<>();
    }


    private ArrayList<AState> backtracePath(AState state) {
        ArrayList<AState> path = new ArrayList<>();
        while (state.getCameFrom() != null) {
            path.add(state);
            state = state.getCameFrom();
        }
        path.add(state);
        Collections.reverse(path);
        return path;
    }
}
