package remote;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import remote.entity.Relais;
import remote.socket.ControlSocket;
import remote.socket.SocketComm;
import remote.socket.StatusSocket;

import java.net.Socket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DataAndTools {
    public static int controlPort = 18745;
    public static int statusPort = 18744;
    public static int udpPort = 18746;
    public static ArrayList<ControlSocket> controlSockets = new ArrayList<>();
    public static ArrayList<StatusSocket> statusSockets = new ArrayList<>();
    public static ArrayList<Relais> relaisArrayList = new ArrayList<>();
    public static boolean DEBUG_FLAG = false;
    public static boolean ENABLE_GPIO = true;
    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");

    public static void printLineWithTime(String text) {
        System.out.println(ZonedDateTime.now().format(format) + " -- "+text);
    }

    public static String createStatusString() {
        String toSend = "";

        for(Relais r : DataAndTools.relaisArrayList) {
            toSend += r.getName()+","+r.getGPIO_OUTPUT().isHigh()+";";
        }

        toSend = toSend.substring(0,toSend.length()-1);

        return toSend;
    }

    public static void notifyStatusChange() {
        for (StatusSocket statusSocket : DataAndTools.statusSockets) {
            statusSocket.send(createStatusString());
        }
    }
}
