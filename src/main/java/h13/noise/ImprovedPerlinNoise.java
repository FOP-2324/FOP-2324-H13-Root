package h13.noise;

import javafx.geometry.Point2D;

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
    private static final int PERMUTATION_SIZE = 256;

    /**
     * The permutation array used for accessing the gradient vectors.
     */
    private final int[] permutation;

    /**
     * Constructs an improved Perlin noise with wrapping the underlying Perlin noise object.
     *
     * @param noise       the underlying Perlin noise object
     * @param permutation the permutation array used for accessing the gradient vectors
     */
    public ImprovedPerlinNoise(PerlinNoise noise, int[] permutation) {
        super(noise.width(), noise.height(), noise.frequency(), noise.seed());
        this.permutation = permutation;
    }

    /**
     * Constructs an improved Perlin noise with wrapping the underlying Perlin noise object.
     *
     * @param noise the underlying Perlin noise object
     */
    public ImprovedPerlinNoise(PerlinNoise noise) {
        this(noise, createPermutation(noise.seed()));
    }

    /**
     * Creates a permutation array of the size 256 * 2, where the first 256 elements are the values from 0 to 255
     * randomly shuffled and the last 256 elements are the same as the first 256 elements but ordered in ascending
     * order.
     *
     * @return the permutation array
     */
    private static int[] createPermutation(Random seed) {
        int[] permutation = new int[PERMUTATION_SIZE * 2];
        for (int i = 0; i < PERMUTATION_SIZE; i++) {
            permutation[i] = permutation[i + PERMUTATION_SIZE] = i;
        }
        for (int i = 0; i < PERMUTATION_SIZE; i++) {
            int j = seed.nextInt(0, PERMUTATION_SIZE);
            int temp = permutation[i];
            permutation[i] = permutation[j];
            permutation[j] = temp;
        }
        return permutation;
    }

    @Override
    public Point2D getGradient(int x, int y) {
        int index = permutation[(x + permutation[y & 255]) & 255];
        return gradients()[index];
    }

    /**
     * Returns the permutation array used for accessing the gradient vectors.
     *
     * @return the permutation array used for accessing the gradient vectors
     */
    public int[] permutation() {
        return permutation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ImprovedPerlinNoise that = (ImprovedPerlinNoise) o;
        return Arrays.equals(permutation, that.permutation);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(permutation);
        return result;
    }
}
