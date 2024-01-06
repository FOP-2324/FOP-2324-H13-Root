package h13.test;

import h13.Package;
import h13.noise.AbstractPerlinNoise;
import h13.serialization.Point2DArrayConverter;
import h13.utils.Links;
import h13.utils.TutorAssertions;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.List;
import java.util.Random;

/**
 * Defines unit tests for task H1.1.
 *
 * @author Nhan Huynh
 */
@DisplayName("H1.1: Gradienten")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class H1_1_TutorTests {

    /**
     * The package containing the methods to test.
     */
    private static final Package PACKAGE = Package.NOISE;

    /**
     * The class containing the methods to test.
     */
    private static final Class<?> CLASS = AbstractPerlinNoise.class;

    /**
     * The type link to the class containing the methods to test.
     */
    private TypeLink type;

    /**
     * Sets up the needed components for the tests.
     */
    @BeforeAll
    public void globalSetup() {
        type = Links.getType(PACKAGE, CLASS);
    }

    /**
     * Defines unit tests for AbstractPerlinNoise#createGradients(int, int).
     */
    @DisplayName("createGradients(int, int)")
    @Nested
    @Order(0)
    @TestMethodOrder(MethodOrderer.DisplayName.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class CreateGradientsTest {

        /**
         * The method to test.
         */
        private MethodLink method;

        /**
         * Sets up the needed components for the tests.
         */
        @BeforeAll
        public void globalSetup() {
            List<TypeLink> parameters = Links.convertParameters(int.class, int.class);
            method = Links.getMethod(type, "createGradients", Matcher.of(m -> m.typeList().equals(parameters)));
        }

        /**
         * Tests whether the gradients are within the unit circle.
         *
         * @param width  the width of the noise
         * @param height the height of the noise
         * @throws Exception if an error occurs during the invocation of the method
         */
        @DisplayName("01 | createGradients(int, int) enthält nur Punkte innerhalb des Einheitskreises.")
        @ParameterizedTest(name = "createGradients({0}, {1})")
        @JsonClasspathSource()
        public void testCoordinateRange(
            @Property("width") int width,
            @Property("height") int height
        ) throws Exception {
            AbstractPerlinNoise noise = new MockAbstractPerlinNoise(width, height);
            Point2D[] gradients = method.invoke(noise, width, height);
            for (int i = 0; i < gradients.length; i++) {
                Point2D gradient = gradients[i];
                Context context = Assertions2.contextBuilder()
                    .subject(method)
                    .add("i", i)
                    .add("Expected g(i)", "Point2D [x = [-1, 1], y = [-1, 1]]")
                    .add("Actual g(i)", gradient)
                    .build();
                TutorAssertions.assertWithin(gradient.getX(), -1, 1, context);
                TutorAssertions.assertWithin(gradient.getY(), -1, 1, context);
            }
        }

        /**
         * Tests whether the gradient array has the correct length.
         *
         * @param width  the width of the noise
         * @param height the height of the noise
         * @param length the expected length of the gradient array
         * @throws Exception if an error occurs during the invocation of the method
         */
        @DisplayName("02 | createGradients(int, int) gibt ein Array mit der korrekten Länge zurück.")
        @ParameterizedTest(name = "createGradients({0}, {1}).length = {2}")
        @JsonClasspathSource()
        public void testArraySize(
            @Property("width") int width,
            @Property("height") int height,
            @Property("length") int length
        ) throws Exception {
            AbstractPerlinNoise noise = new MockAbstractPerlinNoise(width, height);
            Point2D[] gradients = method.invoke(noise, width, height);
            Context context = Assertions2.contextBuilder()
                .subject(method)
                .add("width", width)
                .add("height", height)
                .add("Expected length", width * height)
                .add("Actual length", length)
                .build();
            Assertions2.assertEquals(length, gradients.length, context,
                result -> "Expected length %s, but was %s.".formatted(length, result.object()));
        }

    }

    /**
     * Defines unit tests for {@link AbstractPerlinNoise#getGradient(int, int)}.
     */
    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.DisplayName.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class GetGradientTest {

        /**
         * The method to test.
         */
        private MethodLink method;

        /**
         * The field containing the gradients.
         */
        private FieldLink field;

        /**
         * Sets up the needed components for the tests.
         */
        @BeforeAll
        public void globalSetup() {
            List<TypeLink> parameters = Links.convertParameters(int.class, int.class);
            method = Links.getMethod(type, "getGradient", Matcher.of(m -> m.typeList().equals(parameters)));
            field = Links.getField(type, "gradients");
        }

        /**
         * Tests whether the correct gradient is returned. The test will use custom gradients in order to avoid
         * randomness.
         *
         * @param width     the width of the noise
         * @param height    the height of the noise
         * @param gradients the gradients of the noise
         * @param x         the x-coordinate of the gradient
         * @param y         the y-coordinate of the gradient
         * @param index     the index of the gradient in the array
         */
        @DisplayName("03 | getGradient(int, int) gibt die korrekten Gradienten zurück.")
        @ParameterizedTest(name = "getGradient({2}, {3}) = gradients[{4}]")
        @JsonClasspathSource()
        public void testCorrectElement(
            @Property("width") int width,
            @Property("height") int height,
            @Property("gradients") @ConvertWith(Point2DArrayConverter.class) Point2D[] gradients,
            @Property("x") int x,
            @Property("y") int y,
            @Property("index") int index
        ) {
            AbstractPerlinNoise noise = new MockAbstractPerlinNoise(width, height);
            field.set(noise, gradients);

            Point2D gradient = noise.getGradient(x, y);
            Context context = Assertions2.contextBuilder()
                .subject(method)
                .add("Gradients", gradients)
                .add("x", x)
                .add("y", y)
                .add("Expected gradient", gradients[index])
                .add("Actual gradient", gradient)
                .build();

            Assertions2.assertEquals(gradients[index], gradient, context, result -> "Expected gradient %s, but was %s."
                .formatted(gradients[index], result.object()));
        }

    }


    /**
     * A mock implementation of {@link AbstractPerlinNoise}. We will only test the methods that are implemented in
     * this class.
     */
    private static class MockAbstractPerlinNoise extends AbstractPerlinNoise {

        /**
         * Constructs a perlin noise with the given width and height.
         *
         * @param width  the width of the noise
         * @param height the height of the noise
         */
        public MockAbstractPerlinNoise(int width, int height) {
            super(width, height, new Random(0));
        }

        @Override
        public double compute(int x, int y) {
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
