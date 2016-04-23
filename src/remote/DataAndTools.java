package remote;

import remote.entity.Relais;
import remote.socket.SocketComm;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class DataAndTools {
    public static int controlPort = 18745;
    public static int udpPort = 18744;

    public static ArrayList<Relais> relaisArrayList = new ArrayList<>();
    public static boolean DEBUG_FLAG = false;
    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
    public static String workingDirectory;

    public static final String PROTOCOL_RELAY = "relay";
    public static final String PROTOCOL_TEMP = "temp";

    public static void printLineWithTime(String text) {
        System.out.println(ZonedDateTime.now().format(format) + " -- "+text);
    }

    public static String createStatusString() {
        String toSend = PROTOCOL_RELAY+"%";

        for(Relais r : DataAndTools.relaisArrayList) {
            toSend += r.getName()+","+r.getGPIO_OUTPUT().isHigh()+";";
        }

        toSend = toSend.substring(0,toSend.length()-1);

        return toSend;
    }


}
