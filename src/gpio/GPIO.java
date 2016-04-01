package gpio;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import main.Main;
import remote.DataAndTools;
import remote.entity.Relais;

import java.util.ArrayList;

public class GPIO {
    public void initGpioPins() {
        /* FILL RELAIS ARRAYLIST */
        DataAndTools.relaisArrayList.add(new Relais("Monitor Backlight",createOutputPin(RaspiPin.GPIO_01,false),createInputPin(RaspiPin.GPIO_25)));
        DataAndTools.relaisArrayList.add(new Relais("Relais 2",createOutputPin(RaspiPin.GPIO_04,false),createInputPin(RaspiPin.GPIO_24)));
        DataAndTools.relaisArrayList.add(new Relais("Relais 3",createOutputPin(RaspiPin.GPIO_05,false),createInputPin(RaspiPin.GPIO_23)));
        DataAndTools.relaisArrayList.add(new Relais("Relais 4",createOutputPin(RaspiPin.GPIO_06,false),createInputPin(RaspiPin.GPIO_22)));

        for(Relais relais : DataAndTools.relaisArrayList) {
            relais.getGPIO_INPUT().addListener((GpioPinListenerDigital) event -> {
                if(event.getState() == PinState.HIGH) {
                    if(DataAndTools.DEBUG_FLAG) DataAndTools.printLineWithTime(" --> INTERRUPT: " + event.getPin() + " STATE: "+event.getState());

                    for(Relais r : DataAndTools.relaisArrayList) {
                        if (event.getPin().getPin().getName().equals(r.getGPIO_INPUT().getPin().getName())) {
                            setOutputPin(r.getGPIO_OUTPUT(), !r.getGPIO_OUTPUT().isHigh());
                            DataAndTools.notifyStatusChange();
                        }
                    }
                }
            });
        }

    }

    public GpioPinDigitalOutput createOutputPin(Pin pin, boolean enable) {
        GpioPinDigitalOutput gpioOut;
        if(enable) {
            gpioOut = GpioFactory.getInstance().provisionDigitalOutputPin(pin, PinState.HIGH);
        } else {
            gpioOut = GpioFactory.getInstance().provisionDigitalOutputPin(pin, PinState.LOW);
        }
        gpioOut.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        return gpioOut;
    }

    public GpioPinDigitalInput createInputPin(Pin pin) {
        GpioPinDigitalInput gpioIn = GpioFactory.getInstance().provisionDigitalInputPin(pin, PinPullResistance.PULL_DOWN);
        return gpioIn;
    }

    public void setOutputPin(GpioPinDigitalOutput digitalOutput, boolean enable) {
        if(enable) digitalOutput.high();
        else digitalOutput.low();
    }
}