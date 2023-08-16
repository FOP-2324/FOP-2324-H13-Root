package h13.noise;

import javafx.geometry.Point2D;

import java.util.Random;

/**
 * A simple implementation of the Perlin noise algorithm.
 *
 * @author Nhan Huynh
 */
public class SimplePerlinNoise extends AbstractPerlinNoise implements PerlinNoise {

    /**
     * Constructs a SimplePerlinNoise object with the specified parameters.
     *
     * @param width  the width of the noise domain
     * @param height the height of the noise domain
     * @param seed   the random seed for generating gradient vectors
     */
    public SimplePerlinNoise(int width, int height, Random seed) {
        super(width, height, seed);
    }

    /**
     * Computes the gradient noise value at the specified noise domain coordinates.
     * It's recommended to multiply the coordinates by the frequency to achieve visible results.
     * <p>
     * If you use a lower frequency value (closer to 0), the noise pattern will have larger features and appear more
     * spread out. This can create smoother variations in the noise and give a sense of large-scale structure.
     * <p>
     * If you use a higher frequency value (larger than 1), the noise pattern will have smaller and more frequent
     * features. This can create more detailed and intricate variations in the noise, suitable for fine-grained textures or details.
     *
     * @param x The x-coordinate in the noise domain (scaled by frequency).
     * @param y The y-coordinate in the noise domain (scaled by frequency).
     * @return The computed gradient noise value at the specified noise domain coordinates.
     */
    @Override
    public double compute(double x, double y) {
        // Compute the coordinates of the gradient vector grid cell
        int x0 = (int) Math.floor(x);
        int y0 = (int) Math.floor(y);
        int x1 = x0 + 1;
        int y1 = y0 + 1;

        // Compute the offset vectors from the grid cell coordinates
        double dx = x - x0;
        double dy = y - y0;

        // Get gradient vectors at four corners of the cell
        Point2D gradient00 = getGradient(x0, y0);
        Point2D gradient01 = getGradient(x0, y1);
        Point2D gradient10 = getGradient(x1, y0);
        Point2D gradient11 = getGradient(x1, y1);

        // Compute dot products between gradient vectors and offset vectors
        double dot00 = gradient00.dotProduct(dx, dy);
        double dot01 = gradient01.dotProduct(dx, dy - 1);
        double dot10 = gradient10.dotProduct(dx - 1, dy);
        double dot11 = gradient11.dotProduct(dx - 1, dy - 1);

        // Compute weights for x and y-axis
        double u = fade(dx);
        double v = fade(dy);

        double interpolateX0 = interpolate(dot00, dot10, u);
        double interpolateX1 = interpolate(dot01, dot11, u);

        return interpolate(interpolateX0, interpolateX1, v);
    }

    @Override
    public double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    /**
     * Performs linear interpolation between two values.
     *
     * @param a     The first value.
     * @param b     The second value.
     * @param alpha The interpolation factor, typically in the range [0, 1].
     * @return the interpolated value
     */
    @Override
    public double interpolate(double a, double b, double alpha) {
        return a + alpha * (b - a);
    }

}
