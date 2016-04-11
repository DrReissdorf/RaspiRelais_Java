package gpio;

import java.io.*;
import java.util.ArrayList;

public class GpioSetup {
    public ArrayList<String[]> readConfig(String path) throws IOException {
        File configFile = new File(path);
        String temp;
        ArrayList<String[]> strings = new ArrayList<>();

        if(!configFile.exists()) {
            try {
                configFile.createNewFile();
                PrintWriter pw = new PrintWriter(new FileWriter(configFile));
                pw.println("//relais name;output gpio number;input gpio number");
                pw.println("//example:");
                pw.println("//light;21;20");
                pw.flush();
                pw.close();

                System.out.println("No GPIO-config detected. I created it.");
                System.out.println("Please open 'gpio-setup.txt' and insert used GPIO-Pins.");
                System.out.println("Shutting down...");
                System.exit(-1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedReader br = new BufferedReader(new FileReader(configFile));

        while((temp = br.readLine()) != null) {
            if(!temp.startsWith("//")) {
                strings.add(temp.split(";"));
            }
        }

        for(String[] stringArray : strings) {
            System.out.println("GPIO from config: "+stringArray[0]+";"+stringArray[1]+";"+stringArray[2]);
        }

        return strings;
    }
}
