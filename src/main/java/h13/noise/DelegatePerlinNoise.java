package h13.noise;

import javafx.geometry.Point2D;

import java.util.Random;

/**
 * An abstract base class that delegates method calls to an underlying Perlin noise object.
 * This class simplifies the implementation of new Perlin noise variations by providing a basic structure
 * and forwarding method calls to an existing Perlin noise object.
 * <p>
 * Subclasses of DelegatePerlinNoise can focus on implementing only the compute method
 * while inheriting the default implementations of other methods from the PerlinNoise interface.
 * <p>
 * The DelegatePerlinNoise class is designed to promote code reuse and modularity in Perlin noise algorithms.
 * It allows new Perlin noise variants to be created easily by extending this class and implementing specific
 * noise generation logic in the compute method.
 *
 * @author Nhan Huynh
 * @see PerlinNoise
 */
public abstract class DelegatePerlinNoise implements PerlinNoise {

    /**
     * The underlying Perlin noise object to which method calls are delegated.
     */
    protected final PerlinNoise noise;

    /**
     * Constructs a DelegatePerlinNoise object with the specified underlying Perlin noise object.
     *
     * @param noise the underlying Perlin noise object
     */
    public DelegatePerlinNoise(PerlinNoise noise) {
        this.noise = noise;
    }

    @Override
    public int width() {
        return noise.width();
    }

    @Override
    public int height() {
        return noise.height();
    }

    @Override
    public Random seed() {
        return noise.seed();
    }

    @Override
    public Point2D[] gradients() {
        return noise.gradients();
    }

    @Override
    public Point2D getGradient(int x, int y) {
        return noise.getGradient(x, y);
    }

    @Override
    public double frequency() {
        return noise.frequency();
    }

    @Override
    public double fade(double t) {
        return noise.fade(t);
    }

    @Override
    public double interpolate(double y1, double y2, double alpha) {
        return noise.interpolate(y1, y2, alpha);
    }

}
