package remote.thread;

import main.Main;
import remote.DataAndTools;
import remote.socket.ControlSocket;

import java.net.ServerSocket;
import java.net.Socket;

public class ControlServerThread extends Thread {
    public void run() {
        ServerSocket serverSocket;
        ControlSocket controlSocket;

        DataAndTools.printLineWithTime(getClass().getSimpleName()+" ===> Running!");

        try {
            serverSocket = new ServerSocket(DataAndTools.controlPort);
            while (true) {
                DataAndTools.printLineWithTime(getClass().getSimpleName()+" ===> Waiting for connections!");
                controlSocket = new ControlSocket(serverSocket.accept());
                Main.logger.info(getClass().getSimpleName()+" ===> "+controlSocket.getIP()+" connected!");
                DataAndTools.controlSockets.add(controlSocket);
                new SocketThread(controlSocket).start();
            }
        }
        catch (Exception e){
            if(DataAndTools.DEBUG_FLAG) e.printStackTrace();
        }

    }
}