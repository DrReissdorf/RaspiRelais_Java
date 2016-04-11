package remote.thread;

import gpio.GPIO;
import main.Main;
import remote.DataAndTools;
import remote.socket.ControlSocket;
import remote.entity.Relais;
import remote.socket.SocketComm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ControlHandleThread extends Thread {
    private ControlSocket controlSocket;
    private GPIO gpio;

    public ControlHandleThread(ControlSocket controlSocket) {
        this.controlSocket = controlSocket;
        if(DataAndTools.ENABLE_GPIO) this.gpio = new GPIO();
    }

    public void run() {
        String received;
        boolean cancel = false;

        DataAndTools.printLineWithTime("Connection established to " + controlSocket.getIP() + "!");
        DataAndTools.printLineWithTime("Connected clients: " + DataAndTools.socketHashMap.size());

        while (!cancel) {
            try {
                received = controlSocket.receive();

                DataAndTools.printLineWithTime("Received from "+ controlSocket.getIP()+": " + received);

                for (Relais r : DataAndTools.relaisArrayList) {
                    if (received.equals(r.getName())) {
                        if(gpio != null) gpio.setOutputPin(r.getGPIO_OUTPUT(),!r.getGPIO_OUTPUT().isHigh());
                        DataAndTools.notifyStatusChange();
                        break;
                    }
                }
            } catch (Exception e) {
                cancel = true;
                if(DataAndTools.DEBUG_FLAG) e.printStackTrace();
                DataAndTools.socketHashMap.remove(controlSocket);
                Main.logger.info(getClass().getSimpleName()+" ===> Lost socket connection with " + controlSocket.getIP() + "!");
                Main.logger.info(getClass().getSimpleName()+" ===> Connected clients: " + DataAndTools.socketHashMap.size());
                DataAndTools.printLineWithTime("Lost socket connection with " + controlSocket.getIP() + "!");
                DataAndTools.printLineWithTime("Connected clients: " + DataAndTools.socketHashMap.size());
            }

        }
    }
}