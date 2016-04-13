package remote.thread;

import gpio.GPIO;
import main.Main;
import remote.DataAndTools;
import remote.entity.Relais;
import main.ServerSingleton;
import remote.socket.SocketComm;

public class ConnectionHandlerThread extends Thread {
    private SocketComm socketComm;
    private GPIO gpio;
    private ServerSingleton serverInstance;

    public ConnectionHandlerThread(SocketComm socketComm) {
        this.socketComm = socketComm;
        this.gpio = new GPIO();
        serverInstance = ServerSingleton.getInstance();
    }

    public void run() {
        String received;
        boolean cancel = false;

        DataAndTools.printLineWithTime("Connection established to " + socketComm.getIP() + "!");
        DataAndTools.printLineWithTime("Connected clients: " + serverInstance.getNumberOfConnectedClients());

        /****** Send current Status to client, for initialization ******/
        socketComm.send(DataAndTools.createStatusString());


        while (!cancel) {
            try {
                received = socketComm.receive();

                DataAndTools.printLineWithTime("Received from "+ socketComm.getIP()+": " + received);

                for (Relais r : DataAndTools.relaisArrayList) {
                    if (received.equals(r.getName())) {
                        if(gpio != null) gpio.setOutputPin(r.getGPIO_OUTPUT(),!r.getGPIO_OUTPUT().isHigh());
                        serverInstance.notifyStatusChange();
                        break;
                    }
                }
            } catch (Exception e) {
                cancel = true;
                if(DataAndTools.DEBUG_FLAG) e.printStackTrace();
                serverInstance.removeSocketComm(socketComm);
                Main.logger.info(getClass().getSimpleName()+" ===> Lost socket connection with " + socketComm.getIP() + "!");
                Main.logger.info(getClass().getSimpleName()+" ===> Connected clients: " + serverInstance.getNumberOfConnectedClients());
                DataAndTools.printLineWithTime("Lost socket connection with " + socketComm.getIP() + "!");
                DataAndTools.printLineWithTime("Connected clients: " + serverInstance.getNumberOfConnectedClients());
            }

        }
    }
}
