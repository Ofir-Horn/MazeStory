package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {
    private int port;
    private final int intervalMS;
    private IServerStrategy serverStrategy;
    private volatile boolean stop;
    private final ThreadPoolExecutor threadPool;

    // Constructor
    // Server recieves 3 param:
    // (1) int: port (2) int: intervalMS (3) IServerStrategy: serverStrategy
    public Server(int port, int intervalMS, IServerStrategy serverStrategy){
        this.port = port;
        this.intervalMS = intervalMS;
        this.serverStrategy = serverStrategy;
        this.stop = false; // Set to false by default
        this.threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        threadPool.setCorePoolSize(Configurations.getInstance().getTPSize());
    }

    // TODO: Check why inserting the thread to the original start did not work
    public void start(){
        // Create new thread and start the connection
        new Thread(this::startConnection).start();
    }

    // This function starts the connection, and calls the handleClient function
    // to take care of the call.
    public void startConnection()
    {
        try {
            // Set new server socket + set interval time to given input
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(this.intervalMS);
            // While server connection is not stopped
            while (!stop)
            {
                try {
                    // Wait for client
                    Socket clientSocket = serverSocket.accept();
                    // Create a new thread
                    /*
                    new Thread(() -> {
                        // Call 'handleClient' function
                        handleClient(clientSocket);
                    }).start();
                     */
                    threadPool.execute(() -> handleClient(clientSocket));
                    // TODO: Why sometimes giving an exception? check thread sleep time (?)
                    Thread.sleep(5000);
                }
                // Catch overtime exception
                catch (IOException | InterruptedException e) {
                    System.out.println("Where are the clients?");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This function recieves the client's socket, and uses the server's strategy to handle
    // the call.
    private void handleClient(Socket clientSocket){
        try {
            // Set streams, and use server's strategy to handle the call
            InputStream inFromClient = clientSocket.getInputStream();
            OutputStream outToClient = clientSocket.getOutputStream();
            this.serverStrategy.ServerStrategy(inFromClient, outToClient);
            System.out.println("client end");
            // Close all unneeded connections
            inFromClient.close();
            outToClient.close();
            clientSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // This function sets the server's 'stop' condition to 'true',
    // therefor stopping it's work.
    public void stop() {
        this.stop = true;
        this.threadPool.shutdown();
    }

}
