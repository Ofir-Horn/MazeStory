package algorithms.mazeGenerators;
import java.util.Collections;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Random;

public class MyMazeGenerator extends AMazeGenerator{

    //Method 1: generate
    // This method implements the generate method, creating a maze using the DFS algorithm
    // Stages of the algorithm:
    // 1. Choose the initial cell, mark it as visited (set value to '0'), and push it to the stack
    // 2. while the stack is not empty
    // 2.1 If the current cell has any neighbors which have not been visited
    // 2.2 If the current cell has any neighbors which have not been visited
    // 2.2.1 Choose one of the unvisited neighbors
    // 2.2.2 Remove the wall between the current cell and the chosen cell
    // 2.2.3 Mark the chosen cell as visited (1) and push it to the stack
    @Override
    public Maze generate(int rows, int columns) {
    /*    Maze maze = new Maze(rows, columns);
        Stack<Position> stackPosition = new Stack<>(); // Stack will be used to hold positions for the carving
        ArrayList<Position> walls;
        Random rand = new Random();
        // Fill each position with a wall '1' except for the startPosition and goalPosition
        // Outer loop (rows)
        for (int i = 0; i < rows; i++){
            // Inner loop (columns)
            for (int j = 0; j < columns; j++){
                // Make sure the current index is not the startPosition or the goalPosition
                if (maze.getValueIndex(i, j) != 2 && maze.getValueIndex(i, j) != 3){
                    maze.setValueIndex(i, j, 1);
                }
            }
        }
        Position currentPosition = maze.getStartPosition();
        stackPosition.push(currentPosition);
        while (!stackPosition.isEmpty()){
            currentPosition = stackPosition.pop();
            walls = maze.getNeighbors(currentPosition);
            if (walls.size() != 0){
                stackPosition.push(currentPosition);
                Position randomNeighbor = walls.get(rand.nextInt(walls.size()));
                maze.setValueIndex(randomNeighbor.getRowIndex(),randomNeighbor.getColumnIndex(),0);
                if (currentPosition.getColumnIndex() == randomNeighbor.getColumnIndex()){
                    int minRow = Math.min(randomNeighbor.getRowIndex(),currentPosition.getRowIndex() + 1);
                    maze.setValueIndex(minRow,currentPosition.getColumnIndex(),0);
                }
                else if (currentPosition.getRowIndex() == randomNeighbor.getRowIndex()){
                    int minColumn = Math.min(randomNeighbor.getColumnIndex(),currentPosition.getColumnIndex() + 1);
                    maze.setValueIndex(currentPosition.getRowIndex(),minColumn,0);
                }
                stackPosition.push(randomNeighbor);
            }
        }
        return maze;
    }*/

        // Initialize the maze's values
        Maze maze = new Maze(rows, columns);
        Stack<Position> stackPosition = new Stack<>(); // Stack will be used to hold positions for the carving
        int currentRow;
        int currentColumn;
        Random random = new Random();

        // Fill each position with a wall '1' except for the startPosition and goalPosition
        // Outer loop (rows)
        for (int i = 0; i < rows; i++){
            // Inner loop (columns)
            for (int j = 0; j < columns; j++){
                // Make sure the current index is not the startPosition or the goalPosition
                if (maze.getValueIndex(i, j) != 2 && maze.getValueIndex(i, j) != 3){
                    maze.setValueIndex(i, j, 1);
                }
            }
        }
        //1. Choose the initial cell, mark it as visited (set value to '0'), and push it to the stack
        Position currentPosition = randomPosition();
        // Define the starting points as a path
        maze.setValueIndex(currentPosition.getRowIndex(), currentPosition.getColumnIndex(), 0);
        stackPosition.push(currentPosition);

        // 2. while the stack is not empty
        while (stackPosition.isEmpty() == false){
            // 2.1 Pop a cell from the stack and make it a current cell
            currentPosition = stackPosition.pop(); // Pop the stack, and get it's values
            currentRow = currentPosition.getRowIndex();
            currentColumn = currentPosition.getColumnIndex();

            // Niehgbors
            ArrayList<Position> currentNeighbors = getNeighbors(currentRow, currentColumn, rows, columns); // Find new neighbors
            Collections.shuffle(currentNeighbors, random); // Randomize the array's order

            // Iterate all neighbors
            for (int i = 0; i < currentNeighbors.size(); i++) {
                int neighborRow = currentNeighbors.get(i).getRowIndex();
                int neighborCol = currentNeighbors.get(i).getColumnIndex();
                // 2.2 If the current cell has any neighbors which have not been visited
                if (maze.getValueIndex(neighborRow, neighborCol) == 1) {
                    // 2.2.1 Choose one of the unvisited neighbors
                    // 2.2.2 Remove the wall between the current cell and the chosen cell
                    maze.setValueIndex(neighborRow, neighborCol, 0);
                    maze.setValueIndex((currentRow + neighborRow) / 2, (currentColumn + neighborCol) / 2, 0);
                    // 2.2.3 Mark the chosen cell as visited (1) and push it to the stack
                    stackPosition.push(currentNeighbors.get(i));
                }
            }
        }
        // Return maze
        return maze;}
//        return maze;
//    }

    //Method: randomPosition
    //This method returns a random position to be used as a starting point for the maze generator
    public Position randomPosition(){
        Random random = new Random();
        int randomInt = random.nextInt(2);
        if (randomInt == 0){
            return new Position(0, 1);
        }
        else {
            return new Position(1, 0);
        }
    }

    //Method: getNeighbors
    //This method returns an array holding the Positions's of the neighbors for the current position
    private ArrayList<Position> getNeighbors(int row, int colomn, int rows, int columns) {
        ArrayList<Position> neighborsPositions = new ArrayList<>();
        if (row > 1) {
            neighborsPositions.add(new Position(row - 2, colomn));
        }
        if (colomn > 1) {
            neighborsPositions.add(new Position(row, colomn - 2));
        }
        if (row < rows - 2) {
            neighborsPositions.add(new Position(row + 2, colomn));
        }
        if (colomn < columns - 2) {
            neighborsPositions.add(new Position(row, colomn + 2));
        }
        return neighborsPositions;
    }

}
