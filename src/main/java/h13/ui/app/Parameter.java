package h13.ui.app;

import h13.utils.Strings;

enum Parameter {
    SEED(0),
    FREQUENCY(0.005),
    AMPLITUDE(1.0),
    OCTAVES(8),
    LACUNARITY(2),
    PERSISTENCE(0.5),
    ;

    private final Number defaultValue;

    Parameter(Number defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Number defaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        return Strings.capitalize(name());
    }

}
