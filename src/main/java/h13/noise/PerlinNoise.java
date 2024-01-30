package h13.noise;

import javafx.geometry.Point2D;

import java.util.Random;

/**
 * This interface represents a gradient noise generator that used the Perlin noise algorithm to produce coherent noise
 * values based on grid coordinates.
 *
 * <p>Perlin noise is a type of gradient noise used in computer graphics and procedural content generation.
 *
 * @author Nhan Huynh
 * @see <a href="https://en.wikipedia.org/wiki/Perlin_noise">Perlin noise</a>
 */
public interface PerlinNoise extends GradientNoise {

    /**
     * Returns an improved version of the specified Perlin noise object.
     *
     * @param noise            the Perlin noise object to improve
     * @param permutationTable the permutation table used to improve the Perlin noise
     * @return an improved version of the specified Perlin noise object
     */
    static PerlinNoise improved(PerlinNoise noise, int[] permutationTable) {
        return new ImprovedPerlinNoise(noise, permutationTable);
    }

    /**
     * Returns an improved version of the specified Perlin noise object.
     *
     * @param noise the Perlin noise object to improve
     * @return an improved version of the specified Perlin noise object
     */
    static PerlinNoise improved(PerlinNoise noise) {
        return new ImprovedPerlinNoise(noise);
    }

    /**
     * Returns a normalized version of the specified Perlin noise object. Normalization ensures that the noise values
     * are scaled to the range [0, 1] for better usability.
     *
     * @param noise the Perlin noise object to normalize
     * @return a normalized version of the specified Perlin noise object
     */
    static PerlinNoise normalized(PerlinNoise noise) {
        return new NormalizedPerlinNoise(noise);
    }

    /**
     * Returns the random generator used by this Perlin noise object.
     *
     * @return the random generator used by this Perlin noise object.
     */
    Random getRandomGenerator();

    /**
     * Returns the gradient vectors associated with the specified noise domain coordinates. Since the gradient vectors
     * wrap around the noise domain.
     *
     * <p>Visual representation of a noise point (x, y) with its gradients:
     * <pre>{@code
     * (x0, y1)          (x1, y1)
     *          ---------
     *          | (x,y) |
     *          ---------
     * (x0, y0)          (x1, y0)
     * }</pre>
     *
     * @return the gradient vectors associated with the specified gradient domain coordinates
     */
    Point2D[] getGradients();

    /**
     * Returns the gradient vector associated with the specified gradient domain coordinates. Since the gradient vectors
     * wrap around the noise domain, the starting point of the gradient domain is at (0, 0) and the ending point
     * is at (width, height).
     *
     * @param x the x coordinate of the gradient domain
     * @param y the y coordinate of the gradient domain
     * @return the gradient vector associated with the specified gradient domain coordinates
     */
    Point2D getGradient(int x, int y);

    /**
     * Returns the frequency of the Perlin noise which is between 0 and 1. The frequency determines how quickly the
     * noise values change across space, and it is related to the scaling factor used in noise generation.
     *
     * @return the frequency of the Perlin noise
     */
    double getFrequency();

    /**
     * Sets the frequency of the Perlin noise to the specified value.
     *
     * @param frequency the new frequency of the Perlin noise
     * @throws IllegalArgumentException if the specified frequency is not in the range [0, 1]
     */
    void setFrequency(double frequency);

    /**
     * Applies the fade function to the given value to achieve a fading effect. The fade function is used to reduce the
     * influence of gradient vectors as the distance from the corner vertex increases. This fading effect ensures that
     * the gradient effect is stronger closer to the vertex and fades away as we move away from it.
     *
     * @param t the value to which the fade function will be applied
     * @return the result of applying the fade function to the input value
     */
    double fade(double t);

    /**
     * Performs an interpolation between two values based on a given alpha (weight) value which is typically in the
     * range [0, 1].
     *
     * @param x1    The first value.
     * @param x2    The second value.
     * @param alpha The interpolation factor, typically in the range [0, 1].
     * @return the interpolated value between x1 and x2.
     */
    double interpolate(double x1, double x2, double alpha);

    /**
     * Computes the gradient noise value at the specified noise domain coordinates.
     * It's recommended to multiply the coordinates by the frequency to achieve visible results.
     *
     * <p>If you use a lower frequency value, the noise pattern will have larger features and appear to more
     * spread out. This can create smoother variations in the noise and give a sense of large-scale structure.
     *
     * <p>If you use a higher frequency value, the noise pattern will have smaller and more frequent
     * features. This can create more detailed and intricate variations in the noise, suitable for fine-grained textures
     * or details.
     *
     * @param x The x-coordinate in the noise domain (scaled by frequency).
     * @param y The y-coordinate in the noise domain (scaled by frequency).
     * @return The computed gradient noise value at the specified noise domain coordinates.
     */
    double compute(double x, double y);
}
