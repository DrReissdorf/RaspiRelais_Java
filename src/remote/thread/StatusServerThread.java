package remote.thread;

import main.Main;
import remote.DataAndTools;

import java.net.ServerSocket;
import java.net.Socket;

public class StatusServerThread extends Thread {
    public void run() {
        ServerSocket serverSocket;
        Socket socket;

        DataAndTools.printLineWithTime(getClass().getSimpleName()+" ===> Running!");

        try {
            serverSocket = new ServerSocket(DataAndTools.statusPort);
            while (true) {
                DataAndTools.printLineWithTime(getClass().getSimpleName()+" ===> Waiting for connections!");
                socket = serverSocket.accept();
                DataAndTools.statusSockets.add(socket);
            }
        } catch (Exception e) {
            if(DataAndTools.DEBUG_FLAG) e.printStackTrace();
        }
    }
}