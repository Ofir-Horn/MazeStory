package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import java.io.*;
public class ServerStrategyGenerateMaze implements IServerStrategy{
    /*
    ServerStrategy
    Gets an int[] input from the client, where int[0] = rows, int[1] = columns.
    The server than generates a maze built on these parameters, and compressing it,
    returning a byte[] representing the maze.
     */
    @Override
    public void ServerStrategy(InputStream inFromClient, OutputStream outToClient) throws IOException {
        try {
            // Input/Output streams from/to client
            ObjectInputStream inputClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream outputClient = new ObjectOutputStream(outToClient);

            // Read maze dimensions from the client
            int[] mazeDimensions = (int[]) inputClient.readObject();

            // Generate the maze using the selected algorithm
            Maze newMaze = generateMaze(mazeDimensions);

            // Compress the maze data
            byte[] compressedMaze = compressMaze(newMaze);

            // Send compressed maze to client
            outputClient.writeObject(compressedMaze);
            outputClient.flush();

            // Close streams
            outputClient.close();
            inputClient.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
    Receives the int[] for maze dimensions, and returns the maze, using the config's defines parameter.
     */
    private Maze generateMaze(int[] mazeDimensions) throws IllegalArgumentException {
        if (mazeDimensions == null)
            throw new IllegalArgumentException("Invalid input");
        // Get config's instance since it's a singleton
        Configurations config = Configurations.getInstance();
        // Get the defined mazo generating algo.
        IMazeGenerator mazeGenerator = config.getMazeAlgo();
        return mazeGenerator.generate(mazeDimensions[0], mazeDimensions[1]);
    }

    /*
    Receives a maze, and compressing it into a byte[].
     */
    private byte[] compressMaze(Maze maze) throws IOException,IllegalArgumentException {
        if (maze == null)
            throw new IllegalArgumentException("Invalid input");
        // Defined streams
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        MyCompressorOutputStream compOut = new MyCompressorOutputStream(byteArray);
        // write maze's byte[]
        compOut.write(maze.toByteArray());
        // flush and close
        compOut.flush();
        compOut.close();
        return byteArray.toByteArray();
    }

}