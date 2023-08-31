package h13.ui.layout;

import h13.noise.NormalizedPerlinNoise;
import h13.noise.PerlinNoise;
import h13.ui.controls.NumberField;
import javafx.beans.property.BooleanProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.DoubleFunction;
import java.util.function.Supplier;

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

    private @Nullable PerlinNoise lastAlgorithm = null;

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
                writer.setColor(xi, yi, color);
            }
        }
        return image;
    }

    /**
     * Draws the given algorithm on the given graphics context at the given position and size. If the given algorithm
     * is {@code null}, nothing will be drawn. The algorithm will be normalized if it is not already normalized.
     *
     * @param algorithm the algorithm to draw
     * @param context   the graphics context to draw on
     * @param x         the starting x coordinate of the image
     * @param y         the starting y coordinate of the image
     * @param w         the width of the image
     * @param h         the height of the image
     */
    public void draw(@Nullable PerlinNoise algorithm, GraphicsContext context, int x, int y, int w, int h) {
        if (algorithm == null) {
            return;
        }
        if (!(algorithm instanceof NormalizedPerlinNoise)) {
            algorithm = PerlinNoise.normalized(algorithm);
        }
        lastAlgorithm = algorithm;

        @Nullable PerlinNoise toDraw = algorithm;
        run(() -> {
            context.drawImage(createImage(toDraw, x, y, w, h), x, y);
            return null;
        });
    }

    /**
     * Runs the given input and returns the result. If the input throws an {@link IllegalArgumentException}, an
     * {@link Alert} will be shown and {@code null} will be returned.
     *
     * @param input the input to run
     * @return the result of the input
     * @param <T> the type of the result
     */
    protected <T> @Nullable T run(Supplier<T> input) {
        try {
            return input.get();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Illegal parameter(s)");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return null;
        }
    }

    /**
     * Returns the last drawn algorithm. The is {@code null} if no algorithm has been drawn yet.
     *
     * @return the last drawn algorithm
     */
    public @Nullable PerlinNoise getLastAlgorithm() {
        return lastAlgorithm;
    }

}
