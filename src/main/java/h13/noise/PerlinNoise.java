package h13.noise;

import javafx.geometry.Point2D;

import java.util.Random;

/**
 * This interface represents a gradient noise generator that used the Perlin noise algorithm to produce coherent noise
 * values based on grid coordinates.
 * <p>
 * Perlin noise is a type of gradient noise used in computer graphics and procedural content generation.
 *
 * @author Nhan Huynh
 * @see <a href="https://en.wikipedia.org/wiki/Perlin_noise">Perlin noise</a>
 */
public interface PerlinNoise extends GradientNoise {

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
     * Returns the seed used by this Perlin noise object.
     *
     * @return the seed used by this Perlin noise object
     */
    Random seed();

    /**
     * Returns the gradient vectors associated with the specified noise domain coordinates. Since the gradient vectors
     * wrap around the noise domain, the starting point of the gradients domain is at (-1, -1) and the ending point
     * is at (width, height) of the noise domain.
     *
     * <p>Visual representation of the gradients of a noise point (x,y):
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
    Point2D[] gradients();

    /**
     * Returns the gradient vector associated with the specified noise domain coordinates. Since the gradient vectors
     * wrap around the noise domain, the starting point of the gradients domain is at (-1, -1) and the ending point
     * is at (width, height) of the noise domain.
     *
     * @param x the x coordinate of the gradient domain
     * @param y the y coordinate of the gradient domain
     * @return the gradient vector associated with the specified gradient domain coordinates
     */
    Point2D getGradient(int x, int y);

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
     * Returns the frequency of the Perlin noise. The frequency determines how quickly the noise values change across
     * space, and it is related to the scaling factor used in noise generation.
     *
     * @return the frequency of the Perlin noise
     */
    double frequency();

    /**
     * Performs an interpolation between two values based on a given alpha (weight) value which is typically in the
     * range [0, 1].
     *
     * @param y1    The first value.
     * @param y2    The second value.
     * @param alpha The interpolation factor, typically in the range [0, 1].
     * @return the interpolated value between y1 and y2.
     */
    double interpolate(double y1, double y2, double alpha);

}
