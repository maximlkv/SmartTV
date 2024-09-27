package Lab1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The RemoteHandler class manages communication between a client socket and a SmartTV.
 * It handles the initialization and setup required to handle remote commands for the SmartTV.
 */
public class RemoteHandler {
    private SmartTV smartTV;
    private Socket clientSocket;
    private BufferedReader socketReader;
    private PrintWriter socketWriter;

    public RemoteHandler(SmartTV smartTV, Socket clientSocket) {
        this.smartTV = smartTV;
        this.clientSocket = clientSocket;
        System.out.println("Client connected. Address: " + clientSocket.getRemoteSocketAddress()
                + ", Port: " + clientSocket.getPort());
    }

    public void run() {
        try {
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            sendInterfaceToRemote();
            String remoteCommand;
    
            while ((remoteCommand = socketReader.readLine()) != null) {
                System.out.println("Received command: " + remoteCommand);
                handleCommand(remoteCommand);
    
                if (remoteCommand.equals("-1")) {
                    System.out.println("Termination command received. Disconnecting client.");
                    break; 
                }
    
            }
    
        } catch (IOException e) {
            System.err.println("Error while processing the client: " + e.getMessage());
        } finally {
            closeConnection();
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

    private void closeConnection() {
        try {
            if (socketReader != null) {
                socketReader.close();  
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close(); 
                System.out.println("Client connection closed.");
            }
        } catch (IOException e) {
            System.err.println("Error while closing the client connection: " + e.getMessage());
        }
    }

    private void handleCommand(String remoteCommand) {
        try {
            if (remoteCommand.equals("1")) {
                if (!smartTV.getIsOn()) {
                    smartTV.setIsOn(true);
                    socketWriter.println("TV turned on.");
                } else {
                    smartTV.setIsOn(false);
                    socketWriter.println("TV turned off.");
                }
            }
            else if (remoteCommand.equals("2") && smartTV.getIsOn()) {
                smartTV.setChannelUp();
                socketWriter.println("Channel up. New Channel: " + smartTV.getActiveChannel());

            }
            else if (remoteCommand.equals("3") && smartTV.getIsOn()) {
                smartTV.setChannelDown();
                socketWriter.println("Channel down. New Channel: " + smartTV.getActiveChannel());

            } else {
                socketWriter.println("Unknown command. Please use one of the following buttons:");
            }
            sendInterfaceToRemote();

        } catch (Exception e) {
            System.err.println("Error handling Command: " + e.getMessage());
        }
    }

    private void sendInterfaceToRemote() {
        try {
            if (smartTV.getIsOn()) {
                socketWriter.println("---------------------------------------");
                socketWriter.println("Current Channel: " + smartTV.getActiveChannel() + "\n"); 
                socketWriter.println("Controls:"); 
                socketWriter.println("1 - Turn TV on/off");
                socketWriter.println("2 - Switch one channel up"); 
                socketWriter.println("3 - Switch one channel down"); 
                socketWriter.println("---------------------------------------");
                socketWriter.println("END_OF_MESSAGE");
            } else {
                socketWriter.println("---------------------------------------");
                socketWriter.println("TV turned off. Press 1 to turn TV on.");
                socketWriter.println("---------------------------------------");
                socketWriter.println("END_OF_MESSAGE");
            }
        } catch (Exception e) {
            System.err.println("Error sending info to writer: " + e.getMessage());
        }

    }
}


