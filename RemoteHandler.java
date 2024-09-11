package Lab1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * The RemoteHandler class manages communication between a client socket and a SmartTV.
 * It handles the initialization and setup required to handle remote commands for the SmartTV.
 */
public class RemoteHandler {
    private SmartTV smartTV;
    private Socket clientSocket;
    private BufferedReader sockerReader;

    public RemoteHandler(SmartTV smartTV, Socket clientSocket) {
        this.smartTV = smartTV;
        this.clientSocket = clientSocket;
        System.out.println("Client connected. Address: " + clientSocket.getRemoteSocketAddress()
                + ", Port: " + clientSocket.getPort());
    }

    public void run() {
        try {
            sockerReader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String remoteCommand = readOneCommand(clientSocket);

        } catch (IOException e) {
            System.err.println("Error while processing the client: " + e.getMessage());
        }


    }

    private String readOneCommand(Socket clientSocket) {
        String remoteCommand = null;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()))) {
            remoteCommand = in.readLine();
            if (remoteCommand != null) {
                System.out.println("Received command: " + remoteCommand);
            }
        } catch (IOException e) {
            System.err.println("Error reading command from client: " + e.getMessage());
        }
        return remoteCommand;
    }

}


