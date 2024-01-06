package h13.ui.app;

import h13.util.Strings;

/**
 * The parameters that can be used in the {@link PerlinNoiseApp} and Perlin Noise {@link Algorithm}s.
 *
 * @author Nhan Huynh
 */
enum Parameter {

    /**
     * The seed used to generate the noise.
     */
    SEED(0),
    /**
     * The frequency of the noise.
     */
    FREQUENCY(0.005),
    /**
     * The amplitude of the noise.
     */
    AMPLITUDE(1.0),
    /**
     * The number of octaves used to generate the noise.
     */
    OCTAVES(8),
    /**
     * The lacunarity of the noise.
     */
    LACUNARITY(2),
    /**
     * The persistence of the noise.
     */
    PERSISTENCE(0.5),
    ;

    /**
     * The default value of the parameter.
     */
    private final Number defaultValue;

    /**
     * Constructs a new parameter with the given default value.
     *
     * @param defaultValue the default value of the parameter
     */
    Parameter(Number defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Returns the default value of the parameter.
     *
     * @return the default value of the parameter
     */
    public Number defaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        return Strings.capitalize(name());
    }

}
