package remote.thread;

import main.Main;
import remote.DataAndTools;
import remote.socket.ControlSocket;
import remote.socket.StatusSocket;

import java.net.ServerSocket;

public class AcceptSocketsThread extends Thread {
    public void run() {
        ServerSocket controlServerSocket;
        ControlSocket controlSocket;

        ServerSocket statusServerSocket;
        StatusSocket statusSocket;

        DataAndTools.printLineWithTime(getClass().getSimpleName()+" ===> Running!");

        try {
            controlServerSocket = new ServerSocket(DataAndTools.controlPort);
            statusServerSocket = new ServerSocket(DataAndTools.statusPort);
            while (true) {
                DataAndTools.printLineWithTime(getClass().getSimpleName()+" ===> Waiting for connections!");
                controlSocket = new ControlSocket(controlServerSocket.accept());
                statusSocket = new StatusSocket(statusServerSocket.accept());
                statusSocket.send(DataAndTools.createStatusString());
                Main.logger.info(getClass().getSimpleName()+" ===> "+controlSocket.getIP()+" connected!");
                DataAndTools.socketHashMap.put(controlSocket,statusSocket);
                new ControlHandleThread(controlSocket).start();
            }
        }
        catch (Exception e){
            if(DataAndTools.DEBUG_FLAG) e.printStackTrace();
        }
    }
}