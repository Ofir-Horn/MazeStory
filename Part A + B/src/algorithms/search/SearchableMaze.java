package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import java.util.ArrayList;
import java.util.LinkedList;

public class SearchableMaze implements ISearchable{
    private final Maze myMaze;

    public SearchableMaze(Maze maze){
        this.myMaze = maze;
    }

    public Maze getMyMaze() {
        return myMaze;
    }


    @Override
    // Method: getAllPossibleStates
    // This method recieves a state, and finds all the state's reachable from it,
    // returning them as a linkedlist, containing the state's themeselves and the cost to reach them
    public ArrayList<AState> getAllPossibleStates(AState state) {
        ArrayList<AState> allPossibleStates = new ArrayList<>();
        Position currentPosition = (Position) state.getState();
        boolean isValidUp = myMaze.isValid(currentPosition.getUp());
        boolean isValidRight = myMaze.isValid(currentPosition.getRight());
        boolean isValidDown = myMaze.isValid(currentPosition.getDown());
        boolean isValidLeft = myMaze.isValid(currentPosition.getLeft());

        boolean isWallUp = isValidUp && myMaze.isWall(currentPosition.getUp());
        boolean isWallRight = isValidRight && myMaze.isWall(currentPosition.getRight());
        boolean isWallDown = isValidDown && myMaze.isWall(currentPosition.getDown());
        boolean isWallLeft = isValidLeft && myMaze.isWall(currentPosition.getLeft());

        Position upRight = currentPosition.getUp().getRight();
        Position downRight = currentPosition.getDown().getRight();
        Position upLeft = currentPosition.getUp().getLeft();
        Position downLeft = currentPosition.getDown().getLeft();

        if (myMaze.isValid(upRight) && !myMaze.isWall(upRight) && (!isWallUp || !isWallRight))
            allPossibleStates.add(new MazeState(upRight, state.getCost() + 15, state));

        if (myMaze.isValid(downRight) && !myMaze.isWall(downRight) && (!isWallDown || !isWallRight))
            allPossibleStates.add(new MazeState(downRight, state.getCost() + 15, state));

        if (myMaze.isValid(upLeft) && !myMaze.isWall(upLeft) && (!isWallUp || !isWallLeft))
            allPossibleStates.add(new MazeState(upLeft, state.getCost() + 15, state));

        if (myMaze.isValid(downLeft) && !myMaze.isWall(downLeft) && (!isWallDown || !isWallLeft))
            allPossibleStates.add(new MazeState(downLeft, state.getCost() + 15, state));

        if (isValidUp && !isWallUp)
            allPossibleStates.add(new MazeState(currentPosition.getUp(), state.getCost() + 10, state));

        if (isValidRight && !isWallRight)
            allPossibleStates.add(new MazeState(currentPosition.getRight(), state.getCost() + 10, state));

        if (isValidDown && !isWallDown)
            allPossibleStates.add(new MazeState(currentPosition.getDown(), state.getCost() + 10, state));

        if (isValidLeft && !isWallLeft)
            allPossibleStates.add(new MazeState(currentPosition.getLeft(), state.getCost() + 10, state));

        return allPossibleStates;
    }



    // Method: isAngeled
    // This function recieves a state, and a direction, and returns a boolean indicating if the diagonak is
    // posible for access.
    private boolean isAngeled(AState state, int direction) {
        // Define variables
        MazeState mazeState = (MazeState) state;
        Position position = mazeState.getPosition();
        int row = position.getRowIndex();
        int column = position.getColumnIndex();

        int[][] availablePositions = {{-1,1},{1,1},{1,-1},{-1,-1}};  // possible angeled shifts
        int[] currentMove = availablePositions[direction / 2]; // current shift based on direction

        int newRow = row + currentMove[0];
        int newColumn = column + currentMove[1];

        // If new position not inside the maze
        if (!withinBound(newRow, newColumn)) {
            return false;
        }

        // Check if both near cells are avaiable
        int[][] nearMove = {{-1,0},{0,1},{1,0},{0,-1}};
        int[] firstMove = nearMove[(direction + 1) % 8 / 2];
        int[] secondMove = nearMove[(direction - 1) % 8 / 2];

        int firstNearRow = row + firstMove[0];
        int firstNearColumn = column + firstMove[1];
        int secondNearRow = row + secondMove[0];
        int secondNearColumn = column + secondMove[1];

        // Check if both available positions are inside the maze and 0
        if (withinBound(firstNearRow, firstNearColumn) && myMaze.getMaze()[firstNearRow][firstNearColumn] == 0
                && withinBound(secondNearRow, secondNearColumn) && myMaze.getMaze()[secondNearRow][secondNearColumn] == 0) {
            return true;
        }
        return false;
    }



    // Method: withinBound
    // Returns a boolean indicating if the given coordinates are within the maze's boundries
    private boolean withinBound(int row, int col) {
        return row >= 0 && row < myMaze.getMaze().length && col >= 0 && col < myMaze.getMaze()[0].length;
    }

    //Method: getStartState
    @Override
    public AState getStartState() {
        return new MazeState(myMaze.getStartPosition(), 0, null);
    }

    //Method: getGoalState
    @Override
    public AState getGoalState() {
        return new MazeState(myMaze.getGoalPosition(), 0, null);
    }

}
