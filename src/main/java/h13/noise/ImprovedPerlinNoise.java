package h13.noise;

import javafx.geometry.Point2D;

import java.util.Random;

/**
 * An improved implementation of the Perlin noise algorithm.
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
     * Constructs a ImprovedPerlinNoise object with the specified parameters.
     *
     * @param width       the width of the noise domain
     * @param height      the height of the noise domain
     * @param frequency   the frequency of the noise
     * @param seed        the random seed for generating gradient vectors
     * @param permutation the permutation array used for accessing the gradient vectors
     */
    public ImprovedPerlinNoise(int width, int height, double frequency, Random seed, int[] permutation) {
        super(width, height, frequency, seed);
        this.permutation = permutation;
    }

    /**
     * Constructs a ImprovedPerlinNoise object with the specified parameters.
     *
     * @param width       the width of the noise domain
     * @param height      the height of the noise domain
     * @param seed        the random seed for generating gradient vectors
     * @param permutation the permutation array used for accessing the gradient vectors
     */
    public ImprovedPerlinNoise(int width, int height, Random seed, int[] permutation) {
        super(width, height, seed);
        this.permutation = permutation;
    }

    /**
     * Constructs a ImprovedPerlinNoise object with the specified parameters.
     *
     * @param width     the width of the noise domain
     * @param height    the height of the noise domain
     * @param frequency the frequency of the noise
     * @param seed      the random seed for generating gradient vectors
     */
    public ImprovedPerlinNoise(int width, int height, double frequency, Random seed) {
        super(width, height, frequency, seed);
        this.permutation = createPermutation();
    }

    /**
     * Constructs a ImprovedPerlinNoise object with the specified parameters.
     *
     * @param width  the width of the noise domain
     * @param height the height of the noise domain
     * @param seed   the random seed for generating gradient vectors
     */
    public ImprovedPerlinNoise(int width, int height, Random seed) {
        super(width, height, seed);
        this.permutation = createPermutation();
    }

    /**
     * Creates a permutation array of the size 256 * 2, where the first 256 elements are the values from 0 to 255
     * randomly shuffled and the last 256 elements are the same as the first 256 elements but ordered in ascending
     * order.
     *
     * @return the permutation array
     */
    private int[] createPermutation() {
        Random seed = new Random(1);
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

}
