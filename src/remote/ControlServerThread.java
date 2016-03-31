package remote;

import main.Main;

import java.net.ServerSocket;
import java.net.Socket;

public class ControlServerThread extends Thread {
    public void run() {
        ServerSocket serverSocket;
        Socket socket;

        Main.printLineWithTime(getClass().getSimpleName()+" ===> Running!");

        try {
            serverSocket = new ServerSocket(Data.controlPort);
            while (true) {
                Main.printLineWithTime(getClass().getSimpleName()+" ===> Waiting for connections!");
                socket = serverSocket.accept();
                Data.controlSockets.add(socket);
                new SocketThread(socket).start();
            }
        }
        catch (Exception e){
            if(Data.DEBUG_FLAG) e.printStackTrace();
        }

    }
}