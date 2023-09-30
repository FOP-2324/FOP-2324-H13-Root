package h13.test;

import h13.Package;
import h13.noise.SimplePerlinNoise;
import h13.serialization.Point2DArrayConverter;
import h13.utils.Links;
import h13.utils.TutorAssertions;
import javafx.geometry.Point2D;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static h13.utils.Links.getField;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

/**
 * Defines unit tests for task H1.3.
 *
 * @author Nhan Huynh
 */
@DisplayName("H1.3: Simpler Perlin-Noise Algorithmus")
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class H1_3_TutorTests {

    /**
     * The package containing the methods to test.
     */
    private static final Package PACKAGE = Package.NOISE;

    /**
     * The class containing the methods to test.
     */
    private static final Class<?> CLASS = SimplePerlinNoise.class;

    /**
     * The epsilon used for floating point comparisons.
     */
    private static final double EPSILON = 1e-6;

    /**
     * The type link to the class containing the methods to test.
     */
    private TypeLink type;

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
        type = Links.getType(PACKAGE, CLASS);

        List<TypeLink> parameters = Links.convertParameters(double.class, double.class);
        method = Links.getMethod(type, "compute", Matcher.of(m -> m.typeList().equals(parameters)));

        field = getField(type, "gradients");
    }

    /**
     * Tests whether the method {@link SimplePerlinNoise#compute(double, double)} uses the correct gradients.
     *
     * @param width     the width of the noise
     * @param height    the height of the noise
     * @param gradients the gradients of the noise
     * @param x         the x coordinate to compute
     * @param y         the y coordinate to compute
     * @param expected  the expected gradients
     */
    @DisplayName("05 | compute(double, double) verwendet die korrekten Gradienten.")
    @ParameterizedTest(name = "compute({3}, {4})")
    @JsonClasspathSource()
    public void testCorrectGradients(
        @Property("width") int width,
        @Property("height") int height,
        @Property("gradients") double[] gradients,
        @Property("x") double x,
        @Property("y") double y,
        @Property("expected") @ConvertWith(Point2DArrayConverter.class) Point2D[] expected) {
        Map<Pair<Integer, Integer>, Point2D> visited = new LinkedHashMap<>();
        SimplePerlinNoise noise = new SimplePerlinNoise(width, height, new Random(0)) {

            @Override
            public Point2D getGradient(int x, int y) {
                Point2D gradient = super.getGradient(x, y);
                visited.put(new Pair<>(x, y), gradient);
                return gradient;
            }

        };

        field.set(noise, gradients);
        noise.compute(x, y);

        Context context = contextBuilder()
            .subject(method)
            .add("Gradients", gradients)
            .add("x", x)
            .add("y", y)
            .add("Expected gradients", expected)
            .add("Actual gradients", visited.values())
            .build();
        Assertions2.assertEquals(List.of(expected), visited.values(), context,
            result -> "Expected gradients %s, but was %s.".formatted(expected, result.object()));
    }

    /**
     * Tests whether the method {@link SimplePerlinNoise#compute(double, double)} computes the correct noise value.
     *
     * @param width     the width of the noise
     * @param height    the height of the noise
     * @param gradients the gradients of the noise
     * @param x         the x coordinate to compute
     * @param y         the y coordinate to compute
     * @param expected  the expected noise value
     */
    @DisplayName("compute(double, double) berechnet den korrekten Rauschwert.")
    @ParameterizedTest(name = "compute({3}, {4}) = {5}")
    @JsonClasspathSource()
    public void testResult(
        @Property("width") int width,
        @Property("height") int height,
        @Property("gradients") double[] gradients,
        @Property("x") double x,
        @Property("y") double y,
        @Property("expected") double expected
    ) {
        SimplePerlinNoise noise = new SimplePerlinNoise(width, height, new Random(0));
        field.set(noise, gradients);
        double actual = noise.compute(x, y);

        Context context = contextBuilder()
            .subject(method)
            .add("Gradients", gradients)
            .add("x", x)
            .add("y", y)
            .add("Expected noise", expected)
            .add("Actual noise", actual)
            .build();
        TutorAssertions.assertEquals(expected, actual, EPSILON, context,
            result -> "Expected noise %s, but was %s.".formatted(expected, result.object()));
    }

}
