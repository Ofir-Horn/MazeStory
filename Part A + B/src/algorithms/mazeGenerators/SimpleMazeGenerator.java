package algorithms.mazeGenerators;
import java.util.Random;

public class SimpleMazeGenerator extends AMazeGenerator{

    @Override
    public Maze generate(int rows, int columns) {
        Maze simpleMaze = new Maze(rows, columns);
        Random rand = new Random();

        // Randomly fill the maze with walls '1' or empty spaces '0'
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i == 0 && j == 0) || (i == rows - 1 && j == columns - 1)) {
                    continue;
                }
                int val = rand.nextInt(2);
                simpleMaze.setValueIndex(i, j, val);
            }
        }

        // Loop the first row and last column, making sure there is a path there
        for (int i = 1; i < columns; i++){
            simpleMaze.setValueIndex(0, i, 0);
        }
        for (int i = 0; i < rows - 1; i++){
            simpleMaze.setValueIndex(i, columns - 1, 0);
        }
        // Return simpleMaze
        return simpleMaze;
    }
}
