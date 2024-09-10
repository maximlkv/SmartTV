package Lab1;

import java.net.Socket;

public class RemoteHandler {
    private SmartTV smartTV;
    private Socket clientSocket;

    public RemoteHandler(SmartTV smartTV, Socket clientSocket) {
        this.smartTV = smartTV;
        this.clientSocket = clientSocket;
        System.out.println("Client connected. Address: " + clientSocket.getRemoteSocketAddress() 
                + ", Port: " + clientSocket.getPort());
    }

    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

}


