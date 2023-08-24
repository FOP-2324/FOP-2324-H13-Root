package h13.ui.layout;

import h13.noise.FractalPerlinNoise;
import h13.noise.PerlinNoise;
import h13.noise.SimplePerlinNoise;
import h13.utils.Cache;
import h13.utils.LRUCache;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.DoubleFunction;
import java.util.stream.Collectors;

/**
 * A controller for a Perlin noise algorithm visualization which handles the logic of the visualization in order to
 * separate it from the view.o
 *
 * @author Nhan Huynh
 */
public class AlgorithmPaneController {

    /**
     * The cache size for the algorithms to store in memory in order to not recompute the same algorithm multiple times.
     */
    private static final int CACHE_SIZE = 10;

    /**
     * The color mapper function that maps a perlin noise value to a color.
     */
    private static final DoubleFunction<Color> COLOR_MAPPER = value -> {
        if (value <= 0.5) {
            // Water color (blue)
            return Color.color(0, 0f, value * 2f);
        } else {
            // Land color (green)
            return Color.color(0f, value, 0f);
        }
    };

    /**
     * The last algorithm that was computed which will be used to check if the algorithm is the same as the last one.
     * If it is the same, then the algorithm will not be recomputed.
     */
    private @Nullable PerlinNoise lastAlgorithm;

    /**
     * A cache for the simple perlin noise algorithm.
     */
    private final Cache<Long, PerlinNoise> cacheSimpleNoise = new LRUCache<>(CACHE_SIZE);

    /**
     * A cache for the improved perlin noise algorithm.
     */
    private final Cache<PerlinNoise, PerlinNoise> cacheImprovedNoise = new LRUCache<>(CACHE_SIZE);

    /**
     * The parameters properties of the perlin noise algorithm. This determines the parameters of the algorithm.
     */
    private final Map<String, Property<Number>> parameters = Arrays.stream(Parameter.values())
        .collect(Collectors.toMap(
            Parameter::toString,
            p -> switch (p) {
                case SEED -> new SimpleLongProperty(this, p.toString());
                case OCTAVES -> new SimpleIntegerProperty(this, p.toString());
                default -> new SimpleDoubleProperty(this, p.toString());
            }
        ));


    /**
     * The algorithm selection properties of the perlin noise algorithm. This determines which algorithm to use.
     */
    private final Map<String, BooleanProperty> algorithms = Arrays.stream(Algorithm.values())
        .collect(Collectors.toMap(
            Algorithm::toString,
            a -> new SimpleBooleanProperty(this, a.toString())
        ));

    /**
     * Returns the parameter property of the given parameter.
     *
     * @param parameter the parameter to get the property of
     * @return the property of the parameter
     */
    public Property<Number> getParameterProperty(Parameter parameter) {
        return parameters.get(parameter.toString());
    }

    /**
     * Returns the algorithm selection property of the given algorithm.
     *
     * @return the property of the algorithm
     */
    public Map<String, BooleanProperty> getAlgorithms() {
        return algorithms;
    }

    /**
     * Returns the algorithm selection property of the given algorithm.
     *
     * @param algorithm the algorithm to get the property of
     * @return the property of the algorithm
     */
    public BooleanProperty getAlgorithm(String algorithm) {
        return algorithms.get(algorithm);
    }

    /**
     * Returns the current algorithm based on the parameters and algorithm selection.
     *
     * @return the current algorithm based on the parameters and algorithm selection
     */
    private PerlinNoise getAlgorithm() {
        Property<Number> seed = getParameterProperty(Parameter.SEED);
        Long key = seed.getValue().longValue();

        PerlinNoise noise = cacheSimpleNoise.computeIfAbsent(key, k -> {
            Rectangle2D screen = Screen.getPrimary().getVisualBounds();
            return new SimplePerlinNoise(
                (int) screen.getWidth(),
                (int) screen.getHeight(),
                new Random(seed.getValue().longValue())
            );
        });

        if (getAlgorithm(Algorithm.IMPROVED.toString()).getValue()) {
            PerlinNoise tmp = noise;
            noise = cacheImprovedNoise.computeIfAbsent(noise, k -> PerlinNoise.improved(tmp));
        }

        if (getAlgorithm(Algorithm.FRACTAL.toString()).getValue()) {
            noise = new FractalPerlinNoise(
                noise,
                getParameterProperty(Parameter.AMPLITUDE).getValue().doubleValue(),
                getParameterProperty(Parameter.OCTAVES).getValue().intValue(),
                getParameterProperty(Parameter.LACUNARITY).getValue().doubleValue(),
                getParameterProperty(Parameter.PERSISTENCE).getValue().doubleValue()
            );
        }
        return PerlinNoise.normalized(noise);
    }

    /**
     * Draws the given algorithm on the given canvas.
     *
     * @param algorithm the algorithm to draw
     * @param canvas    the canvas to draw on
     * @param x         the starting x coordinate to draw
     * @param y         the starting y coordinate to draw
     * @param w         the width to draw
     * @param h         the height to draw
     */
    private void draw(PerlinNoise algorithm, Canvas canvas, int x, int y, int w, int h) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        double[][] noises = algorithm.compute(x, y, w, h);
        for (int xi = 0; xi < noises.length; xi++) {
            for (int yi = 0; yi < noises[xi].length; yi++) {
                Color color = COLOR_MAPPER.apply(noises[xi][yi]);
                context.setFill(color);
                context.fillRect(x + xi, y + yi, 1, 1);
            }
        }
    }

    /**
     * Adds a listener to the given expression which will redraw the algorithm on the given canvas when the expression
     * is true.
     *
     * @param expression the expression to listen to
     * @param canvas     the canvas to redraw on
     */
    public void addGenerateListener(BooleanExpression expression, Canvas canvas) {
        expression.addListener((observable, oldValue, newValue) -> {
            if (!newValue) return;
            PerlinNoise algorithm = getAlgorithm();
            double frequency = getParameterProperty(Parameter.FREQUENCY).getValue().doubleValue();
            // Do not compute if the algorithm and frequency are the same
            if (Objects.equals(lastAlgorithm, algorithm) && Double.compare(frequency, algorithm.frequency()) == 0)
                return;
            lastAlgorithm = algorithm;
            algorithm.setFrequency(frequency);
            draw(algorithm, canvas, 0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        });
    }
}
