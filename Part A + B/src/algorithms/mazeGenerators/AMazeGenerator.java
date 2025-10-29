package algorithms.mazeGenerators;
import java.io.*;

public abstract class AMazeGenerator implements IMazeGenerator {


    @Override
    public long measureAlgorithmTimeMillis(int _row, int _colum) {
        long starting = System.currentTimeMillis();
        generate(_row,_colum);
        long ending = System.currentTimeMillis();
        long totalTime = ( ending - starting );
        return totalTime;
    }


}
