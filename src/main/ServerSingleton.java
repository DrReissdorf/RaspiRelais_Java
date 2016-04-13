package main;

import remote.DataAndTools;
import remote.socket.SocketComm;
import remote.thread.AcceptConnectionsThread;
import remote.thread.UdpServerThread;
import java.util.ArrayList;

public class ServerSingleton {
    private static ServerSingleton serverInstance;
    private ArrayList<SocketComm> socketComms;

    private ServerSingleton() {
        socketComms = new ArrayList<>();
    }

    public static ServerSingleton getInstance() {
        if(serverInstance == null) serverInstance = new ServerSingleton();
        return serverInstance;
    }

    public void startServer() throws InterruptedException {
        UdpServerThread udpServerThread = new UdpServerThread();
        AcceptConnectionsThread acceptConnectionsThread = new AcceptConnectionsThread();

        udpServerThread.start();
        acceptConnectionsThread.start();

        udpServerThread.join();
        acceptConnectionsThread.join();
    }

    public void notifyStatusChange() {
        for (SocketComm socketComm : socketComms) {
            socketComm.send(DataAndTools.createStatusString());
        }
    }

    public void removeSocketComm(SocketComm socketComm) {
        socketComms.remove(socketComm);
    }

    public void addSocketComm(SocketComm socketComm) {
        socketComms.add(socketComm);
    }

    public int getNumberOfConnectedClients() {
        return socketComms.size();
    }
}
