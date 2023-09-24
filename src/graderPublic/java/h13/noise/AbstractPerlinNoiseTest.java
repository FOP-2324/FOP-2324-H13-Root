package h13.noise;

import h13.Package;
import h13.serialization.Point2DArrayConverter;
import h13.utils.TutorAssertions;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junitpioneer.jupiter.json.Property;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.List;
import java.util.Random;

import static h13.utils.Links.field;
import static h13.utils.Links.method;
import static h13.utils.Links.parameters;
import static h13.utils.Links.type;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractPerlinNoiseTest {

    private static final Package PACKAGE = Package.NOISE;
    private static final Class<?> CLASS = AbstractPerlinNoise.class;

    private TypeLink type;

    @BeforeAll
    public void globalSetup() {
        type = type(PACKAGE, CLASS);
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class CreateGradientsTest {

        private MethodLink method;

        @BeforeAll
        public void globalSetup() {
            List<TypeLink> parameters = parameters(int.class, int.class);
            Matcher<MethodLink> matcher = Matcher.of(m -> m.typeList().equals(parameters));
            method = method(type, "createGradients", matcher);
        }

        @DisplayName("createGradients(int, int) enhält nur Punkte innerhalb des Einheitskreises.")
        @ParameterizedTest(name = "{0}x{1}")
        @Test
        public void testCoordinateRange(
            @Property("width") int width,
            @Property("height") int height
        ) throws Exception {
            AbstractPerlinNoise noise = new MockAbstractPerlinNoise(width, height);
            Point2D[] gradients = method.invoke(noise, width, height);
            for (int i = 0; i < gradients.length; i++) {
                Point2D gradient = gradients[i];
                Context context = contextBuilder()
                    .subject(method)
                    .add("i", i)
                    .add("Actual g(i)", gradient)
                    .add("Expected g(i)", "Point2D [x = [-1, 1], y = [-1, 1]]")
                    .build();
                TutorAssertions.assertWithin(gradient.getX(), -1, 1, context);
                TutorAssertions.assertWithin(gradient.getY(), -1, 1, context);
            }
        }

        @DisplayName("createGradients(int, int) gibt ein Array mit der korrekten Länge zurück.")
        @ParameterizedTest(name = "{0}x{1}")
        @Test
        public void testArraySize(
            @Property("width") int width,
            @Property("height") int height,
            @Property("length") int length
        ) throws Exception {
            AbstractPerlinNoise noise = new MockAbstractPerlinNoise(width, height);
            Point2D[] gradients = method.invoke(noise, width, height);
            Context context = contextBuilder()
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

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class GetGradientTest {

        private MethodLink method;
        private FieldLink field;

        @BeforeAll
        public void globalSetup() {
            List<TypeLink> parameters = parameters(int.class, int.class);
            Matcher<MethodLink> matcher = Matcher.of(m -> m.typeList().equals(parameters));
            method = method(type, "createGradients", matcher);
            field = field(type, "gradients");
        }

        @DisplayName("getGradient(int, int) gibt die korrekten Gradienten zurück.")
        @ParameterizedTest(name = "{0}x{1}; g({2}, {3}) = gradients[{4}]")
        @Test
        public void testCorrectElement(
            @Property("width") int width,
            @Property("height") int height,
            @Property("gradients") @ConvertWith(Point2DArrayConverter.class) Point2D[] gradients,
            @Property("x") int x,
            @Property("y") int y,
            @Property("index") int index
        ) throws Exception {
            AbstractPerlinNoise noise = new MockAbstractPerlinNoise(width, height);
            field.set(noise, gradients);

            Point2D gradient = noise.getGradient(x, y);
            Context context = contextBuilder()
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

    private static class MockAbstractPerlinNoise extends AbstractPerlinNoise {

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
