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

    public static Pin getPin(int hardwareGpioNumber) {
        switch (hardwareGpioNumber) {
            case 3: return RaspiPin.GPIO_08;
            case 5: return RaspiPin.GPIO_09;
            case 7: return RaspiPin.GPIO_07;
            case 8: return RaspiPin.GPIO_15;
            case 10: return RaspiPin.GPIO_16;
            case 11: return RaspiPin.GPIO_00;
            case 12: return RaspiPin.GPIO_01;
            case 15: return RaspiPin.GPIO_03;
            case 16: return RaspiPin.GPIO_04;
            case 18: return RaspiPin.GPIO_05;
            case 19: return RaspiPin.GPIO_12;
            case 21: return RaspiPin.GPIO_13;
            case 22: return RaspiPin.GPIO_06;
            case 23: return RaspiPin.GPIO_14;
            case 24: return RaspiPin.GPIO_10;
            case 26: return RaspiPin.GPIO_11;
            case 29: return RaspiPin.GPIO_21;
            case 31: return RaspiPin.GPIO_22;
            case 32: return RaspiPin.GPIO_26;
            case 33: return RaspiPin.GPIO_23;
            case 35: return RaspiPin.GPIO_24;
            case 36: return RaspiPin.GPIO_27;
            case 37: return RaspiPin.GPIO_25;
            case 38: return RaspiPin.GPIO_28;
            case 40: return RaspiPin.GPIO_29;
            default: return null;
        }
    }
}
