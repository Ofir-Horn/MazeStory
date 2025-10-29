package algorithms.search;

import algorithms.mazeGenerators.Position;

import java.util.*;

public class DepthFirstSearch extends ASearchingAlgorithm  {

    @Override
    public Solution solve(ISearchable domain) {
        if (domain == null){
            return null;
        }
        ArrayList<AState> path = DFSAlgo(domain);
        return new Solution(path);
    }

    private ArrayList<AState> DFSAlgo(ISearchable domain) {
        // Define starting and goal positions
        AState startPosition = domain.getStartState();
        AState goalPosition = domain.getGoalState();

        // Init the stack, and a linked list to recover visited states
        Stack<AState> stackStates = new Stack<>();
        LinkedList<AState> visitedStates = new LinkedList<>();
        HashSet<String> visited = new HashSet<>();

        // Adding start position to both the stack, and to the already visited states + counting
        stackStates.push(startPosition);
        visitedStates.add(startPosition);
        visited.add(startPosition.getString());
        this.NumberOfVisitedNodes++;

        // While the stack is not empty
        while (!stackStates.empty()) {
            // Pop currentState from the stack
            AState currentState = stackStates.pop();

            // Check if the currentState is the goal
            if (currentState.equals(goalPosition)) {
                return backtracePath(currentState);
            }

            // Get the neighbors of the current state
            // Check all neighbors and add to stack if not visited
            for (AState neighbor: domain.getAllPossibleStates(currentState)) {
                // If the neightbor has not been visited
                if (!visited.contains(neighbor.getString())) {
                    visited.add(neighbor.getString()); // Add
                    this.NumberOfVisitedNodes++; // Count
                    neighbor.setCameFrom(currentState); // Set father
                    // Check if the neightbor is the goal position, if so - return the path
                    if (neighbor.equals(goalPosition)) {
                        return backtracePath(neighbor);
                    }
                    // Push neightbor into stack
                    stackStates.push(neighbor);
                }
            }

        }
        // If there is no path, return an empty array
        System.out.println("Could not find a solution path"); //TEST
        return new ArrayList<>();
    }



    // Method: backtracePath
    private ArrayList<AState> backtracePath(AState state) {
        //Create a new array
        ArrayList<AState> path = new ArrayList<>();
        // While there is another father state
        while (state.getCameFrom() != null) {
            path.add(state);
            state = state.getCameFrom();
        }
        // Add state
        path.add(state);
        // Reverse
        Collections.reverse(path);
        return path;
    }

    @Override
    public Object getNumberOfNodesEvaluates() {
        return this.NumberOfVisitedNodes;
    }
}
