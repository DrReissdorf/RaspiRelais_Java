package main;

import remote.DataAndTools;
import remote.entity.Relais;
import remote.thread.ControlServerThread;
import remote.thread.StatusServerThread;

public class Main {
    public static void main(String args[]) throws InterruptedException {
        /* FILL RELAIS ARRAYLIST */
        DataAndTools.relaisArrayList.add(new Relais("Monitor Backlight",1,1,false));
        DataAndTools.relaisArrayList.add(new Relais("Relais 2",2,2,false));
        DataAndTools.relaisArrayList.add(new Relais("Relais 3",3,3,false));
        DataAndTools.relaisArrayList.add(new Relais("Relais 4",4,4,false));

        ControlServerThread controlServerThread = new ControlServerThread();
        StatusServerThread statusServerThread = new StatusServerThread();

        controlServerThread.start();
        statusServerThread.start();

        controlServerThread.join();
        statusServerThread.join();
    }
}
