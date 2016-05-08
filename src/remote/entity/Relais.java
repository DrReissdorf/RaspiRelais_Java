package remote.entity;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

public class Relais {
    private String name;
    private GpioPinDigitalOutput GPIO_OUTPUT;
    private GpioPinDigitalInput GPIO_INPUT;

    public Relais(String name, GpioPinDigitalOutput GPIO_OUTPUT, GpioPinDigitalInput GPIO_INPUT) {
        this.name = name;
        this.GPIO_OUTPUT = GPIO_OUTPUT;
        this.GPIO_INPUT = GPIO_INPUT;
    }

    public String getName() {
        return name;
    }

    public GpioPinDigitalOutput getGPIO_OUTPUT() {
        return GPIO_OUTPUT;
    }

    public GpioPinDigitalInput getGPIO_INPUT() {
        return GPIO_INPUT;
    }
}