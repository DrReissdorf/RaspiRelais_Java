package remote;

import main.Main;

import java.net.ServerSocket;
import java.net.Socket;

public class StatusServerThread extends Thread {
    public void run() {
        ServerSocket serverSocket;
        Socket socket;

        Main.printLineWithTime(getClass().getSimpleName()+" ===> Running!");

        while (true) {
            try {
                serverSocket = new ServerSocket(Data.statusPort);
                while (true) {
                    Main.printLineWithTime(getClass().getSimpleName()+" ===> Waiting for connections!");
                    socket = serverSocket.accept();
                    Data.statusSockets.add(socket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}