package Server;


import algorithms.mazeGenerators.Maze;
import algorithms.search.*;
import java.io.*;


public class ServerStrategySolveSearchProblem implements IServerStrategy{
    String tempDirectoryPath = System.getProperty("java.io.tmpdir");

    //TODO: Split into seperate functions

    /*
    ServerStrategy
    Server recieves fropm the client a maze object representing a maze,
    solves it with the pre-defined search-algo from the config file,
    and returns a Solution holding the solution for the maze.
     */
    @Override
    public void ServerStrategy(InputStream inFromClient, OutputStream outToClient) throws IOException {
        try {
            // Input/Output streams from/to client
            ObjectInputStream inputClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream outputClient = new ObjectOutputStream(outToClient);

            // Read the maze from the client
            Maze currentMaze = (Maze) inputClient.readObject();

            // Check if a solution exists for the maze, using the uniqe hashCode
            String solutionFilePath = tempDirectoryPath + "/" + currentMaze.hashCode() + ".sol";
            boolean solutionExistsStatus = new File(solutionFilePath).exists();
            Solution currentSolution;

            if (solutionExistsStatus) {
                // Check solution from existing solutions
                System.out.println("SOLUTION EXISTS");
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(solutionFilePath));
                // Read and close
                currentSolution = (Solution) in.readObject();
                in.close();
            } else {
                // Create new solution
                System.out.println("SOLUTION DOES NOT EXIST");
                // Get config's singleton instance
                Configurations.getInstance();
                // Get searching algo from the config
                ISearchingAlgorithm searchAlgo = Configurations.getSearchAlgo();
                // Search maze and get the solution
                SearchableMaze searchMaze = new SearchableMaze(currentMaze);
                currentSolution = searchAlgo.solve(searchMaze);
                // Write and close
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(solutionFilePath));
                out.writeObject(currentSolution);
                out.flush();
                out.close();
            }

            // Send the solution to the client and close
            outputClient.writeObject(currentSolution);
            outputClient.flush();
            outputClient.close();
            inputClient.close();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}