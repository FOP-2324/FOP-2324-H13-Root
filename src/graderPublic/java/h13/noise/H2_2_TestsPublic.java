package h13.noise;

import com.fasterxml.jackson.databind.JsonNode;
import h13.rubric.TutorAssertions;
import h13.util.Links;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

@DisplayName("H2.1 | Permutationstabelle")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestForSubmission
public class H2_2_TestsPublic extends H2_Tests {

    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.ofEntries(
        Map.entry("frequency", JsonNode::asDouble),
        Map.entry("amplitude", JsonNode::asDouble),
        Map.entry("octaves", JsonNode::asInt),
        Map.entry("persistence", JsonNode::asDouble),
        Map.entry("lacunarity", JsonNode::asDouble),
        Map.entry("x", JsonNode::asDouble),
        Map.entry("y", JsonNode::asDouble),
        Map.entry("expected", JsonNode::asDouble)
    );

    @Override
    public Map<String, String> getContextInformation() {
        return Map.ofEntries(
            Map.entry("x", "The x-coordinate of the gradient"),
            Map.entry("y", "The y-coordinate of the gradient"),
            Map.entry("frequency", "The frequency of the Perlin noise"),
            Map.entry("amplitude", "The amplitude of the noise, controlling the range of values for each octave"),
            Map.entry("octaves", "he number of octaves to use in the fractal noise computation"),
            Map.entry("persistence", "The persistence value which determines the amplitude decrease factor "
                + "between octaves"),
            Map.entry("lacunarity", "The lacunarity value which determines the frequency increase factor "
                + "between octaves"),
            Map.entry("gradients", "The gradients of a noise domain"),
            Map.entry("permutation", "The permutation of the improved algorithm"),
            Map.entry("expected", "The expected result of the compute method")
        );
    }

    @Override
    public TypeLink getTypeLink() {
        return BasicTypeLink.of(FractalPerlinNoise.class);
    }

    private PerlinNoise createNoise(JsonParameterSet parameters) {
        return new PerlinNoise() {
            @Override
            public Random getRandomGenerator() {
                return null;
            }

            @Override
            public Point2D[] getGradients() {
                return new Point2D[0];
            }

            @Override
            public Point2D getGradient(int x, int y) {
                return null;
            }

            @Override
            public double getFrequency() {
                return parameters.get("frequency");
            }

            @Override
            public void setFrequency(double frequency) {

            }

            @Override
            public double fade(double t) {
                return 0;
            }

            @Override
            public double interpolate(double x1, double x2, double alpha) {
                return 0;
            }

            @Override
            public double compute(double x, double y) {
                return x * y;
            }

            @Override
            public int getWidth() {
                return 0;
            }

            @Override
            public int getHeight() {
                return 0;
            }

            @Override
            public double compute(int x, int y) {
                return 0;
            }
        };
    }

    @DisplayName("Die Methoden compute(double, double) gibt den korrekten Rauschwert zurück.")
    @Order(10)
    @ParameterizedTest
    @JsonParameterSetTest(value = "H2_2.json", customConverters = CONVERTERS_FIELD_NAME)
    public void testResult(JsonParameterSet parameters) {
        PerlinNoise delegate = createNoise(parameters);
        FractalPerlinNoise noise = new FractalPerlinNoise(
            delegate,
            parameters.get("amplitude"),

            parameters.get("octaves"),

            parameters.get("lacunarity"),

            parameters.get("persistence")
        );

        double x = parameters.get("x");
        double y = parameters.get("y");

        double expected = parameters.get("expected");
        double actual = noise.compute(x, y);

        List<TypeLink> parameterLinks = Stream.of(double.class, double.class).<TypeLink>map(BasicTypeLink::of).toList();
        MethodLink methodLink = Links.getMethod(getTypeLink(), "compute",
            Matcher.of(method -> method.typeList().equals(parameterLinks)));
        Context context = contextBuilder(methodLink, "testResult")
            .add("frequency", noise.getFrequency())
            .add("amplitude", noise.getAmplitude())
            .add("octaves", noise.getOctaves())
            .add("persistence", noise.getPersistence())
            .add("lacunarity", noise.getLacunarity())
            .build();

        TutorAssertions.assertEquals(expected, actual, context);
    }
}
