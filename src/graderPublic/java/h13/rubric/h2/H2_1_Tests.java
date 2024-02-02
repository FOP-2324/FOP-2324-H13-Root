package h13.rubric.h2;

import com.fasterxml.jackson.databind.JsonNode;
import h13.json.JsonConverters;
import h13.noise.ImprovedPerlinNoise;
import h13.noise.PerlinNoise;
import h13.rubric.TutorUtils;
import h13.util.Links;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

@DisplayName("H2.1 | Permutationstabelle")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestForSubmission
public class H2_1_Tests extends H2_Tests {

    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.ofEntries(
        Map.entry("width", JsonNode::asInt),
        Map.entry("height", JsonNode::asInt),
        Map.entry("gradients", JsonConverters::toGradients),
        Map.entry("permutation", JsonConverters::toPermutation),
        Map.entry("x", JsonNode::asInt),
        Map.entry("y", JsonNode::asInt),
        Map.entry("expected", JsonConverters::toGradient)
    );

    @Override
    public Map<String, String> getContextInformation() {
        return Map.ofEntries(
            Map.entry("width", "The width of the noise domain"),
            Map.entry("height", "The height of the noise domain"),
            Map.entry("gradients", "The gradients of a noise domain"),
            Map.entry("permutation", "The permutation of the improved algorithm"),
            Map.entry("x", "The x-coordinate of the gradient"),
            Map.entry("y", "The y-coordinate of the gradient"),
            Map.entry("expected", "The expected result of the compute method")
        );
    }

    @Override
    public TypeLink getTypeLink() {
        return BasicTypeLink.of(ImprovedPerlinNoise.class);
    }

    private ImprovedPerlinNoise createNoise(JsonParameterSet parameters) {
        PerlinNoise delegate = Mockito.mock(PerlinNoise.class);
        Mockito.when(delegate.getRandomGenerator()).thenReturn(new Random(0));
        Mockito.when(delegate.getWidth()).thenReturn(parameters.get("width"));
        Mockito.when(delegate.getHeight()).thenReturn(parameters.get("height"));
        return new ImprovedPerlinNoise(delegate);
    }

    @DisplayName("Die Methode createPermutation(Random seed) erstellt eine korrekte Permutationstabelle.")
    @Order(8)
    @ParameterizedTest
    @JsonParameterSetTest(value = "H2_1_Criterion_01.json", customConverters = CONVERTERS_FIELD_NAME)
    public void testCreatePermutation(JsonParameterSet parameters) throws Throwable {
        ImprovedPerlinNoise noise = createNoise(parameters);
        MethodLink methodLink = Links.getMethod(getTypeLink(), "createPermutation");
        int[] p = methodLink.invoke(noise, new Random(0));

        Context context = contextBuilder(methodLink, "testCreatePermutation")
            .build();

        Assertions2.assertEquals(ImprovedPerlinNoise.PERMUTATION_SIZE * 2, p.length, context,
            result -> "Permutation size is incorrect.");

        int[] first = Arrays.copyOf(p, ImprovedPerlinNoise.PERMUTATION_SIZE);

        for (int i = 0; i < ImprovedPerlinNoise.PERMUTATION_SIZE; i++) {
            Assertions2.assertEquals(i, first[i], context,
                result -> "First half is not sorted.");
        }
        List<Integer> second = Arrays.stream(Arrays.copyOfRange(p, ImprovedPerlinNoise.PERMUTATION_SIZE, p.length))
            .boxed()
            .toList();
        Assertions2.assertTrue(Arrays.stream(first).boxed().toList().containsAll(second), context,
            result -> "Second does not contain all elements from 0 to 255.");
        List<Integer> secondSorted = new ArrayList<>(second);
        secondSorted.sort(Comparator.naturalOrder());
        Assertions2.assertNotEquals(secondSorted, second, context,
            result -> "Second half is not shuffled.");
    }

    @DisplayName("Die Methode getGradient(int, int) gibt den korrekten Gradienten an der Koordinate (x, y) zurück.")
    @Order(9)
    @ParameterizedTest
    @JsonParameterSetTest(value = "H2_1_Criterion_02.json", customConverters = CONVERTERS_FIELD_NAME)
    public void testGetGradient(JsonParameterSet parameters) {
        ImprovedPerlinNoise noise = createNoise(parameters);
        MethodLink methodLink = Links.getMethod(getTypeLink(), "getGradient");
        Point2D[] gradients = parameters.get("gradients");
        FieldLink gradientsLink = Links.getField(getTypeLink().superType().superType(), "gradients");
        gradientsLink.set(noise, gradients);
        int[] permutation = parameters.get("permutation");
        FieldLink permutationLink = Links.getField(getTypeLink(), "p");
        permutationLink.set(noise, permutation);

        int x = parameters.get("x");
        int y = parameters.get("y");

        Context context = contextBuilder(methodLink, "testGetGradient")
            .add("width", noise.getWidth())
            .add("height", noise.getHeight())
            .add("gradients", TutorUtils.toString(gradients))
            .add("p", Arrays.toString(permutation))
            .add("x", x)
            .add("y", y)
            .build();

        Point2D expected = parameters.get("expected");
        Point2D actual = noise.getGradient(x, y);
        Assertions2.assertEquals(expected, actual, context,
            result -> "The gradient g(%s, %s) is incorrect.".formatted(x, y));
    }
}
