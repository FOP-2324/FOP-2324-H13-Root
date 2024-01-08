package h13.ui.app;


import h13.noise.FractalPerlinNoise;
import h13.noise.PerlinNoise;
import h13.noise.SimplePerlinNoise;
import h13.ui.layout.AlgorithmView;
import h13.ui.layout.AlgorithmViewModel;
import h13.util.Cache;
import h13.util.LRUCache;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.DoubleFunction;

/**
 * The viw model for {@link AlgorithmView} that defines the behavior of the {@link PerlinNoise}.
 *
 * @author Nhan Huynh
 */
public class PerlinNoiseViewModel extends AlgorithmViewModel {

    /**
     * The cache for the {@link SimplePerlinNoise} algorithm used to improve performance when accessing the same
     * algorithm multiple times.
     */
    private final Cache<Long, PerlinNoise> cacheSimpleNoise;

    /**
     * The cache for the {@link FractalPerlinNoise} algorithm used to improve performance when accessing the same
     * algorithm multiple times.
     */
    private final Cache<PerlinNoise, PerlinNoise> cacheImprovedNoise;

    /**
     * Constructs a new {@link PerlinNoiseViewModel} with the given options and parameters to handle and the color
     * function to use.
     *
     * @param options    the options to handle
     * @param parameters the parameters to handle
     * @param color      the color function to use
     * @param cacheSize  the size of the cache used to improve performance when accessing the same algorithm multiple
     */
    public PerlinNoiseViewModel(
        Map<String, BooleanProperty> options,
        Map<String, Property<Number>> parameters,
        DoubleFunction<Color> color,
        int cacheSize
    ) {
        super(options, parameters, color);
        this.cacheSimpleNoise = new LRUCache<>(cacheSize);
        this.cacheImprovedNoise = new LRUCache<>(cacheSize);
    }

    @Override
    protected @Nullable PerlinNoise getAlgorithm() {
        // TODO H6
        return run(
            () -> {
                // Simple algorithm will always be used
                Property<Number> seed = getParameter(Parameter.SEED);
                Long seedValue = seed.getValue().longValue();
                double frequency = getParameter(Parameter.FREQUENCY).getValue().doubleValue();

                PerlinNoise algorithm = cacheSimpleNoise.computeIfAbsent(seedValue, k -> {
                    Rectangle2D screen = Screen.getPrimary().getVisualBounds();
                    return new SimplePerlinNoise(
                        (int) screen.getWidth(),
                        (int) screen.getHeight(),
                        frequency,
                        new Random(seed.getValue().longValue())
                    );
                });

                // CCheck if the improved algorithm is enabled.
                if (getOption(Algorithm.IMPROVED).getValue()) {
                    PerlinNoise tmp = algorithm;
                    algorithm = cacheImprovedNoise.computeIfAbsent(algorithm, k -> PerlinNoise.improved(tmp));
                }

                // Check if the fractal algorithm is enabled.
                if (getOption(Algorithm.FRACTAL).getValue()) {
                    algorithm = new FractalPerlinNoise(
                        algorithm,
                        getParameter(Parameter.AMPLITUDE).getValue().doubleValue(),
                        getParameter(Parameter.OCTAVES).getValue().intValue(),
                        getParameter(Parameter.LACUNARITY).getValue().doubleValue(),
                        getParameter(Parameter.PERSISTENCE).getValue().doubleValue()
                    );
                }

                // Don't update the algorithm if it hasn't changed, null is used to indicate that the algorithm should not be
                // updated.
                if (Objects.equals(lastAlgorithm, algorithm) && Double.compare(frequency, algorithm.getFrequency()) == 0) {
                    return null;
                }

                // Used to check if the algorithm has changed.
                lastAlgorithm = algorithm;
                algorithm.setFrequency(frequency);
                return algorithm;
            },
            "Illegal frequency",
            Throwable::getMessage
        );
    }

    /**
     * Returns the number property to the given parameter.
     *
     * @param parameter the parameter to get the number property to
     * @return the number property to the given parameter
     */
    private Property<Number> getParameter(Parameter parameter) {
        return parameters.get(parameter.toString());
    }

    /**
     * Returns the boolean property to the given algorithm.
     *
     * @param algorithm the algorithm to get the boolean property to
     * @return the boolean property to the given algorithm
     */
    private BooleanProperty getOption(Algorithm algorithm) {
        return options.get(algorithm.toString());
    }
}
