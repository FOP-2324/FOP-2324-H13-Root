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
    private final int[] permutationTable;

    /**
     * Constructs an improved Perlin noise with wrapping the underlying Perlin noise object.
     *
     * @param noise            the underlying Perlin noise object
     * @param permutationTable the permutation array used for accessing the gradient vectors
     * @throws IllegalArgumentException if the permutation array does not have the size {@value #PERMUTATION_SIZE} * 2
     */
    public ImprovedPerlinNoise(PerlinNoise noise, int[] permutationTable) {
        super(noise.getWidth(), noise.getHeight(), noise.getFrequency(), noise.getSeed());
        if (permutationTable.length != PERMUTATION_SIZE * 2) {
            throw new IllegalArgumentException("The permutation array must have the size %d * 2.".formatted(PERMUTATION_SIZE));
        }
        this.permutationTable = permutationTable;
    }

    /**
     * Constructs an improved Perlin noise with wrapping the underlying Perlin noise object.
     *
     * @param noise the underlying Perlin noise object
     */
    public ImprovedPerlinNoise(PerlinNoise noise) {
        this(noise, createPermutation(noise.getSeed()));
    }

    /**
     * Creates a permutation array of the size {@value #PERMUTATION_SIZE} * 2, where the first {@value #PERMUTATION_SIZE}
     * elements are the values from 0 to {@value #PERMUTATION_SIZE} randomly shuffled and the last
     * {@value #PERMUTATION_SIZE} elements are the same as the first {@value #PERMUTATION_SIZE} elements but ordered in
     * ascending order.
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
        int index = permutationTable[(x + permutationTable[y & (PERMUTATION_SIZE - 1)]) & (PERMUTATION_SIZE - 1)];
        return getGradients()[index];
    }

    /**
     * Returns the permutation array used for accessing the gradient vectors.
     *
     * @return the permutation array used for accessing the gradient vectors
     */
    public int[] getPermutationTable() {
        return permutationTable;
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
        return Arrays.equals(permutationTable, that.permutationTable);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(permutationTable);
        return result;
    }

}
