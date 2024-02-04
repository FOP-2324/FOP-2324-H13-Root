package h13.noise;

import com.fasterxml.jackson.databind.JsonNode;
import h13.rubric.TutorAssertions;
import h13.util.Links;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Map;
import java.util.function.Function;

@DisplayName("H1.2 | Lineare Interpolation und Fading-Funktion")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestForSubmission
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class H1_2_Tests extends H1_Tests {

    private SimplePerlinNoise noise;

    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.ofEntries(
        Map.entry("x1", JsonNode::asDouble),
        Map.entry("x2", JsonNode::asDouble),
        Map.entry("alpha", JsonNode::asDouble),
        Map.entry("t", JsonNode::asDouble),
        Map.entry("expectedInterpolate", JsonNode::asDouble),
        Map.entry("expectedFade", JsonNode::asDouble)
    );

    @BeforeAll
    public void globalSetup() {
        noise = Mockito.mock(SimplePerlinNoise.class, Mockito.CALLS_REAL_METHODS);
    }

    @Override
    public Map<String, String> getContextInformation() {
        return Map.ofEntries(
            Map.entry("x1", "The first value to interpolate"),
            Map.entry("x2", "The second value to interpolate"),
            Map.entry("alpha", "The interpolation factor"),
            Map.entry("t", "The value to which the fade function will be applied"),
            Map.entry("expectedInterpolate", "The expected interpolated value"),
            Map.entry("expectedFade", "The expected fading factor")
        );
    }

    @Override
    public TypeLink getTypeLink() {
        return BasicTypeLink.of(SimplePerlinNoise.class);
    }

    @DisplayName("Die Methoden interpolate(double, double, double) und fade(double) geben die korrekten Ergebnisse "
        + "zurück.")
    @Order(4)
    @ParameterizedTest
    @JsonParameterSetTest(value = "H1_2_Criterion.json", customConverters = CONVERTERS_FIELD_NAME)
    public void testFunctions(JsonParameterSet parameters) {
        testInterpolate(parameters);
        testFade(parameters);
    }

    @JsonParameterSetTest(value = "H1_2_Criterion.json", customConverters = CONVERTERS_FIELD_NAME)
    private void testInterpolate(JsonParameterSet parameters) {
        double x1 = parameters.get("x1");
        double x2 = parameters.get("x2");
        double alpha = parameters.get("alpha");
        double expected = parameters.get("expectedInterpolate");
        double actual = noise.interpolate(x1, x2, alpha);

        MethodLink methodLink = Links.getMethod(getTypeLink(), "interpolate");
        Context context = contextBuilder(methodLink, "testInterpolate")
            .add("x1", x1)
            .add("x2", x2)
            .add("alpha", alpha)
            .add("Expected result", expected)
            .add("Actual result", actual)
            .build();
        TutorAssertions.assertEquals(expected, actual, context);
    }

    @JsonParameterSetTest(value = "H1_2_Criterion.json", customConverters = CONVERTERS_FIELD_NAME)
    private void testFade(JsonParameterSet parameters) {
        double t = parameters.get("t");
        double expected = parameters.get("expectedFade");
        double actual = noise.fade(t);

        MethodLink methodLink = Links.getMethod(getTypeLink(), "fade");
        Context context = contextBuilder(methodLink, "testFade")
            .add("t", t)
            .add("Expected result", expected)
            .add("Actual result", actual)
            .build();
        TutorAssertions.assertEquals(expected, actual, context);
    }
}
