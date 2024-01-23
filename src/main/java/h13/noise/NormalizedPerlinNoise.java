package h13.noise;

import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

/**
 * A class that wraps a Perlin noise object and provides a normalized version of the noise values. Normalization
 * ensures that the noise values are scaled to the range [0, 1] for better usability.
 *
 * <p>The normalization is applied to the output of the specified Perlin noise object by dividing the noise value by 2
 * and then adding 0.5. This transformation maps the noise range [-1, 1] to the normalized range [0, 1].
 *
 * <p>The class implements the PerlinNoise interface by forwarding method calls to the wrapped Perlin noise object.
 * This allows for seamless integration with other Perlin noise-related components.
 *
 * @author Nhan Huynh
 * @see PerlinNoise
 */
public class NormalizedPerlinNoise extends DelegatePerlinNoise implements PerlinNoise {

    /**
     * Constructs a normalized Perlin object that normalizes the specified Perlin noise object.
     *
     * @param noise the Perlin noise object to normalize
     */
    public NormalizedPerlinNoise(PerlinNoise noise) {
        super(noise);
    }

    @Override
    @StudentImplementationRequired
    public double compute(int x, int y) {
        return (delegate.compute(x, y) + 1) / 2;
    }

    @Override
    @StudentImplementationRequired
    public double compute(double x, double y) {
        return (delegate.compute(x, y) + 1) / 2;
    }
}
