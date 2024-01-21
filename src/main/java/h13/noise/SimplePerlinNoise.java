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
     * @param width           the width of the noise domain
     * @param height          the height of the noise domain
     * @param frequency       the frequency of the Perlin noise
     * @param randomGenerator the random generator used for generating gradient vectors
     */
    public SimplePerlinNoise(int width, int height, double frequency, Random randomGenerator) {
        super(width, height, frequency, randomGenerator);
    }

    /**
     * Constructs a simple Perlin noise object with the specified noise domain width, height and seed.
     *
     * @param width           the width of the noise domain
     * @param height          the height of the noise domain
     * @param randomGenerator the random generator used for generating gradient vectors
     */
    public SimplePerlinNoise(int width, int height, Random randomGenerator) {
        super(width, height, randomGenerator);
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
        Point2D g00 = getGradient(x0, y0);
        Point2D g01 = getGradient(x0, y1);
        Point2D g10 = getGradient(x1, y0);
        Point2D g11 = getGradient(x1, y1);

        // Compute dot products between gradient vectors and offset vectors
        double s00 = g00.dotProduct(dx, dy);
        double s01 = g01.dotProduct(dx, dy - 1);
        double s10 = g10.dotProduct(dx - 1, dy);
        double s11 = g11.dotProduct(dx - 1, dy - 1);

        // Compute weights for x and y-axis
        double fdx = fade(dx);
        double fdy = fade(dy);

        double lx0 = interpolate(s00, s10, fdx);
        double lx1 = interpolate(s01, s11, fdx);

        return interpolate(lx0, lx1, fdy);
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
     * @param x1    The first value.
     * @param x2    The second value.
     * @param alpha The interpolation factor, typically in the range [0, 1].
     * @return the interpolated value
     */
    @Override
    @StudentImplementationRequired
    public double interpolate(double x1, double x2, double alpha) {
        // TODO H1.2
        return x1 + alpha * (x2 - x1);
    }
}
