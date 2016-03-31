package remote.entity;

public class Relais {
    private String name;
    private int GPIO_OUTPUT;
    private int GPIO_INPUT;
    private boolean isEnabled;

    public Relais(String name, int GPIO_OUTPUT, int GPIO_INPUT, boolean isEnabled) {
        this.name = name;
        this.GPIO_OUTPUT = GPIO_OUTPUT;
        this.GPIO_INPUT = GPIO_INPUT;
        this.isEnabled = isEnabled;
    }

    public String getName() {
        return name;
    }

    public int getGPIO_OUTPUT() {
        return GPIO_OUTPUT;
    }

    public int getGPIO_INPUT() {
        return GPIO_INPUT;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
