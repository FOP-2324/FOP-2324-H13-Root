package h13.noise;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * This interface represents a gradient noise generator that produces coherent noise values based on grid coordinates.
 *
 * @author Nhan Huynh
 * @see <a href="https://en.wikipedia.org/wiki/Gradient_noise">Gradient noise</a>
 */
public interface GradientNoise {

    /**
     * Returns the width of the noise domain.
     *
     * @return the width of the noise domain
     */
    int getWidth();

    /**
     * Returns the height of the noise domain.
     *
     * @return the height of the noise domain
     */
    int getHeight();

    /**
     * Computes the gradient noise value at the specified noise domain coordinates.
     *
     * @param x The x-coordinate in the noise domain.
     * @param y The y-coordinate in the noise domain.
     * @return the computed gradient noise value at the specified noise domain coordinates
     */
    double compute(int x, int y);

    /**
     * Computes the gradient noise values for the specified noise domain coordinates (rectangle area).
     *
     * @param x the x-coordinate of the starting point of the noise domain
     * @param y the y-coordinate of the starting point of the noise domain
     * @param w the width of the noise domain
     * @param h the height of the noise domain
     * @return the computed gradient noise values for the specified noise domain coordinates
     */
    default double[][] compute(int x, int y, int w, int h) {
        return IntStream.range(x, w)
            .parallel()
            .mapToObj(xi -> IntStream.range(y, h).parallel().mapToDouble(yi -> compute(xi, yi)))
            .map(DoubleStream::toArray)
            .toArray(double[][]::new);
    }

    /**
     * Computes the gradient noise values for the entire noise domain.
     *
     * @return the computed gradient noise values for the entire noise domain
     */
    default double[][] compute() {
        return compute(0, 0, getWidth(), getHeight());
    }
}
