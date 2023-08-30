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

/**
 * An abstract base class that provides common functionality for handling the logic of the {@link AlgorithmView} for
 * visualizing a {@link PerlinNoise} algorithm.
 *
 * <p></p>This class serves as a foundation for implementing specific algorithm view models that use
 * {@link PerlinNoise} algorithms for creating the visualization. The selected algorithm and configuration are
 * determined by the options and parameters and will be specified by the concrete implementation of this class.
 *
 * @author Nhan Huynh
 */
public abstract class AlgorithmViewModel {

    /**
     * The available algorithm to choose from.
     */
    protected final Map<String, BooleanProperty> options;

    /**
     * The available parameters for the algorithms.
     */
    protected final Map<String, NumberField> parameters;

    /**
     * The color mapper for mapping the noise value to a color.
     */
    private final DoubleFunction<Color> colorMapper;

    /**
     * Creates a new algorithm view model with the given options, parameters and color mapper.
     *
     * @param options     the available algorithm to choose from
     * @param parameters  the available parameters for the algorithms
     * @param colorMapper the color mapper for mapping the noise value to a color
     */
    public AlgorithmViewModel(
        Map<String, BooleanProperty> options,
        Map<String, NumberField> parameters,
        DoubleFunction<Color> colorMapper
    ) {
        this.options = options;
        this.parameters = parameters;
        this.colorMapper = colorMapper;
    }

    /**
     * Returns the selected algorithm. If the algorithm is already drawn, return {@code null}.
     *
     * @return the selected algorithm
     */
    protected abstract @Nullable PerlinNoise getAlgorithm();

    /**
     * Creates an image using the given algorithm and starting position and size.
     *
     * @param algorithm the algorithm to use
     * @param x         the starting x coordinate of the image
     * @param y         the starting y coordinate of the image
     * @param w         the width of the image
     * @param h         the height of the image
     * @return the created image using the given algorithm and starting position and size
     */
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

    /**
     * Adds a draw listener to the given expression and canvas. The listener will draw the algorithm on the canvas when
     * the expression is true.
     *
     * @param expression the expression to listen to
     * @param canvas     the canvas to draw on
     */
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
