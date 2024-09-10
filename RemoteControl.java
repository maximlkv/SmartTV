package Lab1;

import java.net.Socket;
import java.io.IOException;

public class RemoteControl {

    private static String SERVER_HOST = null;
    private static int TCP_PORT = 0;

    private Socket socket;

    public static void main(String[] args) {
        for (int i = 0; i < args.length; ++i) {
            System.out.println(args[i]+"\n");
        } 
        if (args.length == 2) {
            SERVER_HOST = args[0];
            TCP_PORT = Integer.valueOf(args[1]);
        } else {
            System.err.println("Please provide hostname and Port number as command line parameters, in order to connect to a SmartTV.");
        }
        RemoteControl remoteControl = new RemoteControl();
        remoteControl.run();
    }
    
    private void run() {
        if (connect()) { 
            try {
                Thread.sleep(20000);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        
        disconnect();
    }

    /**
     * Close TCP connection
     */
    private void disconnect() {
        try {
            if (socket != null) {
                socket.close();
                System.out.println("Socket closed");
            } else {
                System.err.println("Cannot close socket which has not been initialized.");
          }
        } catch (IOException e) {
            System.err.println("Could not close the socket: " + e.getMessage());
        }
    }

    private boolean connect() {
        boolean success = false;
        try {
            socket = new Socket(SERVER_HOST, TCP_PORT);
            System.out.println("Connection established");
            success = true;
        } catch (IOException e) {
            System.err.println("Could not connect to the server: " + e.getMessage());
        }

        return success;
    }

}
