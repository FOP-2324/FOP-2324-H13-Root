package h13.test;

import h13.Package;
import h13.noise.FractalPerlinNoise;
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
 * Defines unit tests for task H2.4.
 *
 * @author Nhan Huynh
 */
@DisplayName("H2.4: Fraktale")
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class H2_4_TutorTests {

    /**
     * The package containing the methods to test.
     */
    private static final Package PACKAGE = Package.NOISE;

    /**
     * The class containing the methods to test.
     */
    private static final Class<?> CLASS = FractalPerlinNoise.class;

    /**
     * The epsilon used for floating point comparisons.
     */
    private static final double EPSILON = 1e-6;

    /**
     * The type link to the class containing the methods to test.
     */
    private TypeLink type;

    /**
     * The method {@link FractalPerlinNoise#compute(double, double)} method to test.
     */
    private MethodLink compute;

    /**
     * Sets up the needed components for the tests.
     */
    @BeforeAll
    public void globalSetup() {
        type = Links.getType(PACKAGE, CLASS);
        List<TypeLink> parameters = Links.convertParameters(double.class, double.class);
        compute = Links.getMethod(type, "compute", Matcher.of(m -> m.typeList().equals(parameters)));
    }

    /**
     * Tests that the {@link FractalPerlinNoise#compute(double, double)} method delegates to the underlying noise.
     *
     * @param width       the width of the underlying noise
     * @param height      the height of the underlying noise
     * @param gradients   the gradients of the underlying noise
     * @param frequency   the frequency of the underlying noise
     * @param amplitude   the amplitude of the underlying noise
     * @param octaves     the octaves of the underlying noise
     * @param lacunarity  the lacunarity of the underlying noise
     * @param persistence the persistence of the underlying noise
     * @param x           the x coordinate to test
     * @param y           the y coordinate to test
     * @param expected    the expected result
     */
    @DisplayName("11 | compute(double, double) berechnet den Rauschwert korrekt.")
    @ParameterizedTest(name = "compute({8}, {9}) = {10}")
    @JsonClasspathSource()
    public void testResult(
        @Property("width") int width,
        @Property("height") int height,
        @Property("gradients") Point2D[] gradients,
        @Property("frequency") double frequency,
        @Property("amplitude") double amplitude,
        @Property("octaves") int octaves,
        @Property("lacunarity") double lacunarity,
        @Property("persistence") double persistence,
        @Property("x") double x,
        @Property("y") double y,
        @Property("expected") double expected
    ) {
        MockPerlinNoise underlying = new MockPerlinNoise(width, height, frequency);
        underlying.gradients = gradients;
        PerlinNoise noise = new FractalPerlinNoise(underlying, amplitude, octaves, lacunarity, persistence);

        double actual = noise.compute(x, y);
        Context contextDouble = Assertions2.contextBuilder()
            .subject(compute)
            .add("Gradients", Arrays.toString(gradients))
            .add("Frequency", frequency)
            .add("Amplitude", amplitude)
            .add("Octaves", octaves)
            .add("Lacunarity", lacunarity)
            .add("Persistence", persistence)
            .add("x", x)
            .add("y", y)
            .add("Expected", expected)
            .add("Actual", actual)
            .build();
        TutorAssertions.assertWithin(expected, actual, EPSILON, contextDouble);
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
         * The frequency of the noise.
         */
        private final double frequency;

        /**
         * Constructs a mock perlin noise object with the specified width and height.
         *
         * @param width     the width of the noise
         * @param height    the height of the noise
         * @param frequency the frequency of the noise
         */
        public MockPerlinNoise(int width, int height, double frequency) {
            this.width = width;
            this.height = height;
            this.frequency = frequency;
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
            return frequency;
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
