package remote.thread;

import main.Main;
import remote.DataAndTools;
import remote.socket.StatusSocket;

import java.net.ServerSocket;
import java.net.Socket;

public class StatusServerThread extends Thread {
    public void run() {
        ServerSocket serverSocket;
        StatusSocket statusSocket;

        DataAndTools.printLineWithTime(getClass().getSimpleName()+" ===> Running!");

        try {
            serverSocket = new ServerSocket(DataAndTools.statusPort);
            while (true) {
                DataAndTools.printLineWithTime(getClass().getSimpleName()+" ===> Waiting for connections!");
                statusSocket = new StatusSocket(serverSocket.accept());
                Main.logger.info(getClass().getSimpleName()+" ===> "+statusSocket.getIP()+" connected!");
                statusSocket.send(DataAndTools.createStatusString());
                DataAndTools.statusSockets.add(statusSocket);
            }
        } catch (Exception e) {
            if(DataAndTools.DEBUG_FLAG) e.printStackTrace();
        }
    }
}