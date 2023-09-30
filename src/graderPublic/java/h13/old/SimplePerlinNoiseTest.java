package h13.old;

import h13.Package;
import h13.noise.SimplePerlinNoise;
import h13.serialization.Point2DArrayConverter;
import h13.utils.TutorAssertions;
import javafx.geometry.Point2D;
import javafx.util.Pair;
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
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static h13.utils.Links.convertParameters;
import static h13.utils.Links.getField;
import static h13.utils.Links.getMethod;
import static h13.utils.Links.getType;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SimplePerlinNoiseTest {

    private static final double EPSILON = 1e-6;
    private static final Package PACKAGE = Package.NOISE;
    private static final Class<?> CLASS = SimplePerlinNoise.class;

    private TypeLink type;

    @BeforeAll
    public void globalSetup() {
        type = getType(PACKAGE, CLASS);
    }

    @Nested
    @Order(0)
    @TestMethodOrder(MethodOrderer.DisplayName.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class FunctionsTest {


        private MethodLink method1;

        private MethodLink method2;

        @BeforeAll
        public void globalSetup() {
            List<TypeLink> parameters1 = convertParameters(double.class, double.class, double.class);
            Matcher<MethodLink> matcher1 = Matcher.of(m -> m.typeList().equals(parameters1));
            method1 = getMethod(type, "interpolate", matcher1);

            List<TypeLink> parameters2 = convertParameters(double.class);
            Matcher<MethodLink> matcher2 = Matcher.of(m -> m.typeList().equals(parameters1));
            method2 = getMethod(type, "fade", matcher1);
        }

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
            Context contextInterpolate = contextBuilder()
                .subject(method1)
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
            Context contextFade = contextBuilder()
                .subject(method2)
                .add("t", t)
                .add("Expected", faded)
                .add("Actual", actualFaded)
                .build();

            TutorAssertions.assertEquals(faded, actualFaded, EPSILON, contextFade,
                result -> "Expected fade(%s) = %s, but was %s".formatted(t, faded, result.object()));
        }
    }

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.DisplayName.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class ComputeTest {

        private MethodLink method;
        private FieldLink field;

        @BeforeAll
        public void globalSetup() {
            List<TypeLink> parameters = convertParameters(double.class, double.class);
            Matcher<MethodLink> matcher = Matcher.of(m -> m.typeList().equals(parameters));
            method = getMethod(type, "compute", matcher);

            field = getField(type, "gradients");
        }

        @DisplayName("compute(double, double) verwendet die korrekten Gradienten.")
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
            assertEquals(List.of(expected), visited.values(), context,
                result -> "Expected gradients %s, but was %s.".formatted(expected, result.object()));
        }

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

}
