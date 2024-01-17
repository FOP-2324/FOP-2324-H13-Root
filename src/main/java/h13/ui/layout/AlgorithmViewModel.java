package h13.ui.layout;

import h13.noise.NormalizedPerlinNoise;
import h13.noise.PerlinNoise;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.DoubleFunction;
import java.util.function.Function;

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
    protected final Map<String, Property<Number>> parameters;

    /**
     * The color mapper for mapping the noise value to a color.
     */
    private final DoubleFunction<Color> colorMapper;

    /**
     * The last algorithm that was used to draw the image.
     */
    protected @Nullable PerlinNoise lastAlgorithm = null;

    /**
     * Creates a new algorithm view model with the given options, parameters and color mapper.
     *
     * @param options     the available algorithm to choose from
     * @param parameters  the available parameters for the algorithms
     * @param colorMapper the color mapper for mapping the noise value to a color
     */
    public AlgorithmViewModel(
        Map<String, BooleanProperty> options,
        Map<String, Property<Number>> parameters,
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
     * Returns the last algorithm used to draw the image. If no algorithm was drawn, return {@code null}.
     *
     * @return the last algorithm that was used to draw the image
     */
    protected @Nullable PerlinNoise getLastAlgorithm() {
        return lastAlgorithm;
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
        // TODO H5.1
        if (algorithm == null) {
            return;
        }
        if (!(algorithm instanceof NormalizedPerlinNoise)) {
            algorithm = PerlinNoise.normalized(algorithm);
        }
        lastAlgorithm = algorithm;

        @Nullable PerlinNoise toDraw = algorithm;
        run(() -> {
                Image image = createImage(toDraw, x, y, w, h);
                context.drawImage(image, x, y);
                return null;
            },
            "Illegal parameter(s)",
            Throwable::getMessage
        );
    }

    /**
     * Runs the given input and returns the result.
     * If an error occurs, an alert will be shown with the given text and the content of the error.
     *
     * @param input the input to run
     * @param <T>   the type of the result
     * @return the result of the input
     */
    protected <T> @Nullable T run(Callable<T> input, String text, Function<Throwable, String> content) {
        try {
            return input.call();
        } catch (Throwable e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(text);
            alert.setContentText(content.apply(e));
            alert.showAndWait();
            return null;
        }
    }

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
        // TODO H5.1
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
     * Saves the last drawn image to a file.
     *
     * @param width  the width of the image
     * @param height the height of the image
     */
    public void save(int width, int height) {
        if (lastAlgorithm == null) {
            return;
        }
        run(() -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
                File file = fileChooser.showSaveDialog(null);
                if (file != null) {
                    // Allows saving with a transparent background
                    SnapshotParameters parameters = new SnapshotParameters();
                    parameters.setFill(Color.TRANSPARENT);
                    Image image = createImage(lastAlgorithm, 0, 0, width, height);
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                }
                return null;
            },
            "Error saving image",
            Throwable::getMessage
        );
    }
}
