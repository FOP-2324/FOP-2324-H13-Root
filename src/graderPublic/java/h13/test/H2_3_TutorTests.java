package h13.test;

import h13.Package;
import h13.noise.NormalizedPerlinNoise;
import h13.noise.PerlinNoise;
import h13.utils.Links;
import h13.utils.TutorAssertions;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Defines unit tests for task H2.3.
 *
 * @author Nhan Huynh
 */
@DisplayName("H2.3: Normalisierung")
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class H2_3_TutorTests {

    /**
     * The package containing the methods to test.
     */
    private static final Package PACKAGE = Package.NOISE;

    /**
     * The class containing the methods to test.
     */
    private static final Class<?> CLASS = NormalizedPerlinNoise.class;

    /**
     * The epsilon used for floating point comparisons.
     */
    private static final double EPSILON = 1e-6;

    /**
     * The type link to the class containing the methods to test.
     */
    private TypeLink type;

    /**
     * The method {@link NormalizedPerlinNoise#compute(int, int)} method to test.
     */
    private MethodLink computeInt;

    /**
     * The method {@link NormalizedPerlinNoise#compute(double, double)} method to test.
     */
    private MethodLink computeDouble;

    /**
     * Sets up the needed components for the tests.
     */
    @BeforeAll
    public void globalSetup() {
        type = Links.getType(PACKAGE, CLASS);
        List<TypeLink> typesInt = Links.convertParameters(int.class, int.class);
        List<TypeLink> typesDouble = Links.convertParameters(double.class, double.class);
        computeInt = Links.getMethod(type, "compute", Matcher.of(m -> m.typeList().equals(typesInt)));
        computeDouble = Links.getMethod(type, "compute", Matcher.of(m -> m.typeList().equals(typesDouble)));
    }

    /**
     * Tests that the result of the compute methods is normalized.
     *
     * @param width          the width of the noise
     * @param height         the height of the noise
     * @param gradients      the gradients of the noise
     * @param xInt           the integer x coordinate to test
     * @param yInt           the integer y coordinate to test
     * @param xDouble        the double x coordinate to test
     * @param yDouble        the double y coordinate to test
     * @param expectedInt    the expected noise value for the integer coordinates
     * @param expectedDouble the expected noise value for the double coordinates
     */
    @DisplayName("10 | compute(int, int) und compute(double, double) normalisieren die Werte korrekt.")
    @ParameterizedTest(name = "compute({3}, {4}) = {7}, compute({5}, {6}) = {8}")
    @JsonClasspathSource()
    public void testResult(
        @Property("width") int width,
        @Property("height") int height,
        @Property("gradients") Point2D[] gradients,
        @Property("xInt") int xInt,
        @Property("yInt") int yInt,
        @Property("xDouble") double xDouble,
        @Property("yDouble") double yDouble,
        @Property("expectedInt") int expectedInt,
        @Property("expectedDouble") double expectedDouble
    ) {
        MockPerlinNoise underlying = new MockPerlinNoise(width, height);
        underlying.gradients = gradients;
        PerlinNoise noise = new NormalizedPerlinNoise(underlying);

        double actualInt = noise.compute(xInt, yInt);
        Context contextInt = Assertions2.contextBuilder()
            .subject(computeInt)
            .add("Gradients", Arrays.toString(gradients))
            .add("x", xInt)
            .add("y", yInt)
            .add("Expected", expectedInt)
            .add("Actual", actualInt)
            .build();
        TutorAssertions.assertWithin(expectedInt, actualInt, EPSILON, contextInt);

        double actualDouble = noise.compute(xDouble, yDouble);
        Context contextDouble = Assertions2.contextBuilder()
            .subject(computeDouble)
            .add("Gradients", Arrays.toString(gradients))
            .add("x", xDouble)
            .add("y", yDouble)
            .add("Expected", expectedDouble)
            .add("Actual", actualDouble)
            .build();
        TutorAssertions.assertWithin(expectedDouble, actualDouble, EPSILON, contextDouble);
    }

    /**
     * A mock perlin noise where the compute method returns the magnitude of the gradient vector.
     */
    private static class MockPerlinNoise implements PerlinNoise {

        /**
         * The gradients of the noise. This will be overwritten by the test.
         */
        public Point2D[] gradients = new Point2D[0];

        /**
         * The width of the noise.
         */
        private final int width;

        /**
         * The height of the noise.
         */
        private final int height;

        /**
         * Constructs a mock perlin noise object with the specified width and height.
         *
         * @param width  the width of the noise
         * @param height the height of the noise
         */
        public MockPerlinNoise(int width, int height) {
            this.width = width;
            this.height = height;
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
            return gradients[getWidth() * y + x].magnitude();
        }

        @Override
        public Random getSeed() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Point2D[] getGradients() {
            return gradients;
        }

        @Override
        public Point2D getGradient(int x, int y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getFrequency() {
            throw new UnsupportedOperationException();
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
            return compute((int) Math.ceil(x), (int) Math.floor(y));
        }

    }

}
