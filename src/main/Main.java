package main;

import com.pi4j.io.gpio.*;
import gpio.GPIO;
import remote.DataAndTools;
import remote.entity.Relais;
import remote.thread.ControlServerThread;
import remote.thread.StatusServerThread;

public class Main {
    public static void main(String args[]) throws InterruptedException {
        GPIO gpio = new GPIO();
        gpio.initGpioPins();

        createShutDownHook();

        ControlServerThread controlServerThread = new ControlServerThread();
        StatusServerThread statusServerThread = new StatusServerThread();

        controlServerThread.start();
        statusServerThread.start();

        controlServerThread.join();
        statusServerThread.join();
    }

    private static void createShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            GpioFactory.getInstance().shutdown();
            System.out.println("\nThanks for using the application");
            System.out.println("Exiting...");

        }));
    }


}
