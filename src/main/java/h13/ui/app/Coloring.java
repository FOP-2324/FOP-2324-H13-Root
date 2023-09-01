package h13.ui.app;

import h13.noise.PerlinNoise;
import javafx.scene.paint.Color;

import java.util.function.DoubleFunction;

/**
 * The colorings for the noise values of the {@link PerlinNoise}.
 *
 * @author Nhan Huynh
 */
enum Coloring {

    /**
     * A simple coloring which contains only water and land.
     */
    SIMPLE(value -> {
        if (value <= 0.5) {
            // Water color (blue)
            return Color.color(0, 0f, value * 2f);
        } else {
            // Land color (green)
            return Color.color(0f, value, 0f);
        }
    }),

    /**
     * A coloring which contains water, land, mountain and snow.
     */
    MOUNTAIN(value -> {
        if (value < 0.45) {
            // Water color (blue)
            return Color.color(0, 0f, value * 2f);
        }
        if (value < 0.6) {
            // Land color (green)
            return Color.color(0f, value, 0f);
        }
        if (value < 0.8) {
            // Mountain color (brown)
            return Color.color(value, value * 0.5, 0);
        }
        // Snow color (white)
        return Color.color(value, value, value);
    }),
    ;

    /**
     * The mapper which maps the noise value to a color.
     */
    private final DoubleFunction<Color> mapper;

    /**
     * Constructs a coloring scheme with the given mapper.
     *
     * @param mapper the mapper which maps the noise value to a color
     */
    Coloring(DoubleFunction<Color> mapper) {
        this.mapper = mapper;
    }

    /**
     * Returns the mapper which maps the noise value to a color.
     *
     * @return the mapper which maps the noise value to a color
     */
    public DoubleFunction<Color> getMapper() {
        return mapper;
    }

}
