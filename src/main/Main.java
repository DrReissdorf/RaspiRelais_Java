package main;

import remote.ControlServerThread;
import remote.Data;
import remote.Relais;
import remote.StatusServerThread;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");

    public static void main(String args[]) throws InterruptedException {
        /* FILL RELAIS ARRAYLIST */
        Data.relaisArrayList.add(new Relais("Monitor Backlight",1,1,false));
        Data.relaisArrayList.add(new Relais("Relais 2",2,2,false));
        Data.relaisArrayList.add(new Relais("Relais 3",3,3,false));
        Data.relaisArrayList.add(new Relais("Relais 4",4,4,false));


        ControlServerThread controlServerThread = new ControlServerThread();
        StatusServerThread statusServerThread = new StatusServerThread();

        controlServerThread.start();
        statusServerThread.start();

        controlServerThread.join();
        statusServerThread.join();
    }

    public static void printLineWithTime(String text) {
        System.out.println(ZonedDateTime.now().format(format) + " -- "+text);
    }

    public static String createStatusString() {
        String toSend = "";

        for(Relais r : Data.relaisArrayList) {
            toSend += r.getName()+","+r.isEnabled()+";";
        }

        toSend = toSend.substring(0,toSend.length()-1);

        return toSend;
    }

    public static void notifyStatusChange() {
        PrintWriter printWriter;

        for (Socket socket : Data.statusSockets) {
            try {
                printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.println(Main.createStatusString());
                printWriter.flush();
            } catch (IOException e) {
                if(Data.DEBUG_FLAG) e.printStackTrace();
                Data.statusSockets.remove(socket);
                try {
                    socket.close();
                } catch (IOException e1) {
                    if(Data.DEBUG_FLAG) e1.printStackTrace();
                }
            }
        }
    }
}
