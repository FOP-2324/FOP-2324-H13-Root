package h13.noise;

import javafx.geometry.Point2D;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

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
 * @see PerlinNoise
 */
public abstract class DelegatePerlinNoise implements PerlinNoise {

    /**
     * The underlying Perlin noise object to which method calls are delegated.
     */
    protected final PerlinNoise delegate;

    /**
     * Constructs a delegated Perlin noise object with the specified underlying Perlin noise object.
     *
     * @param delegate the underlying Perlin noise object
     */
    public DelegatePerlinNoise(PerlinNoise delegate) {
        this.delegate = delegate;
    }

    @Override
    @StudentImplementationRequired
    public int getWidth() {
        return delegate.getWidth();
    }

    @Override
    @StudentImplementationRequired
    public int getHeight() {
        return delegate.getHeight();
    }

    @Override
    @StudentImplementationRequired
    public Random getRandomGenerator() {
        return delegate.getRandomGenerator();
    }

    @Override
    @StudentImplementationRequired
    public Point2D[] getGradients() {
        return delegate.getGradients();
    }

    @Override
    @StudentImplementationRequired
    public Point2D getGradient(int x, int y) {
        return delegate.getGradient(x, y);
    }

    @Override
    @StudentImplementationRequired
    public double getFrequency() {
        return delegate.getFrequency();
    }

    @Override
    @StudentImplementationRequired
    public void setFrequency(double frequency) {
        delegate.setFrequency(frequency);
    }

    @Override
    @StudentImplementationRequired
    public double fade(double t) {
        return delegate.fade(t);
    }

    @Override
    @StudentImplementationRequired
    public double interpolate(double x1, double x2, double alpha) {
        return delegate.interpolate(x1, x2, alpha);
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
        return Objects.equals(delegate, that.delegate);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }
}
