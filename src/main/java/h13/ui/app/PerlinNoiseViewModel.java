package h13.ui.app;

import h13.noise.FractalPerlinNoise;
import h13.noise.PerlinNoise;
import h13.noise.SimplePerlinNoise;
import h13.ui.controls.NumberField;
import h13.ui.layout.AlgorithmViewModel;
import h13.utils.Cache;
import h13.utils.LRUCache;
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

class PerlinNoiseViewModel extends AlgorithmViewModel {
    private final Cache<Long, PerlinNoise> cacheSimpleNoise;
    private final Cache<PerlinNoise, PerlinNoise> cacheImprovedNoise;
    private @Nullable PerlinNoise lastAlgorithm;

    public PerlinNoiseViewModel(
        Map<String, BooleanProperty> options,
        Map<String, NumberField> parameters,
        DoubleFunction<Color> color,
        int cacheSize
    ) {
        super(options, parameters, color);
        this.cacheSimpleNoise = new LRUCache<>(cacheSize);
        this.cacheImprovedNoise = new LRUCache<>(cacheSize);
    }

    private Property<Number> getParameter(Parameter parameter) {
        return parameters.get(parameter.toString()).valueProperty();
    }

    private BooleanProperty getOption(Algorithm algorithm) {
        return options.get(algorithm.toString());
    }

    @Override
    protected @Nullable PerlinNoise getAlgorithm() {
        return run(() -> {
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

            if (getOption(Algorithm.IMPROVED).getValue()) {
                PerlinNoise tmp = algorithm;
                algorithm = cacheImprovedNoise.computeIfAbsent(algorithm, k -> PerlinNoise.improved(tmp));
            }

            if (getOption(Algorithm.FRACTAL).getValue()) {
                algorithm = new FractalPerlinNoise(
                    algorithm,
                    getParameter(Parameter.AMPLITUDE).getValue().doubleValue(),
                    getParameter(Parameter.OCTAVES).getValue().intValue(),
                    getParameter(Parameter.LACUNARITY).getValue().doubleValue(),
                    getParameter(Parameter.PERSISTENCE).getValue().doubleValue()
                );
            }

            if (Objects.equals(lastAlgorithm, algorithm) && Double.compare(frequency, algorithm.getFrequency()) == 0) {
                return null;
            }
            lastAlgorithm = algorithm;
            algorithm.setFrequency(frequency);
            return algorithm;
        });
    }
}
