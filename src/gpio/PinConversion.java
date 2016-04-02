package gpio;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import java.util.HashMap;

public class PinConversion {
    private HashMap<Integer, Pin> gpioConversion;



    public Pin getWiringPiPinNumber(int hardwareGpioNumber) {
        return gpioConversion.get(hardwareGpioNumber);
    }
}
