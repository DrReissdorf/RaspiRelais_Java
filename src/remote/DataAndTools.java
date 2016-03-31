package remote;

import main.Main;
import remote.entity.Relais;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DataAndTools {
    public static int controlPort = 18745;
    public static int statusPort = 18744;
    public static ArrayList<Socket> controlSockets = new ArrayList<>();
    public static ArrayList<Socket> statusSockets = new ArrayList<>();
    public static ArrayList<Relais> relaisArrayList = new ArrayList<>();
    public static boolean DEBUG_FLAG = true;
    public static boolean ENABLE_GPIO = false;
    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");

    public static void printLineWithTime(String text) {
        System.out.println(ZonedDateTime.now().format(format) + " -- "+text);
    }

    public static String createStatusString() {
        String toSend = "";

        for(Relais r : DataAndTools.relaisArrayList) {
            toSend += r.getName()+","+r.isEnabled()+";";
        }

        toSend = toSend.substring(0,toSend.length()-1);

        return toSend;
    }

    public static void notifyStatusChange() {
        PrintWriter printWriter;

        for (Socket socket : DataAndTools.statusSockets) {
            try {
                printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.println(createStatusString());
                printWriter.flush();
            } catch (IOException e) {
                if(DataAndTools.DEBUG_FLAG) e.printStackTrace();
                DataAndTools.statusSockets.remove(socket);
                try {
                    socket.close();
                } catch (IOException e1) {
                    if(DataAndTools.DEBUG_FLAG) e1.printStackTrace();
                }
            }
        }
    }
}
