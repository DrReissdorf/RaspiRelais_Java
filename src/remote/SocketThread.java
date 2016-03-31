package remote;

import gpio.GPIO;
import main.Main;

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
        if(Data.ENABLE_GPIO) this.gpio = new GPIO();
        try {
            this.controlPrintWriter = new PrintWriter(socket.getOutputStream());
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            if(Data.DEBUG_FLAG) e.printStackTrace();
        }
        run();
    }

    public void run() {
        String received;
        boolean cancel = false;

        Main.printLineWithTime("Connection established to " + socket.getInetAddress() + "!");
        Main.printLineWithTime("Connected clients: " + Data.controlSockets.size());

        while (!cancel) {
            try {
                received = bufferedReader.readLine();

                Main.printLineWithTime("Received from "+socket.getInetAddress()+": " + received);

                if (received.equals("info")) {
                    controlPrintWriter.println(Main.createStatusString() + '\n');
                    controlPrintWriter.flush();
                } else {
                    for (Relais r : Data.relaisArrayList) {
                        if (received.equals(r.getName())) {
                            r.setEnabled(!r.isEnabled());
                            if(gpio != null) gpio.output(r.getGPIO_OUTPUT(),r.isEnabled());
                            Main.notifyStatusChange();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                cancel = true;
                if(Data.DEBUG_FLAG) e.printStackTrace();
                Data.controlSockets.remove(socket);
                try {
                    socket.close();
                } catch (IOException e1) {
                    if(Data.DEBUG_FLAG) e1.printStackTrace();
                }
                Main.printLineWithTime("Lost socket connection with " + socket.getInetAddress().getHostAddress() + "!");
                Main.printLineWithTime("Connected clients: " + Data.controlSockets.size());
            }

        }
    }





}
