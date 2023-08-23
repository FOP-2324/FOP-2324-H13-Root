package h13.noise;

import javafx.geometry.Point2D;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

/**
 * An abstract base class that provides common functionality for generating Perlin noise using gradient vectors.
 * This class serves as a foundation for implementing Perlin noise algorithms and offers a base implementation.
 *
 * @author Nhan Huynh
 * @see PerlinNoise
 */
public abstract class AbstractPerlinNoise implements PerlinNoise {

    private static final double DEFAULT_FREQUENCY = 0.01;

    /**
     * The random seed used for generating gradient vectors.
     */
    private final Random seed;

    /**
     * The width of the noise domain.
     */
    private final int width;

    /**
     * The height of the noise domain.
     */

    private final int height;

    /**
     * The frequency of the Perlin noise.
     */
    private double frequency;

    /**
     * The array of gradient vectors where each vector is associated with a grid cell.
     */
    private final Point2D[] gradients;

    /**
     * Constructs a AbstractPerlinNoise object with the specified parameters.
     *
     * @param width     the width of the noise domain
     * @param height    the height of the noise domain
     * @param frequency the frequency of the Perlin noise
     * @param seed      the random seed for generating gradient vectors
     */
    public AbstractPerlinNoise(int width, int height, double frequency, Random seed) {
        if (frequency < 0 || frequency > 1) throw new IllegalArgumentException("Frequency must be between 0 and 1");
        this.width = width;
        this.height = height;
        this.frequency = frequency;
        this.seed = seed;
        this.gradients = createGradients(width + 2, height + 2);
    }

    /**
     * Constructs a AbstractPerlinNoise object with the specified parameters.
     *
     * @param width  the width of the noise domain
     * @param height the height of the noise domain
     * @param seed   the random seed for generating gradient vectors
     */
    public AbstractPerlinNoise(int width, int height, Random seed) {
        this(width, height, DEFAULT_FREQUENCY, seed);
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public double frequency() {
        return frequency;
    }

    @Override
    public void setFrequency(double frequency) {
        if (frequency < 0 || frequency > 1) throw new IllegalArgumentException("Frequency must be between 0 and 1");
        this.frequency = frequency;
    }

    @Override
    public Random seed() {
        return seed;
    }

    /**
     * Generates a random 2D gradient vector within the unit circle.
     *
     * @return a Point2D representing the random gradient vector within the unit circle
     */
    private Point2D createGradient() {
        return new Point2D(seed.nextDouble(-1, 1), seed.nextDouble(-1, 1));
    }

    /**
     * Generates an array of random 2D gradient vectors with dimensions wrapping around on the noise dimension which
     * means that the width and height of the gradient array is two units larger than the noise domain.
     * <p>
     * Each point in the noise domain is associated with four corner gradient vectors.
     * <p>
     * The gradients vectors are stored in a 1D array, where we need to map the 2D coordinates to the 1D index.
     *
     * <p>Visual representation of the gradients domain:
     *
     * <pre>{@code
     *
     *  (xn+1, y0)             ...             (xn+1, yn)
     *              -------------------------
     *              | (xn, y0) ... (xn, yn) |
     *              |    Noise domain ...   |
     *              | (x0, y0) ... (x0, xn) |
     *              -------------------------
     *  (x-1, y-1)             ...            (x-1, yn)
     *
     * }</pre>
     *
     * @param width  The width of the gradient array, which determines the horizontal dimension of the noise domain.
     * @param height The height of the gradient array, which determines the vertical dimension of the noise domain.
     * @return random 2D gradient vectors with dimensions wrapping around on the noise dimension
     */
    private Point2D[] createGradients(int width, int height) {
        Point2D[] gradients = new Point2D[width * height];
        for (int i = 0; i < gradients.length; i++) {
            gradients[i] = createGradient();
        }
        return gradients;
    }

    @Override
    public Point2D[] gradients() {
        return gradients;
    }

    @Override
    public Point2D getGradient(int x, int y) {
        // Since the gradients array wrap around the noise domain, the size of the gradients array is equal to width + 2
        return gradients[(width + 2) * y + x];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractPerlinNoise that = (AbstractPerlinNoise) o;
        return width == that.width
            && height == that.height
            && Double.compare(frequency, that.frequency) == 0
            && Arrays.equals(gradients, that.gradients);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(width, height, frequency);
        result = 31 * result + Arrays.hashCode(gradients);
        return result;
    }
}
