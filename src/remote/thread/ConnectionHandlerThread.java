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
    private final int SEND_TEMPS_SLEEP = 1000;

    public ConnectionHandlerThread(SocketComm socketComm) {
        this.socketComm = socketComm;
        this.gpio = new GPIO();
        serverInstance = ServerSingleton.getInstance();
    }

    public void run() {
        String received;
        boolean cancel = false;
        String[] command;

        SendTempThread sendTempThread = new SendTempThread();
        sendTempThread.start();

        DataAndTools.printLineWithTime("Connection established to " + socketComm.getIP() + "!");
        DataAndTools.printLineWithTime("Connected clients: " + serverInstance.getNumberOfConnectedClients());

        /****** Send current Status to client, for initialization ******/
        socketComm.send(DataAndTools.createStatusString());

        while (!cancel) {
            try {
                received = socketComm.receive();
                command = received.split("%");

                switch(command[0]) {
                    case DataAndTools.PROTOCOL_RELAY:
                        for (Relais r : DataAndTools.relaisArrayList) {
                            if (command[1].equals(r.getName())) {
                                if(gpio != null) gpio.setOutputPin(r.getGPIO_OUTPUT(),!r.getGPIO_OUTPUT().isHigh());
                                serverInstance.notifyGpioChange();
                                break;
                            }
                        }
                        break;

                    case DataAndTools.PROTOCOL_TEMP:
                        socketComm.send(DataAndTools.createTemperatureString());
                        break;
                }

                DataAndTools.printLineWithTime("Received from "+ socketComm.getIP()+": " + received);
            } catch (Exception e) {
                cancel = true;
                sendTempThread.exit();
                if(DataAndTools.DEBUG_FLAG) e.printStackTrace();
                serverInstance.removeSocketComm(socketComm);
                Main.logger.info(getClass().getSimpleName()+" ===> Lost socket connection with " + socketComm.getIP() + "!");
                Main.logger.info(getClass().getSimpleName()+" ===> Connected clients: " + serverInstance.getNumberOfConnectedClients());
                DataAndTools.printLineWithTime("Lost socket connection with " + socketComm.getIP() + "!");
                DataAndTools.printLineWithTime("Connected clients: " + serverInstance.getNumberOfConnectedClients());
            }

        }
    }

    private class SendTempThread extends Thread {
        private boolean exit = false;

        public void run() {
            while(!exit) {
                socketComm.send(DataAndTools.createTemperatureString());
                try {
                    sleep(SEND_TEMPS_SLEEP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void exit() {
            exit = true;
        }
    }
}
