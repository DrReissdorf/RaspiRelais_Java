package remote.thread;

import main.Main;
import remote.DataAndTools;
import main.ServerSingleton;
import remote.socket.SocketComm;
import java.net.ServerSocket;

public class AcceptConnectionsThread extends Thread {
    public void run() {
        ServerSocket serverSocket;
        SocketComm socketComm;

        DataAndTools.printLineWithTime(getClass().getSimpleName()+" ===> Running!");

        try {
            serverSocket = new ServerSocket(DataAndTools.controlPort);

            while (true) {
                DataAndTools.printLineWithTime(getClass().getSimpleName()+" ===> Waiting for connections!");
                socketComm = new SocketComm(serverSocket.accept());
                Main.logger.info(getClass().getSimpleName()+" ===> "+socketComm.getIP()+" connected!");
                ServerSingleton.getInstance().addSocketComm(socketComm);
                new ConnectionHandlerThread(socketComm).start();
            }
        }
        catch (Exception e){
            if(DataAndTools.DEBUG_FLAG) e.printStackTrace();
        }
    }
}