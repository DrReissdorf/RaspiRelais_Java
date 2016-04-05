package main;

import com.pi4j.io.gpio.*;
import gpio.GPIO;
import remote.thread.ControlServerThread;
import remote.thread.StatusServerThread;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    public static Logger logger;
    public static FileHandler fh;

    public static void main(String args[]) throws InterruptedException {
        /****** LOGGING ******/
        logger = Logger.getLogger("MyLog");
        try {
            fh = new FileHandler("relais_java.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fh);
        fh.setFormatter(new SimpleFormatter());
        /**********************/

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
