package remote.thread;

import gpio.GPIO;
import main.Main;
import remote.DataAndTools;
import remote.entity.Relais;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketThread extends Thread {
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter controlPrintWriter;
    private GPIO gpio;

    public SocketThread(Socket socket) {
        this.socket = socket;
        if(DataAndTools.ENABLE_GPIO) this.gpio = new GPIO();
        try {
            this.controlPrintWriter = new PrintWriter(socket.getOutputStream());
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            if(DataAndTools.DEBUG_FLAG) e.printStackTrace();
        }
    }

    public void run() {
        String received;
        boolean cancel = false;

        DataAndTools.printLineWithTime("Connection established to " + socket.getInetAddress() + "!");
        DataAndTools.printLineWithTime("Connected clients: " + DataAndTools.controlSockets.size());

        while (!cancel) {
            try {
                received = bufferedReader.readLine();

                DataAndTools.printLineWithTime("Received from "+socket.getInetAddress()+": " + received);

                if (received.equals("info")) {
                    controlPrintWriter.println(DataAndTools.createStatusString() + '\n');
                    controlPrintWriter.flush();
                } else {
                    for (Relais r : DataAndTools.relaisArrayList) {
                        if (received.equals(r.getName())) {
                            r.setEnabled(!r.isEnabled());
                            if(gpio != null) gpio.output(r.getGPIO_OUTPUT(),r.isEnabled());
                            DataAndTools.notifyStatusChange();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                cancel = true;
                if(DataAndTools.DEBUG_FLAG) e.printStackTrace();
                DataAndTools.controlSockets.remove(socket);
                try {
                    socket.close();
                } catch (IOException e1) {
                    if(DataAndTools.DEBUG_FLAG) e1.printStackTrace();
                }
                DataAndTools.printLineWithTime("Lost socket connection with " + socket.getInetAddress().getHostAddress() + "!");
                DataAndTools.printLineWithTime("Connected clients: " + DataAndTools.controlSockets.size());
            }

        }
    }





}
