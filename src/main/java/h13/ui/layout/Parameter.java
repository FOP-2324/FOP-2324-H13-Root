package h13.ui.layout;

/**
 * Represents all available parameters for generating Perlin noise.
 *
 * @author Nhan Huynh
 */
public enum Parameter {

    /**
     * The random seed used for generating gradient vectors.
     */
    SEED(0),

    /**
     * The frequency which determines how quickly the noise values change across space, and it is related to the
     * scaling factor used in noise generation.
     */
    FREQUENCY(0.005),

    /**
     * The amplitude of the noise, controlling the range of values for each octave
     */
    AMPLITUDE(1),

    /**
     * The number of octaves, determining the number of noise layers to combine.
     */
    OCTAVES(8),

    /**
     * The lacunarity of the noise, controlling the change in frequency between octaves.
     */
    LACUNARITY(2),

    /**
     * The persistence of the noise, influencing the amplitude of each successive octave.
     */
    PERSISTENCE(0.5),
    ;

    /**
     * The default value of the parameter.
     */
    private final Number defaultValue;

    /**
     * Constructs a parameter with the specified default value.
     * @param defaultValue the default value of the parameter
     */
    Parameter(Number defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Returns the default value of the parameter.
     * @return the default value of the parameter
     */
    public Number getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        String name = name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

}
