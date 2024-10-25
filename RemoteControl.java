import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class RemoteControl {

    private static String SERVER_HOST = null;
    private static int TCP_PORT = 1238;

    private Socket socket;
    private Scanner remoteInputs;
    private PrintWriter socketWriter;
    private BufferedReader socketReader;

    public static void main(String[] args) {
        if (args.length == 2) {
            SERVER_HOST = args[0];
            TCP_PORT = Integer.valueOf(args[1]);
        } else {
            System.err.println("Please provide (only) hostname and Port number as command line parameters, in order to connect to a SmartTV.");
        }
        RemoteControl remoteControl = new RemoteControl();
        remoteControl.run();
    }
    
    private void run() {
        if (connect()) { 
            try {
                String buttonNumber;
                do {
                    buttonNumber = remoteInputs.next();
                    socketWriter.println(buttonNumber);
                    getServerMessage();
                } while (true); 
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        disconnect();
    }

    private boolean connect() {
        boolean success = false;
        try {
            socket = new Socket(SERVER_HOST, TCP_PORT);
            remoteInputs = new Scanner(System.in);
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketWriter = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connection established");
            success = true;
            getServerMessage();
        } catch (IOException e) {
            System.err.println("Could not connect to the server: " + e.getMessage());
        }

        return success;
    }

    private void getServerMessage() {
        String serverResponse;
        while ((serverResponse = reiceiveOneLineFromServer()) != null) {
            if (serverResponse.equals("END_OF_MESSAGE")) {
                break;
            }
            System.out.println(serverResponse);
        }
    }

    private String reiceiveOneLineFromServer() {
        String response = null;
        try {
            response = socketReader.readLine();
        } catch (IOException e) {
            System.err.println("Error receiving data from the server: " + e.getMessage());       
        }        
        return response;
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

    

    

}
