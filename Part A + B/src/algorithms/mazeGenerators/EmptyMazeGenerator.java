package algorithms.mazeGenerators;

public class EmptyMazeGenerator extends AMazeGenerator{


    @Override
    public Maze generate(int row, int column) {
        // Generate and return an empty maze
        Maze emptyMaze = new Maze(row, column);
        return emptyMaze;
    }
}
