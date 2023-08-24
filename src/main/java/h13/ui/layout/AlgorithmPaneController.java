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

public class AlgorithmPaneController {

    private static final int CACHE_SIZE = 10;

    private static final DoubleFunction<Color> COLOR_MAPPER = value -> {
        if (value <= 0.5) {
            // Water color (blue)
            double blue = value * 2f;
            return Color.color(0, 0f, blue);
        } else {
            // Land color (green)
            double green = value;
            return Color.color(0f, green, 0f);
        }
    };

    private @Nullable PerlinNoise lastAlgorithm;
    private final Cache<Long, PerlinNoise> cacheSimpleNoise = new LRUCache<>(CACHE_SIZE);

    private final Cache<PerlinNoise, PerlinNoise> cacheImprovedNoise = new LRUCache<>(CACHE_SIZE);
    private final Map<String, Property<Number>> parameters = Arrays.stream(Parameter.values())
        .collect(Collectors.toMap(
            Parameter::toString,
            p -> switch (p) {
                case SEED -> new SimpleLongProperty(this, p.toString());
                case OCTAVES -> new SimpleIntegerProperty(this, p.toString());
                default -> new SimpleDoubleProperty(this, p.toString());
            }
        ));

    private final Map<String, BooleanProperty> algorithms = Arrays.stream(Algorithm.values())
        .collect(Collectors.toMap(
            Algorithm::toString,
            a -> new SimpleBooleanProperty(this, a.toString())
        ));

    public Property<Number> getParameterProperty(Parameter parameter) {
        return parameters.get(parameter.toString());
    }

    public Map<String, BooleanProperty> getAlgorithms() {
        return algorithms;
    }

    public BooleanProperty getAlgorithm(String algorithm) {
        return algorithms.get(algorithm);
    }

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

    private void draw(PerlinNoise algorithm, Canvas canvas) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        double[][] noises = algorithm.compute(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        for (int x = 0; x < noises.length; x++) {
            for (int y = 0; y < noises[x].length; y++) {
                Color color = COLOR_MAPPER.apply(noises[x][y]);
                context.setFill(color);
                context.fillRect(x, y, 1, 1);
            }
        }
    }

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
            draw(algorithm, canvas);
        });
    }
}
