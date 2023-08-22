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
    int width();

    /**
     * Returns the height of the noise domain.
     *
     * @return the height of the noise domain
     */
    int height();


    /**
     * Computes the gradient noise value at the specified noise domain coordinates.
     *
     * @param x The x-coordinate in the noise domain.
     * @param y The y-coordinate in the noise domain.
     * @return the computed gradient noise value at the specified noise domain coordinates
     */
    double compute(int x, int y);

    /**
     * Computes the gradient noise values for the entire noise domain.
     *
     * @return the computed gradient noise values for the entire noise domain
     */
    default double[][] compute() {
        return IntStream.range(0, width())
            .parallel()
            .mapToObj(x -> IntStream.range(0, height()).parallel().mapToDouble(y -> compute(x, y)))
            .map(DoubleStream::toArray)
            .toArray(double[][]::new);
    }

}
