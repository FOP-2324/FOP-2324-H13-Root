package h13.ui.app;

import h13.noise.FractalPerlinNoise;
import h13.noise.ImprovedPerlinNoise;
import h13.noise.SimplePerlinNoise;
import h13.util.Strings;

/**
 * The algorithm to use for the {@link PerlinNoiseApp}.
 *
 * @author Nhan Huynh
 */
enum Algorithm {

    /**
     * The {@link SimplePerlinNoise} algorithm.
     */
    SIMPLE,

    /**
     * The {@link ImprovedPerlinNoise} algorithm.
     */
    IMPROVED,

    /**
     * The {@link FractalPerlinNoise} algorithm.
     */
    FRACTAL,
    ;

    @Override
    public String toString() {
        return Strings.capitalize(name());
    }

}
