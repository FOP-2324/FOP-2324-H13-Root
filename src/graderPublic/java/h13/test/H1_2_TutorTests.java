package h13.test;

import h13.Package;
import h13.noise.SimplePerlinNoise;
import h13.utils.Links;
import h13.utils.TutorAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.List;
import java.util.Random;

/**
 * Defines unit tests for task H1.2.
 *
 * @author Nhan Huynh
 */
@DisplayName("H1.2: Lineare Interpolation und Fading-Funktion")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class H1_2_TutorTests {

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
     * The method link to the interpolate method to test.
     */
    private MethodLink interpolate;

    /**
     * The method link to the fade method to test.
     */
    private MethodLink fade;

    /**
     * Sets up the needed components for the tests.
     */
    @BeforeAll
    public void globalSetup() {
        type = Links.getType(PACKAGE, CLASS);

        List<TypeLink> interpolateParameters = Links.convertParameters(double.class, double.class, double.class);
        interpolate = Links.getMethod(type, "interpolate", Matcher.of(m -> m.typeList().equals(interpolateParameters)));

        List<TypeLink> fadeParameters = Links.convertParameters(double.class);
        fade = Links.getMethod(type, "fade", Matcher.of(m -> m.typeList().equals(interpolateParameters)));
    }

    /**
     * Tests the interpolate and fade methods.
     *
     * @param a            The first value to interpolate.
     * @param b            The second value to interpolate.
     * @param alpha        The alpha value to interpolate with.
     * @param interpolated The expected result of the interpolation.
     * @param t            The t value to fade with.
     * @param faded        The expected result of the fading.
     */
    @DisplayName("04 | Die Interpolations- und Fading-Funktionen funktionieren korrekt.")
    @ParameterizedTest(name = "interpolate({0}, {1}, {2}) = {3}, fade({4}) = {5}")
    @JsonClasspathSource()
    public void testResult(
        @Property("a") double a,
        @Property("b") double b,
        @Property("alpha") double alpha,
        @Property("interpolated") double interpolated,
        @Property("t") double t,
        @Property("faded") double faded
    ) {
        SimplePerlinNoise noise = new SimplePerlinNoise(0, 0, new Random(0));
        double actualInterpolated = noise.interpolate(a, b, alpha);
        Context contextInterpolate = Assertions2.contextBuilder()
            .subject(interpolate)
            .add("a", a)
            .add("b", b)
            .add("alpha", alpha)
            .add("Expected", interpolated)
            .add("Actual", actualInterpolated)
            .build();

        TutorAssertions.assertEquals(interpolated, actualInterpolated, EPSILON, contextInterpolate,
            result -> "Expected interpolate(%s, %s, %s) = %s, but was %s"
                .formatted(a, b, alpha, interpolated, result.object()));

        double actualFaded = noise.fade(t);
        Context contextFade = Assertions2.contextBuilder()
            .subject(fade)
            .add("t", t)
            .add("Expected", faded)
            .add("Actual", actualFaded)
            .build();

        TutorAssertions.assertEquals(faded, actualFaded, EPSILON, contextFade,
            result -> "Expected fade(%s) = %s, but was %s".formatted(t, faded, result.object()));
    }

}
