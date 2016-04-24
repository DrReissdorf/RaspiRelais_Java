package temperature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TempReader {
    public static ArrayList<String> readTemps(File file) throws IOException {
        ArrayList<String> strings = new ArrayList<>();
        String toadd = "";
        String temporary;


        BufferedReader br = new BufferedReader(new FileReader(file));

        while((temporary = br.readLine()) != null) {
            toadd += temporary;
        }

        if(toadd.contains("NO")) {
            System.out.println("No Temperature-Sensors installed!");
            return strings;
        }

        int index = 0;
        while(index != -1) {
            index = toadd.indexOf('t');
            if(index != -1) {
                strings.add(toadd.substring(index+2,index+7));
                toadd = toadd.substring(index+3);
            }
        }

        //System.out.println(strings.size()+" Temperature-Sensors installed! Returning ArrayList of values...");
        return strings;
    }
}
