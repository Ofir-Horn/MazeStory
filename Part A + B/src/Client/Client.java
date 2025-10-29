package Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private InetAddress serverIP;
    private int port;
    private IClientStrategy clientStrategy;

    // Constructor
    // Client recieves 3 params:
    // (1) InetAddress: serverIP (2) int: port (3) IClientStrategy: clientStrategy
    // (1) the serever's ip
    // (2) the free port "apartment number" for the socket to connect to
    // (3) the client's strategy
    public Client(InetAddress serverIP, int port, IClientStrategy clientStrategy){
        this.serverIP = serverIP;
        this.port = port;
        this.clientStrategy = clientStrategy;
    }

    // This function establishes the client's socket, that will connect with the server,
    // and announces the connection has been established.
    public void communicateWithServer(){
        try {
            Socket clientSocket = new Socket(serverIP, port);
            System.out.println("Client's connection has been established.");
            clientStrategy.clientStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
            clientSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
