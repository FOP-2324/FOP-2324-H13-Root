package h13.ui.layout;

/**
 * Represents all available algorithms for generating Perlin noise.
 *
 * @author Nhan Huynh
 */
public enum Algorithm {

    /**
     * The simple Perlin noise algorithm.
     */
    SIMPLE,

    /**
     * The improved Perlin noise algorithm using a permutation table.
     */
    IMPROVED,

    /**
     * The fractal Perlin noise algorithm using multiple octaves, each with a different frequency and amplitude.
     */
    FRACTAL;

    @Override
    public String toString() {
        String name = name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

}
