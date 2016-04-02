package gpio;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import remote.DataAndTools;
import remote.entity.Relais;

public class GPIO {
    private long timeout;
    private final int bouncetime = 200;
    private GpioController gpioController;

    public GPIO() {
        gpioController = GpioFactory.getInstance();
    }

    public void initGpioPins() {
        /* FILL RELAIS ARRAYLIST */
        DataAndTools.relaisArrayList.add(new Relais("Monitor Backlight",createOutputPin(DataAndTools.getPin(12),false),createInputPin(DataAndTools.getPin(37))));
        DataAndTools.relaisArrayList.add(new Relais("Relais 2",createOutputPin(DataAndTools.getPin(16),false),createInputPin(DataAndTools.getPin(35))));
        DataAndTools.relaisArrayList.add(new Relais("Relais 3",createOutputPin(DataAndTools.getPin(18),false),createInputPin(DataAndTools.getPin(33))));
        DataAndTools.relaisArrayList.add(new Relais("Relais 4",createOutputPin(DataAndTools.getPin(22),false),createInputPin(DataAndTools.getPin(31))));

        for(Relais relais : DataAndTools.relaisArrayList) {
            /* LISTENER FOR HARDWARE BUTTONS */
            relais.getGPIO_INPUT().addListener((GpioPinListenerDigital) event -> {
                if(event.getState()==PinState.HIGH && (System.currentTimeMillis()-timeout)>bouncetime) {
                    timeout = System.currentTimeMillis();
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