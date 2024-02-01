package h13.noise;

import javafx.geometry.Point2D;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.util.Arrays;
import java.util.Random;

/**
 * An improved implementation of the Perlin noise algorithm that uses a permutation array to access the gradient.
 *
 * @author Nhan Huynh
 */
public class ImprovedPerlinNoise extends SimplePerlinNoise implements PerlinNoise {

    /**
     * The size of the permutation array.
     */
    public static final int PERMUTATION_SIZE = 256;

    /**
     * The permutation array used for accessing the gradient vectors.
     */
    private final int[] p;

    /**
     * Constructs an improved Perlin noise with wrapping the underlying Perlin noise object.
     *
     * @param noise the underlying Perlin noise object
     */
    public ImprovedPerlinNoise(PerlinNoise noise) {
        this(noise, createPermutation(noise.getRandomGenerator()));
    }

    /**
     * Constructs an improved Perlin noise with wrapping the underlying Perlin noise object.
     *
     * @param noise            the underlying Perlin noise object
     * @param p the permutation array used for accessing the gradient vectors
     * @throws IllegalArgumentException if the permutation array does not have the size {@value #PERMUTATION_SIZE} * 2
     */
    public ImprovedPerlinNoise(PerlinNoise noise, int[] p) {
        super(noise.getWidth(), noise.getHeight(), noise.getFrequency(), noise.getRandomGenerator());
        if (p.length != PERMUTATION_SIZE * 2) {
            throw new IllegalArgumentException("The permutation array must have the size %d * 2.".formatted(PERMUTATION_SIZE));
        }
        this.p = p;
    }

    /**
     * Creates a permutation array of the size {@value #PERMUTATION_SIZE} * 2, where the first {@value #PERMUTATION_SIZE}
     * elements are the values from 0 to {@value #PERMUTATION_SIZE} ordered in ascending order and the last
     * {@value #PERMUTATION_SIZE} elements are the same as the first {@value #PERMUTATION_SIZE} elements but randomly
     * shuffled.
     *
     * @param randomGenerator the random generator used for generating the permutation array
     * @return the permutation array
     */
    @StudentImplementationRequired
    private static int[] createPermutation(Random randomGenerator) {
        // TODO H2.1
        int[] p = new int[PERMUTATION_SIZE * 2];
        for (int i = 0; i < PERMUTATION_SIZE; i++) {
            p[i] = p[i + PERMUTATION_SIZE] = i;
        }
        for (int i = 0; i < PERMUTATION_SIZE; i++) {
            int j = randomGenerator.nextInt(0, PERMUTATION_SIZE);
            int temp = p[i + PERMUTATION_SIZE];
            p[i + PERMUTATION_SIZE] = p[j + PERMUTATION_SIZE];
            p[j + PERMUTATION_SIZE] = temp;
        }
        return p;
    }

    @Override
    @StudentImplementationRequired
    public Point2D getGradient(int x, int y) {
        // TODO H2.1
        // Formula: p[x + p[y & (n - 1)] & (n - 1)]
        int index = p[(x + p[y & (PERMUTATION_SIZE - 1)]) & (PERMUTATION_SIZE - 1)];
        return getGradients()[index];
    }

    /**
     * Returns the permutation array used for accessing the gradient vectors.
     *
     * @return the permutation array used for accessing the gradient vectors
     */
    public int[] getP() {
        return p;
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
        var that = (ImprovedPerlinNoise) o;
        return Arrays.equals(p, that.p);
    }

    @Override
    public int hashCode() {
        if (hashCode == -1) {
            hashCode = 31 * super.hashCode() + Arrays.hashCode(p);
        }
        return hashCode;
    }
}
