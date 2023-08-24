package h13.ui.layout;

public enum Parameter {

    SEED(0),
    FREQUENCY(0.005),
    AMPLITUDE(1),
    OCTAVES(8),
    LACUNARITY(2),
    PERSISTENCE(0.5),
    ;

    private final Number defaultValue;

    Parameter(Number defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Number getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        String name = name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

}
