package h13.old;

import h13.Package;
import h13.noise.AbstractPerlinNoise;
import h13.noise.ImprovedPerlinNoise;
import h13.noise.PerlinNoise;
import h13.serialization.Point2DArrayConverter;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
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

import static h13.utils.Links.convertParameters;
import static h13.utils.Links.getConstructor;
import static h13.utils.Links.getField;
import static h13.utils.Links.getType;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertNotEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ImprovedPerlinNoiseTest {

    private static final Package PACKAGE = Package.NOISE;
    private static final Class<?> CLASS = ImprovedPerlinNoise.class;

    private TypeLink type;

    @BeforeAll
    public void globalSetup() {
        type = getType(PACKAGE, CLASS);
    }

    @Nested
    @Order(0)
    @TestMethodOrder(MethodOrderer.DisplayName.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class PermutationTest {

        private FieldLink field;

        @BeforeAll
        public void globalSetup() {
            field = getField(type, "permutationTable");
        }

        @DisplayName("Constructor(PerlinNoise) initialisiert die Permutationstabelle korrekt.")
        @Test
        public void testConstructor() {
            List<TypeLink> parameters = convertParameters(PerlinNoise.class);
            Matcher<ConstructorLink> matcher = Matcher.of(m -> m.typeList().equals(parameters));
            ConstructorLink constructor = getConstructor(type, matcher);
            int[] table = field.get();

            Context.Builder<?> context = contextBuilder()
                .subject(constructor)
                .add("Expected length", ImprovedPerlinNoise.PERMUTATION_SIZE * 2)
                .add("Actual length", table.length);

            assertEquals(ImprovedPerlinNoise.PERMUTATION_SIZE * 2, table.length, context.build(),
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

            assertEquals(firstHalf, sortedFirstHalf, context.build(),
                result -> "Expected first half to be sorted, but was %s".formatted(Arrays.toString(result.object())));

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
            assertNotEquals(secondHalf, sortedSecondHalf, context.build(),
                result -> "Expected second half to be randomly distributed, but was %s"
                    .formatted(Arrays.toString(result.object())));
            assertEquals(secondHalf.length, elements.size(), context.build(),
                result -> "Expected second half to contain elements from 0 to %s, but was %s"
                    .formatted(ImprovedPerlinNoise.PERMUTATION_SIZE - 1, result.object()));
        }

        @DisplayName("getGradient(int, int) gibt den korrekten Gradienten zurück.")
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
            ImprovedPerlinNoise noise = new ImprovedPerlinNoise(new AbstractPerlinNoise(width, height, new Random(0)) {
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

                @Override
                public double compute(int x, int y) {
                    throw new UnsupportedOperationException();
                }
            });
            field.set(noise, table);
            FieldLink gs = getField(type, "gradients");
            gs.set(noise, gradients);
            Point2D gradient = noise.getGradient(x, y);
            Context context = contextBuilder()
                .subject(field)
                .add("table", table)
                .add("x", x)
                .add("y", y)
                .add("Expected gradient", gradients[index])
                .add("Actual gradient", gradient)
                .build();
            assertEquals(gradients[index], gradient, context,
                result -> "Expected gradient %s, but was %s.".formatted(gradients[index], result.object()));
        }

    }
}
