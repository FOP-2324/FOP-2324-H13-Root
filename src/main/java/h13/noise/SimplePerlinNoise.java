package h13.noise;

import javafx.geometry.Point2D;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.util.Random;

/**
 * A simple implementation of the Perlin noise algorithm.
 *
 * @author Nhan Huynh
 */
public class SimplePerlinNoise extends AbstractPerlinNoise implements PerlinNoise {

    /**
     * Constructs a simple Perlin noise object with the specified noise domain width, height, frequency and seed.
     *
     * @param width     the width of the noise domain
     * @param height    the height of the noise domain
     * @param frequency the frequency of the Perlin noise
     * @param seed      the random seed for generating gradient vectors
     */
    public SimplePerlinNoise(int width, int height, double frequency, Random seed) {
        super(width, height, frequency, seed);
    }

    /**
     * Constructs a simple Perlin noise object with the specified noise domain width, height and seed.
     *
     * @param width  the width of the noise domain
     * @param height the height of the noise domain
     * @param seed   the random seed for generating gradient vectors
     */
    public SimplePerlinNoise(int width, int height, Random seed) {
        super(width, height, seed);
    }

    @Override
    public double compute(int x, int y) {
        double f = getFrequency();
        return compute(x * f, y * f);
    }

    @Override
    @StudentImplementationRequired
    public double compute(double x, double y) {
        // TODO H1.3
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
    @StudentImplementationRequired
    public double fade(double t) {
        // TODO H1.2
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
    @StudentImplementationRequired
    public double interpolate(double a, double b, double alpha) {
        // TODO H1.2
        return a + alpha * (b - a);
    }
}
