package algorithms.mazeGenerators;

public interface IMazeGenerator {

    public Maze generate(int _colum, int _row);

    public long measureAlgorithmTimeMillis(int _row,int _colum);
}
