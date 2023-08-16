package h13.noise;

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

}
