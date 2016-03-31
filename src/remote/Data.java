package remote;

import java.net.Socket;
import java.util.ArrayList;

public class Data {
    public static int controlPort = 18745;
    public static int statusPort = 18744;

    public static ArrayList<Socket> controlSockets = new ArrayList<>();
    public static ArrayList<Socket> statusSockets = new ArrayList<>();

    public static ArrayList<Relais> relaisArrayList = new ArrayList<>();
    public static boolean DEBUG_FLAG = true;
    public static boolean ENABLE_GPIO = false;
}
