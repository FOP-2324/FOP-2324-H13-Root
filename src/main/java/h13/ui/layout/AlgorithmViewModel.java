package h13.ui.layout;

import h13.noise.PerlinNoise;
import h13.ui.controls.NumberField;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.DoubleFunction;

public abstract class AlgorithmViewModel {

    protected final Map<String, BooleanProperty> options;

    protected final Map<String, NumberField> parameters;

    private final DoubleFunction<Color> colorMapper;

    public AlgorithmViewModel(Map<String, BooleanProperty> options, Map<String, NumberField> parameters, DoubleFunction<Color> colorMapper) {
        this.options = options;
        this.parameters = parameters;
        this.colorMapper = colorMapper;
    }

    protected abstract @Nullable PerlinNoise getAlgorithm();

    protected Image createImage(PerlinNoise algorithm, int x, int y, int w, int h) {
        WritableImage image = new WritableImage(w, h);
        PixelWriter writer = image.getPixelWriter();
        double[][] noises = algorithm.compute(x, y, w, h);
        for (int xi = 0; xi < noises.length; xi++) {
            for (int yi = 0; yi < noises[xi].length; yi++) {
                Color color = colorMapper.apply(noises[xi][yi]);
                writer.setColor(x + xi, y + yi, color);
            }
        }
        return image;
    }

    public void addDrawListener(BooleanExpression expression, Canvas canvas) {
        expression.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                PerlinNoise algorithm = getAlgorithm();
                if (algorithm == null) {
                    return;
                }
                algorithm = PerlinNoise.normalized(algorithm);
                canvas.getGraphicsContext2D().drawImage(
                        createImage(algorithm, 0, 0, (int) canvas.getWidth(), (int) canvas.getHeight()), 0, 0
                );
            }
        });
    }

}
