package Model;

import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.AState;
import algorithms.search.ISearchingAlgorithm;
import algorithms.search.Solution;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import Client.*;
import Server.*;
import IO.SimpleDecompressorInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyModel extends Observable implements IModel{
    private MyMazeGenerator mazeGenerator;
    private Maze maze;
    private int rowChar;
    private int colChar;
    private Server serverGenerate;
    private Server serverSolve;
    private boolean serversStatus = false;
    private Solution solution;
    private final Logger logServerGen= LogManager.getLogger();
    private final Logger logServerSolve= LogManager.getLogger();
    private final Logger logger = LogManager.getLogger(MyModel.class);

    public MyModel(){
        serverGenerate = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        serverSolve = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        serversStart();
        configureLogging();
    }

    private void configureLogging() {
        System.setProperty("log4j2.configurationFile", "resources/log4j2.xml");
    }

    public void serversClose(){
        if (serversStatus == true){
            serverGenerate.stop();
            serverSolve.stop();
        }
    }

    public void serversStart(){
        serverGenerate.start();
        serverSolve.start();
        serversStatus = true;
    }

    private void generateMazeInServer(int rows,int cols){
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, cols};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject();
                        InputStream is = new SimpleDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[99999];
                        is.read(decompressedMaze);
                        maze = new Maze(decompressedMaze);
                        maze.print();
                        logServerGen.info("IP: "+InetAddress.getLocalHost()+" Maze size: ("+rows+","+cols+").");
                    } catch (Exception e) { e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void solveMazeServer() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze);
                        toServer.flush();
                        solution = (Solution) fromServer.readObject();
                        System.out.println(String.format("Solution steps:%s", solution));
                        ArrayList<AState> mazeSolutionSteps = solution.getSolutionPath();
                        for (int i = 0; i < mazeSolutionSteps.size(); i++) {
                            System.out.println(String.format("%s. %s", i, mazeSolutionSteps.get(i).toString()));
                        }
                        ISearchingAlgorithm searchingAlgorithm = Configurations.getSearchAlgo();
                        logServerSolve.info("IP: "+InetAddress.getLocalHost()+" Solve Algo: "+searchingAlgorithm.getName()+
                                " Solution: "+ mazeSolutionSteps.size()+" steps");
                    } catch (Exception e) { e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) { e.printStackTrace();
        }
    }

    public void Model() {
        maze = null;
        rowChar = 0;
        colChar = 0;

    }

    public void updateCharacterLocation(int direction)
    {
        /*
            direction = 1 -> Up
            direction = 2 -> Down
            direction = 3 -> Left
            direction = 4 -> Right
            direction = 5 -> Up - Left
            direction = 6 -> Up - Right
            direction = 7 -> Down - Left
            direction = 8 -> Down - Right
         */

        switch(direction)
        {
            case 1: // Up
                if ((rowChar > 0) && (maze.getMaze()[rowChar - 1][colChar] != 1)){
                    rowChar--;
                }
                break;

            case 2: // Down
                if ((rowChar<maze.getMaze().length - 1) && (maze.getMaze()[rowChar+1][colChar] != 1)){
                    rowChar++;
                }
                break;
            case 3: // Left
                if ((colChar > 0) && (maze.getMaze()[rowChar][colChar - 1] != 1)){
                    colChar--;
                }
                break;
            case 4: // Right
                if ((colChar < maze.getMaze().length - 1) && (maze.getMaze()[rowChar][colChar + 1] != 1)){
                    colChar++;
                }
                break;
            case 5: // Up - Left
                if ((rowChar > 0 && colChar > 0) && (maze.getMaze()[rowChar - 1][colChar - 1] != 1)
                && (maze.getMaze()[rowChar - 1][colChar] == 0 || maze.getMaze()[rowChar][colChar - 1] == 0)){
                    rowChar--;
                    colChar--;
                }
                break;
            case 6: // Up - Right
                if ((rowChar> 0 && colChar < maze.getMaze().length - 1) && (maze.getMaze()[rowChar - 1][colChar + 1 ] != 1)
                        && (maze.getMaze()[rowChar - 1][colChar] == 0 || maze.getMaze()[rowChar][colChar + 1] == 0)){
                    rowChar--;
                    colChar++;
            }
                break;
            case 7: // Down - Left
                if ((rowChar < maze.getMaze().length - 1 && colChar > 0) && (maze.getMaze()[rowChar + 1][colChar - 1] != 1)
                        && (maze.getMaze()[rowChar + 1][colChar] == 0 || maze.getMaze()[rowChar][colChar - 1] == 0)){
                    rowChar++;
                    colChar--;
                }
                break;
            case 8: // Down - Right
                if ((rowChar < maze.getMaze().length - 1 && colChar < maze.getMaze().length - 1) && (maze.getMaze()[rowChar + 1][colChar + 1] != 1)
                        && (maze.getMaze()[rowChar + 1][colChar] == 0 || maze.getMaze()[rowChar][colChar + 1] == 0)){
                    rowChar++;
                    colChar++;
                }
                break;

        }

        setChanged();
        notifyObservers();
    }

    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void solveMaze(Maze maze) {
        solveMazeServer();
        setChanged();
        notifyObservers();
    }

    @Override
    public Solution getSolution() {
        return solution;
    }


    public void generateMaze(int row, int col)
    {
        generateMazeInServer(row, col);
        setChanged();
        notifyObservers();
    }

    public Maze getMaze() {
        return maze;
    }

    public void saveMazeToFile(Maze maze, String filePath){
        try {
            byte[] byteMaze = maze.toByteArray();
            FileOutputStream outputStream = new FileOutputStream(filePath);
            outputStream.write(byteMaze);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Maze loadMazeFromFile(String filePath) {
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            byte[] byteMaze = inputStream.readAllBytes();
            inputStream.close();
            Maze maze = new Maze(byteMaze);
            return maze;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void restartPosition() {
        rowChar = 0;
        colChar = 0;
        restartSolution();
        setChanged();
        notifyObservers();
    }

    public void restartSolution(){
        solution = null;
    }

    public void exit(){
        serversClose();
    }

    @Override
    public double calculateProgress() {
        int totalCells = maze.getMaze().length * maze.getMaze()[0].length;
        int playerRow = rowChar;
        int playerColumn = colChar;
        int filledCells = playerRow * maze.getMaze()[0].length + playerColumn;
        double progress = (double) filledCells / totalCells;
        progress = Math.min(Math.max(progress, 0.0), 1.0);
        return progress;
    }

}
