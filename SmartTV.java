package Lab1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SmartTV {

    public static int TCP_PORT = 1238;
    public static final int NUMBER_OF_CHANNELS = 20;
    private boolean isOn = false;
    private int activeChannel = 1;

    private ServerSocket serverSocket; 
    private boolean running; 

    public static void main(String[] args) {
        if (args.length == 1) {
            TCP_PORT = Integer.valueOf(args[0]);
        }
        SmartTV smartTV = new SmartTV();
        smartTV.run();            
    }

    private void run() {
        if (openListeningSocket()) {
            running = true;
            while (running) {
                Socket clientSocket = acceptNextClient();
                RemoteHandler remoteHandler = new RemoteHandler(this, clientSocket);
                remoteHandler.run();
            }    
        }
        
        System.out.println("Server exiting...");
    }

    /**
     * Open a listening TCP socket
     * @return true on success, false on error.
     */
    private boolean openListeningSocket() {
        boolean success = false;
        try {
            serverSocket = new ServerSocket(TCP_PORT);
            System.out.println("Server listening on port " + TCP_PORT);
            success = true;
        } catch (IOException e) {
            System.err.println("Could not open a listening socket on port " + TCP_PORT +
                 ", reason: "+ e.getMessage());
        }
        return success;
    }

    private Socket acceptNextClient() {
        Socket clientSocket = null;
        try {  
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Could not accept the next client: " + e.getMessage());
        }
        return clientSocket;
    }

    private int getActiveChannel() {
        return activeChannel;
    }

    private void setActiveChannel(int channel) {
        activeChannel = channel;
    }
}
