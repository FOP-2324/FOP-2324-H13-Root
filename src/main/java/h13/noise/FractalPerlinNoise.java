package h13.noise;

import java.util.Objects;

/**
 * A fractal implementation of the Perlin noise algorithm that generates coherent noise values based on grid
 * coordinates. This class adds fractal behavior to the underlying Perlin noise by combining multiple octaves of noise
 * values. Each octave has different parameters such as amplitude, frequency, and persistence, contributing to the
 * final result.
 *
 * <p>The fractal nature of this noise introduces variations across different scales, creating complex and detailed
 * patterns that resemble natural textures and terrains.
 *
 * <p>FractalPerlinNoise is useful for generating diverse procedural content in applications such as terrain generation,
 * texture synthesis, and more.
 *
 * @author Nhan Huynh
 * @see PerlinNoise
 */
public class FractalPerlinNoise extends DelegationPerlinNoise implements PerlinNoise {

    /**
     * The default amplitude to use for the noise.
     */
    private static final double DEFAULT_AMPLITUDE = 1.0;

    /**
     * The amplitude of the noise, controlling the range of values for each octave.
     */
    private double amplitude;

    /**
     * The number of octaves to use in the fractal noise computation.
     */
    private int octaves;

    /**
     * The persistence value which determines the amplitude decrease factor between octaves.
     */
    private double persistence;

    /**
     * The lacunarity value which determines the frequency increase factor between octaves.
     */
    private double lacunarity;

    /**
     * Constructs a fractal Perlin noise object with the specified underlying Perlin noise object and fractal
     * parameters.
     *
     * @param noise       the underlying Perlin noise object
     * @param amplitude   the amplitude of the noise, controlling the range of values for each octave
     * @param octaves     the number of octaves, determining the number of noise layers to combine
     * @param lacunarity  the lacunarity of the noise, controlling the change in frequency between octaves
     * @param persistence the persistence of the noise, influencing the amplitude of each successive octave
     */
    public FractalPerlinNoise(
        PerlinNoise noise,
        double amplitude,
        int octaves,
        double lacunarity,
        double persistence

    ) {
        super(noise);
        this.amplitude = amplitude;
        this.octaves = octaves;
        this.lacunarity = lacunarity;
        this.persistence = persistence;
    }

    /**
     * Constructs a fractal Perlin noise object with the specified underlying Perlin noise object and fractal
     * parameters.
     *
     * @param noise       the underlying Perlin noise object
     * @param octaves     the number of octaves, determining the number of noise layers to combine
     * @param lacunarity  the lacunarity of the noise, controlling the change in frequency between octaves
     * @param persistence the persistence of the noise, influencing the amplitude of each successive octave
     */
    public FractalPerlinNoise(
        PerlinNoise noise,
        int octaves,
        double lacunarity,
        double persistence
    ) {
        this(noise, DEFAULT_AMPLITUDE, octaves, lacunarity, persistence);
    }

    @Override
    public double compute(int x, int y) {
        return compute((double) x, y);
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
        // Starting frequency
        double f = getFrequency();
        // Starting amplitude
        double a = amplitude;

        // Combine noise values from multiple octaves
        for (int i = 0; i < octaves; i++) {
            totalNoise += underlying.compute(x * f, y * f) * a;
            // Increase frequency for the next octave
            f *= lacunarity;
            // Adjust amplitude for the next octave
            a *= persistence;
        }

        return totalNoise;
    }

    /**
     * Returns the amplitude of the noise, controlling the range of values for each octave.
     *
     * @return the amplitude of the noise
     */
    public double getAmplitude() {
        return amplitude;
    }

    /**
     * Sets the amplitude of the noise, controlling the range of values for each octave.
     *
     * @param amplitude the new amplitude of the noise
     */
    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    /**
     * Returns the number of octaves to use in the fractal noise computation.
     *
     * @return the number of octaves
     */
    public int getOctaves() {
        return octaves;
    }

    /**
     * Sets the number of octaves to use in the fractal noise computation.
     *
     * @param octaves the new number of octaves
     */
    public void setOctaves(int octaves) {
        this.octaves = octaves;
    }

    /**
     * Returns the lacunarity value which determines the frequency increase factor between octaves.
     *
     * @return the lacunarity value
     */
    public double getLacunarity() {
        return lacunarity;
    }

    /**
     * Sets the lacunarity value which determines the frequency increase factor between octaves.
     *
     * @param lacunarity the new lacunarity value
     */
    public void setLacunarity(double lacunarity) {
        this.lacunarity = lacunarity;
    }

    /**
     * Returns the persistence value which determines the amplitude decrease factor between octaves.
     *
     * @return the persistence value
     */
    public double getPersistence() {
        return persistence;
    }

    /**
     * Sets the persistence value which determines the amplitude decrease factor between octaves.
     *
     * @param persistence the new persistence value
     */
    public void setPersistence(double persistence) {
        this.persistence = persistence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        FractalPerlinNoise that = (FractalPerlinNoise) o;
        return Double.compare(amplitude, that.amplitude) == 0
            && octaves == that.octaves
            && Double.compare(lacunarity, that.lacunarity) == 0
            && Double.compare(persistence, that.persistence) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAmplitude(), getOctaves(), getPersistence(), getLacunarity());
    }
}

