package gpio;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import main.Main;
import remote.Data;
import remote.Relais;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class GPIO {
    private GpioController gpioController;
    private ArrayList<GpioPinDigitalOutput> gpioOutputs;
    private ArrayList<GpioPinDigitalInput> gpioInputs;

    public GPIO() {
        gpioController = GpioFactory.getInstance();
        gpioOutputs = new ArrayList<>();
        gpioInputs = new ArrayList<>();
    }

    public void initGpioPins() {
        GpioPinDigitalOutput tempOutput;
        GpioPinDigitalInput tempInput;

        for(Relais r : Data.relaisArrayList) {
            tempOutput = gpioController.provisionDigitalOutputPin(RaspiPin.getPinByName("GPIO "+r.getGPIO_OUTPUT()), PinState.LOW);
            gpioOutputs.add(tempOutput);

            tempInput = gpioController.provisionDigitalInputPin(RaspiPin.getPinByName("GPIO "+r.getGPIO_OUTPUT()), PinPullResistance.PULL_DOWN);
            tempInput.addListener((GpioPinListenerDigital) event -> {
                // display pin state on console
                for(Relais relais : Data.relaisArrayList) {
                    if(event.getPin().getName().equals("GPIO "+relais.getGPIO_OUTPUT())) {
                        relais.setEnabled(!relais.isEnabled());
                        output(event.getPin(),relais.isEnabled());
                        Main.notifyStatusChange();
                    }
                }
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
            });
            gpioInputs.add(tempInput);
        }

    }

    public boolean output(int pinNumber, boolean enable) {
        for(GpioPinDigitalOutput pin : gpioOutputs) {
            if(pin.getName().equals("GPIO "+pinNumber)) {
                if(enable) pin.high();
                else pin.low();
                return true;
            }
        }
        return false;
    }

    public boolean output(GpioPin pin, boolean enable) {
        GpioPinDigitalOutput outputPin = (GpioPinDigitalOutput) pin;
        if(enable) outputPin.high();
        else outputPin.low();
        return true;
    }
}
