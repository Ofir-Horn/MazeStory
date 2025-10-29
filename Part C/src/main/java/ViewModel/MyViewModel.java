package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel model;
    private Maze maze;
    private int rowChar;
    private int colChar;

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
        this.maze = null;
    }


    public Maze getMaze() {
        return maze;
    }


    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof IModel)
        {
            if(maze == null) //generateMaze
            {
                this.maze = model.getMaze();
            }
            else {
                Maze maze = model.getMaze();

                if (maze == this.maze) //Not generateMaze
                {
                    int rowChar = model.getRowChar();
                    int colChar = model.getColChar();
                    if(this.colChar == colChar && this.rowChar == rowChar) //Solve Maze
                    {
                        model.getSolution();
                    }
                    else // Update location
                    {
                        this.rowChar = rowChar;
                        this.colChar = colChar;
                    }


                }
                else // GenerateMaze
                {
                    this.maze = maze;
                }
            }

            setChanged();
            notifyObservers();
        }
    }


    public void generateMaze(int row,int col)
    {
        this.model.generateMaze(row,col);
    }

    public void moveCharacter(KeyEvent keyEvent)
    {
        int direction = -1;

        switch (keyEvent.getCode()){
            // UP
            case UP:
                direction = 1;
                break;
            case NUMPAD8:
                direction = 1;
                break;
            // DOWN
            case DOWN:
                direction = 2;
                break;
            case NUMPAD2:
                direction = 2;
                break;
            // LEFT
            case LEFT:
                direction = 3;
                break;
            case NUMPAD4:
                direction = 3;
                break;
            // RIGHT
            case RIGHT:
                direction = 4;
                break;
            case NUMPAD6:
                direction = 4;
                break;
            // UP - LEFT
            case NUMPAD7:
                direction = 5;
                break;
            // UP - RIGHT
            case NUMPAD9:
                direction = 6;
                break;
            // DOWN - LEFT
            case NUMPAD1:
                direction = 7;
                break;
            // DOWN - RIGHT
            case NUMPAD3:
                direction = 8;
                break;
        }
        model.updateCharacterLocation(direction);
    }

    public void solveMaze(Maze maze)
    {
        model.solveMaze(maze);
    }
    public Solution getSolution()
    {
        return model.getSolution();
    }
    public void saveMazeToFile(Maze maze, String filePath) {
        model.saveMazeToFile(maze, filePath);
    }
    public Maze loadMazeFromFile(String filePath){
        return model.loadMazeFromFile(filePath);
    }
    public void restartPosition() {
        model.restartPosition();
    }
    public void exitGame() {
        model.exit();
    }
    public double calculateProgress() {
        return model.calculateProgress();
    }
}
