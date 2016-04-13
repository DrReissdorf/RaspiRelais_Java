package gpio;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import main.Main;
import remote.DataAndTools;
import remote.entity.Relais;
import main.ServerSingleton;
import java.io.IOException;
import java.util.ArrayList;

import static gpio.WiringPi_To_HardWare_GPIO.getPin;

public class GPIO {
    private long timeout;
    private final int bouncetime = 200;
    private GpioController gpioController;

    public GPIO() {
        gpioController = GpioFactory.getInstance();
    }

    public void initGpioPins() {

        /****** READ GPIO PINS FROM CONFIG FILE **********/
        ArrayList<String[]> gpioInitStrings = null;
        try {
            gpioInitStrings = new GpioSetup().readConfig(DataAndTools.workingDirectory+"/gpio-setup.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**************************************************/

        for(String[] stringArray : gpioInitStrings) {
            DataAndTools.relaisArrayList.add(new Relais(
                    stringArray[0],
                    createOutputPin(getPin(Integer.valueOf(stringArray[1])),false),
                    createInputPin(getPin(Integer.valueOf(stringArray[2])))));
        }

        for(Relais relais : DataAndTools.relaisArrayList) {
            /* LISTENER FOR HARDWARE BUTTONS */
            relais.getGPIO_INPUT().addListener((GpioPinListenerDigital) event -> {
                if(event.getState()==PinState.HIGH && (System.currentTimeMillis()-timeout)>bouncetime) {
                    timeout = System.currentTimeMillis();
                    Main.logger.info(" --> INTERRUPT: " + event.getPin() + " STATE: "+event.getState());
                    GpioPinDigitalOutput pin = getOutputPin(event.getPin().getName());
                    setOutputPin(pin, !pin.isHigh());
                    ServerSingleton.getInstance().notifyStatusChange();
                }
            });
        }

    }

    private GpioPinDigitalOutput getOutputPin(String name) {
        for(Relais r : DataAndTools.relaisArrayList) {
            if (name.equals(r.getGPIO_INPUT().getPin().getName())) {
                return r.getGPIO_OUTPUT();
            }
        }
        return null;
    }

    public GpioPinDigitalOutput createOutputPin(Pin pin, boolean enable) {
        GpioPinDigitalOutput gpioOut;
        if(enable) {
            gpioOut = gpioController.provisionDigitalOutputPin(pin, PinState.HIGH);
        } else {
            gpioOut = gpioController.provisionDigitalOutputPin(pin, PinState.LOW);
        }
        gpioOut.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        return gpioOut;
    }

    public GpioPinDigitalInput createInputPin(Pin pin) {
        GpioPinDigitalInput gpioIn = gpioController.provisionDigitalInputPin(pin, PinPullResistance.PULL_DOWN);
        return gpioIn;
    }

    public void setOutputPin(GpioPinDigitalOutput digitalOutput, boolean enable) {
        if(enable) digitalOutput.high();
        else digitalOutput.low();
    }
}