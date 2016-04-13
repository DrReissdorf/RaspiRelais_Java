package main;

import com.pi4j.io.gpio.*;
import gpio.GPIO;
import remote.DataAndTools;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    public static Logger logger;
    public static FileHandler fh;

    public static void main(String args[]) throws InterruptedException {
        CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();
        File jarFile = null;
        try {
            jarFile = new File(codeSource.getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        DataAndTools.workingDirectory = jarFile.getParentFile().getPath();

        /****** LOGGING ******/
        logger = Logger.getLogger("MyLog");
        try {
            fh = new FileHandler(DataAndTools.workingDirectory+"/relais_java.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fh);
        fh.setFormatter(new SimpleFormatter());
        /**********************/

        GPIO gpio = new GPIO();
        gpio.initGpioPins();

        createShutDownHook();

        ServerSingleton.getInstance().startServer();
    }

    private static void createShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            GpioFactory.getInstance().shutdown();
            System.out.println("\nThanks for using the application");
            System.out.println("Exiting...");
        }));
    }
}