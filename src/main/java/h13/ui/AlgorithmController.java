package h13.ui;

import h13.noise.PerlinNoise;
import h13.noise.SimplePerlinNoise;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.function.DoubleFunction;

public class AlgorithmController {

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

    private @Nullable PerlinNoise noise;

    private final BooleanProperty improved = new SimpleBooleanProperty(this, "improved");

    private final BooleanProperty fractal = new SimpleBooleanProperty(this, "fractal");

    private final IntegerProperty seed = new SimpleIntegerProperty(this, "seed");

    private final DoubleProperty frequency = new SimpleDoubleProperty(this, "frequency");

    private final DoubleProperty amplitude = new SimpleDoubleProperty(this, "amplitude");

    private final IntegerProperty octaves = new SimpleIntegerProperty(this, "octaves");
    private final DoubleProperty persistence = new SimpleDoubleProperty(this, "persistence");

    private final DoubleProperty lacunarity = new SimpleDoubleProperty(this, "lacunarity");

    private final BooleanProperty generate = new SimpleBooleanProperty(this, "generate");

    public BooleanProperty improvedProperty() {
        return improved;
    }

    public BooleanProperty fractalProperty() {
        return fractal;
    }

    public IntegerProperty seedProperty() {
        return seed;
    }

    public DoubleProperty frequencyProperty() {
        return frequency;
    }

    public DoubleProperty amplitudeProperty() {
        return amplitude;
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

    public BooleanProperty generateProperty() {
        return generate;
    }

    public void addFractalParameterListener(Control... controls) {
        for (Control control : controls) {
            control.disableProperty().bind(fractal.not());
        }
    }

    public void addGenerateListener(BooleanExpression property, Canvas canvas) {
        generate.bind(property);
        generate.addListener((observable, oldValue, newValue) -> {
            if (!newValue) return;

            if (noise == null || !improved.getValue()) {
                // Screen bounds
                Rectangle2D screen = Screen.getPrimary().getVisualBounds();
                int swidth = (int) screen.getWidth() / 2;
                int sheight = (int) screen.getHeight() / 2;

                try {
                    noise = new SimplePerlinNoise(swidth, sheight, frequency.get(), new Random(seed.get()));
                } catch (IllegalArgumentException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Invalid parameters");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    return;
                }
            }
            if (improved.getValue()) {
                noise = PerlinNoise.improved(noise);
            }
            noise = PerlinNoise.normalized(noise);

            try {
                noise.setFrequency(frequency.doubleValue());
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Invalid parameters");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }

            int width = (int) canvas.getWidth();
            int height = (int) canvas.getHeight();
            double[][] noises = noise.compute(0, 0, width, height);
            GraphicsContext context = canvas.getGraphicsContext2D();
            for (int x = 0; x < canvas.getWidth(); x++) {
                for (int y = 0; y < canvas.getHeight(); y++) {
                    Color color = COLOR_MAPPER.apply(noises[x][y]);
                    context.setFill(color);
                    context.fillRect(x, y, 1, 1);
                }
            }
        });
    }

}
