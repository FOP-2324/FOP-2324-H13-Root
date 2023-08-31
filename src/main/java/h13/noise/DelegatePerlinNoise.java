package h13.noise;

import javafx.geometry.Point2D;

import java.util.Objects;
import java.util.Random;

/**
 * An abstract base class that delegates method calls to an underlying Perlin noise object.
 * This class simplifies the implementation of new Perlin noise variations by providing a basic structure
 * and forwarding method calls to an existing Perlin noise object.
 *
 * <p>Subclasses of {@link  DelegatePerlinNoise} can focus on implementing only the compute method
 * while inheriting the default implementations of other methods from the PerlinNoise interface.
 *
 * <p>The {@link DelegatePerlinNoise} class is designed to promote code reuse and modularity in Perlin noise algorithms.
 * It allows new Perlin noise variants to be created easily by extending this class and implementing specific
 * noise generation logic in the compute method.
 *
 * @author Nhan Huynh
 */
public abstract class DelegatePerlinNoise implements PerlinNoise {

    /**
     * The underlying Perlin noise object to which method calls are delegated.
     */
    protected final PerlinNoise noise;

    /**
     * Constructs a delegated Perlin noise object with the specified underlying Perlin noise object.
     *
     * @param noise the underlying Perlin noise object
     */
    public DelegatePerlinNoise(PerlinNoise noise) {
        this.noise = noise;
    }

    @Override
    public int getWidth() {
        return noise.getWidth();
    }

    @Override
    public int getHeight() {
        return noise.getHeight();
    }

    @Override
    public Random getSeed() {
        return noise.getSeed();
    }

    @Override
    public Point2D[] getGradients() {
        return noise.getGradients();
    }

    @Override
    public Point2D getGradient(int x, int y) {
        return noise.getGradient(x, y);
    }

    @Override
    public double getFrequency() {
        return noise.getFrequency();
    }

    @Override
    public void setFrequency(double frequency) {
        noise.setFrequency(frequency);
    }

    @Override
    public double fade(double t) {
        return noise.fade(t);
    }

    @Override
    public double interpolate(double y1, double y2, double alpha) {
        return noise.interpolate(y1, y2, alpha);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DelegatePerlinNoise that = (DelegatePerlinNoise) o;
        return Objects.equals(noise, that.noise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noise);
    }

}
