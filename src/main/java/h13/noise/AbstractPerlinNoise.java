package h13.noise;

import javafx.geometry.Point2D;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

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

    /**
     * The default frequency to use for the Perlin noise.
     */
    public static final double DEFAULT_FREQUENCY = 0.005;

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
     * The array of gradient vectors where each vector is associated with a grid cell.
     */
    private final Point2D[] gradients;
    /**
     * The hash code of this Perlin noise object used for caching.
     */
    protected int hashCode = -1;
    /**
     * The frequency of the Perlin noise.
     */
    private double frequency;

    /**
     * Constructs an abstract Perlin noise with the specified noise domain and seed.
     *
     * @param width  the width of the noise domain
     * @param height the height of the noise domain
     * @param seed   the random seed for generating gradient vectors
     * @throws IllegalArgumentException if the width or height is negative
     */
    public AbstractPerlinNoise(int width, int height, Random seed) {
        this(width, height, DEFAULT_FREQUENCY, seed);
    }

    /**
     * Constructs an abstract Perlin noise with the specified noise domain, frequency and seed.
     *
     * @param width     the width of the noise domain
     * @param height    the height of the noise domain
     * @param frequency the frequency of the Perlin noise
     * @param seed      the random seed for generating gradient vectors
     * @throws IllegalArgumentException if the width or height is negative, or if the frequency is not between 0 and 1
     */
    public AbstractPerlinNoise(int width, int height, double frequency, Random seed) {
        if (width < 0) {
            throw new IllegalArgumentException("Width cannot be negative");
        }
        this.width = width;
        if (height < 0) {
            throw new IllegalArgumentException("Width cannot be negative");
        }
        this.height = height;
        setFrequency(frequency);
        this.seed = seed;
        this.gradients = createGradients(width + 2, height + 2);
    }

    /**
     * Generates an array of random 2D gradient vectors with dimensions wrapping around on the noise dimension, which
     * means that the width and height of the gradient array is two units larger than the noise domain.
     *
     * <p>Each point in the noise domain is associated with four corner gradient vectors.
     *
     * <p>The gradient vectors are stored in a 1D array, where we need to map the 2D coordinates to the 1D index.
     *
     * <p>Visual representation of the gradient domain:
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
    @StudentImplementationRequired
    protected Point2D[] createGradients(int width, int height) {
        // TODO H1.1
        Point2D[] gradients = new Point2D[width * height];
        for (int i = 0; i < gradients.length; i++) {
            gradients[i] = createGradient();
        }
        return gradients;
    }

    /**
     * Generates a random 2D gradient vector within the unit circle.
     *
     * @return a random gradient vector within the unit circle
     */
    @StudentImplementationRequired
    protected Point2D createGradient() {
        // TODO H1.1
        return new Point2D(seed.nextDouble(-1, 1), seed.nextDouble(-1, 1));
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public double getFrequency() {
        return frequency;
    }

    @Override
    public void setFrequency(double frequency) {
        if (frequency < 0 || frequency > 1) {
            throw new IllegalArgumentException("Frequency must be between 0 and 1");
        }
        this.frequency = frequency;
    }

    @Override
    public Random getSeed() {
        return seed;
    }

    @Override
    public Point2D[] getGradients() {
        return gradients;
    }

    @Override
    @StudentImplementationRequired
    public Point2D getGradient(int x, int y) {
        // TODO H1.1
        // Position of a 2D gradient (x, y) vector in a 1D array: (width + 2) * y + x
        // The +2 is because the gradient array is two units larger than the noise domain
        // (width + 2) * y =  first dimension of the 2D array, x = second dimension of the 2D array
        return gradients[(width + 2) * y + x];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractPerlinNoise that = (AbstractPerlinNoise) o;
        return width == that.width
            && height == that.height
            && Double.compare(frequency, that.frequency) == 0
            && Arrays.equals(gradients, that.gradients);
    }

    @Override
    public int hashCode() {
        if (hashCode == -1) {
            int result = Objects.hash(width, height, frequency, Arrays.hashCode(gradients));
            hashCode = 31 * result + Arrays.hashCode(gradients);
        }
        return hashCode;
    }
}
