package remote.thread;

import main.Main;
import remote.DataAndTools;

import java.net.ServerSocket;
import java.net.Socket;

public class ControlServerThread extends Thread {
    public void run() {
        ServerSocket serverSocket;
        Socket socket;

        DataAndTools.printLineWithTime(getClass().getSimpleName()+" ===> Running!");

        try {
            serverSocket = new ServerSocket(DataAndTools.controlPort);
            while (true) {
                DataAndTools.printLineWithTime(getClass().getSimpleName()+" ===> Waiting for connections!");
                socket = serverSocket.accept();
                DataAndTools.controlSockets.add(socket);
                new SocketThread(socket).start();
            }
        }
        catch (Exception e){
            if(DataAndTools.DEBUG_FLAG) e.printStackTrace();
        }

    }
}