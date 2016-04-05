package gpio;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class WiringPi_To_HardWare_GPIO {
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
