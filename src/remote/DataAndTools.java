package remote;

import remote.entity.Relais;
import remote.socket.SocketComm;
import temperature.TempReader;

import java.io.File;
import java.io.IOException;
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

    private static final String temperatureFilePath = "/sys/bus/w1/devices/28-800000036b22/w1_slave";

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

    public static String createTemperatureString() {
        String toSend = DataAndTools.PROTOCOL_TEMP+"%";

        ArrayList<String> strings = null;
        try {
            strings = TempReader.readTemps(new File(temperatureFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0 ; i<strings.size() ; i++) {
            if(i < strings.size()-1) toSend += strings.get(i)+";";
            else toSend += strings.get(i);
        }

        return toSend;
    }
}
