package h13.ui;

import h13.noise.FractalPerlinNoise;
import h13.noise.PerlinNoise;
import h13.noise.SimplePerlinNoise;
import javafx.beans.property.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Control;
import javafx.scene.control.TextFormatter;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.function.DoubleFunction;
import java.util.regex.Pattern;

public class AlgorithmViewModel {

    private static final Pattern INTEGER_PATTERN = Pattern.compile("\\d*");
    private static final Pattern DOUBLE_PATTERN = Pattern.compile("\\d*(\\.\\d*)?");

    @Nullable PerlinNoise noise = null;

    private final BooleanProperty isImproved = new SimpleBooleanProperty();
    private final BooleanProperty isFractal = new SimpleBooleanProperty();

    private final IntegerProperty seed = new SimpleIntegerProperty();

    private final DoubleProperty frequency = new SimpleDoubleProperty();

    private final DoubleProperty amplitude = new SimpleDoubleProperty();

    private final IntegerProperty octaves = new SimpleIntegerProperty();

    private final DoubleProperty persistence = new SimpleDoubleProperty();

    private final DoubleProperty lacunarity = new SimpleDoubleProperty();

    private final BooleanProperty isGenerate = new SimpleBooleanProperty();

    private final DoubleProperty width = new SimpleDoubleProperty();

    private final DoubleProperty height = new SimpleDoubleProperty();


    public void bindGenerate(GraphicsContext context) {
        DoubleFunction<Color> f = value -> {
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
        isGenerate.addListener((observable, oldValue, newValue) -> {
            if (!newValue) return;
            int w = width.intValue();
            int h = height.intValue();
            noise = new SimplePerlinNoise(w, h, new Random(seed.get()));
            if (isImproved.get()) {
                noise = PerlinNoise.improved(noise);
            }
            if (isFractal.get()) {
                noise = new FractalPerlinNoise(noise, amplitude.get(), octaves.get(), persistence.get(), lacunarity.get());
            }
            noise = PerlinNoise.normalized(noise);
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    double value = noise.compute(x, y);
                    Color color = f.apply(value);
                    context.setFill(color);
                    context.fillRect(x, y, 1, 1);
                }
            }
        });
    }

    public BooleanProperty isImprovedProperty() {
        return isImproved;
    }

    public BooleanProperty isFractalProperty() {
        return isFractal;
    }

    public IntegerProperty seedProperty() {
        return seed;
    }

    public DoubleProperty amplitudeProperty() {
        return amplitude;
    }

    public DoubleProperty frequencyProperty() {
        return frequency;
    }

    public IntegerProperty octavesProperty() {
        return octaves;
    }

    public DoubleProperty persistenceProperty() {
        return persistence;
    }

    public DoubleProperty lacunarityProperty() {
        return lacunarity;
    }

    public BooleanProperty isGenerateProperty() {
        return isGenerate;
    }

    public DoubleProperty widthProperty() {
        return width;
    }

    public DoubleProperty heightProperty() {
        return height;
    }

    public TextFormatter<String> getIntegerFormatter() {
        return new TextFormatter<>(
            change -> INTEGER_PATTERN.matcher(change.getControlNewText()).matches() ? change : null
        );
    }

    public TextFormatter<String> getDoubleFormatter() {
        return new TextFormatter<>(
            change -> DOUBLE_PATTERN.matcher(change.getControlNewText()).matches() ? change : null
        );
    }

    public StringConverter<Number> getDoubleConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Number object) {
                return object.toString();
            }

            @Override
            public Number fromString(String string) {
                return string.isEmpty() ? null : Double.valueOf(string);
            }
        };
    }

    public StringConverter<Number> getIntegerConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Number object) {
                return object.toString();
            }

            @Override
            public Number fromString(String string) {
                return string.isEmpty() ? null : Integer.valueOf(string);
            }
        };
    }

    public void bindFrequency(Control... controls) {
        isImproved.addListener((observable, oldValue, newValue) -> {
            if (isFractal.get()) {
                return;
            }
            for (Control control : controls) {
                control.setDisable(!newValue);
            }
        });
        isFractal.addListener((observable, oldValue, newValue) -> {
            if (isImproved.get()) {
                return;
            }
            for (Control control : controls) {
                control.setDisable(!newValue);
            }
        });
    }

    public void bindFractal(Control... controls) {
        isFractal.addListener((observable, oldValue, newValue) -> {
            for (Control control : controls) {
                control.setDisable(!newValue);
            }
        });
    }

}
