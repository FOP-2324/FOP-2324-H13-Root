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
 * <p>Subclasses of {@link  DelegationPerlinNoise} can focus on implementing only the compute method
 * while inheriting the default implementations of other methods from the PerlinNoise interface.
 *
 * <p>The {@link DelegationPerlinNoise} class is designed to promote code reuse and modularity in Perlin noise algorithms.
 * It allows new Perlin noise variants to be created easily by extending this class and implementing specific
 * noise generation logic in the compute method.
 *
 * @author Nhan Huynh
 * @see PerlinNoise
 */
public abstract class DelegationPerlinNoise implements PerlinNoise {

    /**
     * The underlying Perlin noise object to which method calls are delegated.
     */
    protected final PerlinNoise underlying;

    /**
     * Constructs a delegated Perlin noise object with the specified underlying Perlin noise object.
     *
     * @param underlying the underlying Perlin noise object
     */
    public DelegationPerlinNoise(PerlinNoise underlying) {
        this.underlying = underlying;
    }

    @Override
    @StudentImplementationRequired
    public int getWidth() {
        // TODO H2.2
        return underlying.getWidth();
    }

    @Override
    @StudentImplementationRequired
    public int getHeight() {
        // TODO H2.2
        return underlying.getHeight();
    }

    @Override
    @StudentImplementationRequired
    public Random getRandomGenerator() {
        // TODO H2.2
        return underlying.getRandomGenerator();
    }

    @Override
    @StudentImplementationRequired
    public Point2D[] getGradients() {
        // TODO H2.2
        return underlying.getGradients();
    }

    @Override
    @StudentImplementationRequired
    public Point2D getGradient(int x, int y) {
        // TODO H2.2
        return underlying.getGradient(x, y);
    }

    @Override
    @StudentImplementationRequired
    public double getFrequency() {
        // TODO H2.2
        return underlying.getFrequency();
    }

    @Override
    @StudentImplementationRequired
    public void setFrequency(double frequency) {
        // TODO H2.2
        underlying.setFrequency(frequency);
    }

    @Override
    @StudentImplementationRequired
    public double fade(double t) {
        // TODO H2.2
        return underlying.fade(t);
    }

    @Override
    @StudentImplementationRequired
    public double interpolate(double y1, double y2, double alpha) {
        // TODO H2.2
        return underlying.interpolate(y1, y2, alpha);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DelegationPerlinNoise that = (DelegationPerlinNoise) o;
        return Objects.equals(underlying, that.underlying);
    }

    @Override
    public int hashCode() {
        return underlying.hashCode();
    }
}
