package h13.noise;

/**
 * A fractal implementation of the Perlin noise algorithm that generates coherent noise values based on grid
 * coordinates. This class adds fractal behavior to the underlying Perlin noise by combining multiple octaves of noise
 * values. Each octave has different parameters such as amplitude, frequency, and persistence, contributing to the
 * final result.
 *
 * <p>
 * The fractal nature of this noise introduces variations across different scales, creating complex and detailed
 * patterns that resemble natural textures and terrains.
 *
 * <p>
 * FractalPerlinNoise is useful for generating diverse procedural content in applications such as terrain generation,
 * texture synthesis, and more.
 *
 * @author Nhan Huynh
 * @see PerlinNoise
 */
public class FractalPerlinNoise extends DelegatePerlinNoise implements PerlinNoise {

    /**
     * The default amplitude for the noise.
     */
    private static final double DEFAULT_AMPLITUDE = 1.0;

    /**
     * The default frequency for the noise.
     */
    private static final double DEFAULT_FREQUENCY = 0.005;

    /**
     * The amplitude of the noise, controlling the range of values for each octave.
     */
    private final double amplitude;

    /**
     * The frequency of the noise, controlling the scale of the noise.
     */
    private final double frequency;

    /**
     * The number of octaves to use in the fractal noise computation.
     */
    private final int octaves;

    /**
     * The persistence value which determines the amplitude decrease factor between octaves.
     */
    private final double persistence;

    /**
     * The lacunarity value which determines the frequency increase factor between octaves.
     */
    private final double lacunarity;

    /**
     * Constructs a FractalPerlinNoise object with the specified underlying Perlin noise object and fractal parameters.
     *
     * @param noise       the underlying Perlin noise object
     * @param amplitude   the amplitude of the noise, controlling the range of values for each octave
     * @param frequency   the frequency of the noise, controlling the scale of the noise
     * @param octaves     the number of octaves, determining the number of noise layers to combine
     * @param persistence the persistence of the noise, influencing the amplitude of each successive octave
     * @param lacunarity  the lacunarity of the noise, controlling the change in frequency between octaves
     */
    public FractalPerlinNoise(
        PerlinNoise noise,
        double amplitude,
        double frequency,
        int octaves,
        double persistence,
        double lacunarity
    ) {
        super(noise);
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.octaves = octaves;
        this.persistence = persistence;
        this.lacunarity = lacunarity;
    }

    /**
     * Constructs a FractalPerlinNoise object with the specified underlying Perlin noise object and fractal parameters.
     *
     * <p>
     * The amplitude and frequency are set to default values of {@value #DEFAULT_AMPLITUDE} and
     * {@value #DEFAULT_FREQUENCY} respectively.
     *
     * @param noise       the underlying Perlin noise object
     * @param octaves     the number of octaves, determining the number of noise layers to combine
     * @param persistence the persistence of the noise, influencing the amplitude of each successive octave
     * @param lacunarity  the lacunarity of the noise, controlling the change in frequency between octaves
     */
    public FractalPerlinNoise(
        PerlinNoise noise,
        int octaves,
        double persistence,
        double lacunarity
    ) {
        this(noise, DEFAULT_AMPLITUDE, DEFAULT_FREQUENCY, octaves, persistence, lacunarity);
    }

    /**
     * Computes the fractal Perlin noise value at the specified noise domain coordinates.
     * This method combines multiple octaves of noise values to produce the final result.
     *
     * @param x The x-coordinate in the noise domain.
     * @param y The y-coordinate in the noise domain.
     * @return The computed fractal Perlin noise value at the specified noise domain coordinates.
     */
    @Override
    public double compute(double x, double y) {
        double totalNoise = 0;
        double f = frequency; // Starting frequency
        double a = amplitude;   // Starting amplitude

        // Combine noise values from multiple octaves
        for (int i = 0; i < octaves; i++) {
            totalNoise += noise.compute(x * f, y * f) * a;
            // Increase frequency for the next octave
            f *= lacunarity;
            // Adjust amplitude for the next octave
            a *= persistence;
        }

        return totalNoise;
    }
}

