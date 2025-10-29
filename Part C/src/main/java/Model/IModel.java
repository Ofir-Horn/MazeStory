package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.util.Observer;

public interface IModel {
    public void generateMaze(int row, int col);
    public Maze getMaze();
    public void updateCharacterLocation(int direction);
    public int getRowChar();
    public int getColChar();
    public void assignObserver(Observer o);
    public void solveMaze(Maze maze);
    public Solution getSolution();
    public void serversClose();
    void saveMazeToFile(Maze maze, String filePath);
    Maze loadMazeFromFile(String filePath);
    void restartPosition();
    void exit();

    double calculateProgress();
}
