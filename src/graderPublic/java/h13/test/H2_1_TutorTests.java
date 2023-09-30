package h13.test;

import h13.Package;
import h13.noise.ImprovedPerlinNoise;
import h13.noise.PerlinNoise;
import h13.serialization.Point2DArrayConverter;
import h13.utils.Links;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.ConstructorLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Defines unit tests for task H2.1.
 *
 * @author Nhan Huynh
 */
@DisplayName("H2.1: Permutationstabelle")
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class H2_1_TutorTests {

    /**
     * The package containing the methods to test.
     */
    private static final Package PACKAGE = Package.NOISE;

    /**
     * The class containing the methods to test.
     */
    private static final Class<?> CLASS = ImprovedPerlinNoise.class;

    /**
     * The type link to the class containing the methods to test.
     */
    private TypeLink type;

    /**
     * The field containing the permutation table to test.
     */
    private FieldLink field;

    /**
     * Sets up the needed components for the tests.
     */
    @BeforeAll
    public void globalSetup() {
        type = Links.getType(PACKAGE, CLASS);
        field = Links.getField(type, "permutationTable");
    }

    /**
     * Tests whether the constructor initializes the permutation table correctly.
     */
    @DisplayName("07 | Constructor(PerlinNoise) initialisiert die Permutationstabelle korrekt.")
    @Test
    public void testConstructor() {
        ImprovedPerlinNoise noise = new ImprovedPerlinNoise(new MockPerlinNoise(100, 100, new Random(0)));
        List<TypeLink> parameters = Links.convertParameters(PerlinNoise.class);
        ConstructorLink constructor = Links.getConstructor(type, Matcher.of(m -> m.typeList().equals(parameters)));
        int[] table = field.get(noise);

        Context.Builder<?> context = Assertions2.contextBuilder()
            .subject(constructor)
            .add("Expected length", ImprovedPerlinNoise.PERMUTATION_SIZE * 2)
            .add("Actual length", table.length);

        Assertions2.assertEquals(ImprovedPerlinNoise.PERMUTATION_SIZE * 2, table.length, context.build(),
            result -> "Expected length %s, but was %s".formatted(ImprovedPerlinNoise.PERMUTATION_SIZE * 2,
                result.object()));
        int[] firstHalf = new int[ImprovedPerlinNoise.PERMUTATION_SIZE];
        int[] sortedFirstHalf = new int[ImprovedPerlinNoise.PERMUTATION_SIZE];
        System.arraycopy(table, 0, firstHalf, 0, ImprovedPerlinNoise.PERMUTATION_SIZE);
        System.arraycopy(table, ImprovedPerlinNoise.PERMUTATION_SIZE, sortedFirstHalf, 0,
            ImprovedPerlinNoise.PERMUTATION_SIZE);
        Arrays.sort(sortedFirstHalf);

        context.add("Expected first half", sortedFirstHalf)
            .add("Actual first half", firstHalf);

        Assertions2.assertEquals(
            Arrays.stream(firstHalf).boxed().toList(),
            Arrays.stream(sortedFirstHalf).boxed().toList(),
            context.build(),
            result -> "Expected first half to be sorted, but was %s".formatted(result.object()));

        int[] secondHalf = new int[ImprovedPerlinNoise.PERMUTATION_SIZE];
        int[] sortedSecondHalf = new int[ImprovedPerlinNoise.PERMUTATION_SIZE];
        System.arraycopy(table, ImprovedPerlinNoise.PERMUTATION_SIZE, secondHalf, 0,
            ImprovedPerlinNoise.PERMUTATION_SIZE);
        System.arraycopy(table, 0, sortedSecondHalf, 0, ImprovedPerlinNoise.PERMUTATION_SIZE);
        Arrays.sort(sortedSecondHalf);
        Set<Integer> elements = new LinkedHashSet<>(secondHalf.length);
        for (int i : secondHalf) {
            elements.add(i);
        }
        context.add("Actual second half", elements);
        Assertions2.assertNotEquals(Arrays.stream(secondHalf).boxed().toList(),
            Arrays.stream(sortedSecondHalf).boxed().toList(),
            context.build(),
            result -> "Expected second half to be randomly distributed, but was %s"
                .formatted(result.object()));
        Assertions2.assertEquals(secondHalf.length, elements.size(), context.build(),
            result -> "Expected second half to contain elements from 0 to %s, but was %s"
                .formatted(ImprovedPerlinNoise.PERMUTATION_SIZE - 1, result.object()));
    }

    /**
     * Tests whether the method {@link ImprovedPerlinNoise#getGradient(int, int)} returns the correct gradient.
     *
     * @param width     the width of the noise
     * @param height    the height of the noise
     * @param gradients the gradients of the noise
     * @param table     the permutation table of the noise
     * @param x         the x coordinate to get the gradient
     * @param y         the y coordinate to get the gradient
     * @param index     the expected index of the gradient
     */
    @DisplayName("08 | getGradient(int, int) gibt den korrekten Gradienten zurück.")
    @ParameterizedTest(name = "g({0}, {1}) = gradient[{2}]")
    @JsonClasspathSource()
    public void testCorrectIndex(
        @Property("width") int width,
        @Property("height") int height,
        @Property("gradients") @ConvertWith(Point2DArrayConverter.class) Point2D[] gradients,
        @Property("table") int[] table,
        @Property("x") int x,
        @Property("y") int y,
        @Property("index") int index
    ) {
        ImprovedPerlinNoise noise = new ImprovedPerlinNoise(new MockPerlinNoise(width, height, new Random(0)), table);
        field.set(noise, table);
        FieldLink gs = Links.getField(type, "gradients");
        gs.set(noise, gradients);
        Point2D gradient = noise.getGradient(x, y);
        Context context = Assertions2.contextBuilder()
            .subject(field)
            .add("table", table)
            .add("x", x)
            .add("y", y)
            .add("Expected gradient", gradients[index])
            .add("Actual gradient", gradient)
            .build();
        Assertions2.assertEquals(gradients[index], gradient, context,
            result -> "Expected gradient %s, but was %s.".formatted(gradients[index], result.object()));
    }

    /**
     * A mock perlin noise class for testing.
     */
    private static class MockPerlinNoise implements PerlinNoise {

        /**
         * The width of the noise.
         */
        private final int width;

        /**
         * The height of the noise.
         */
        private final int height;

        /**
         * The random seed for generating gradient vectors.
         */
        private final Random seed;

        /**
         * The gradient vectors. This will be overridden by the test.
         */

        private final Point2D[] gradients = new Point2D[0];

        /**
         * Constructs a mock perlin noise object with the specified width, height and seed.
         *
         * @param width  the width of the noise
         * @param height the height of the noise
         * @param seed   the random seed for generating gradient vectors
         */
        public MockPerlinNoise(int width, int height, Random seed) {
            this.width = width;
            this.height = height;
            this.seed = seed;
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public double compute(int x, int y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Random getSeed() {
            return seed;
        }

        @Override
        public Point2D[] getGradients() {
            return gradients;
        }

        @Override
        public Point2D getGradient(int x, int y) {
            return gradients[width * y + x];
        }

        @Override
        public double getFrequency() {
            return 0;
        }

        @Override
        public void setFrequency(double frequency) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double fade(double t) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double interpolate(double y1, double y2, double alpha) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double compute(double x, double y) {
            throw new UnsupportedOperationException();
        }

    }

}
