package h13.noise;

import com.fasterxml.jackson.databind.JsonNode;
import h13.json.JsonConverters;
import h13.rubric.TutorAssertions;
import h13.rubric.TutorUtils;
import h13.util.Links;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

@DisplayName("H1.2 | Lineare Interpolation und Fading-Funktion")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestForSubmission
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class H1_3_TestsPublic extends H1_Tests {

    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.ofEntries(
        Map.entry("x", JsonNode::asDouble),
        Map.entry("y", JsonNode::asDouble),
        Map.entry("gradients", JsonConverters::toGradients),
        Map.entry("expectedGradients", JsonConverters::toGradients),
        Map.entry("expectedInterpolations", node -> JsonConverters.toList(node, JsonNode::asDouble)),
        Map.entry("expectedResult", JsonNode::asDouble)
    );

    private MethodLink methodLink;

    @BeforeAll
    public void globalSetup() {
        List<TypeLink> parametersLink = List.of(BasicTypeLink.of(double.class), BasicTypeLink.of(double.class));
        methodLink = Links.getMethod(getTypeLink(), "compute", Matcher.of(method -> method.typeList().equals(parametersLink)));
    }

    @Override
    public Map<String, String> getContextInformation() {
        return Map.ofEntries(
            Map.entry("x", "The x-coordinate of the computed value"),
            Map.entry("y", "The y-coordinate of the computed value"),
            Map.entry("gradients", "The gradients of a noise domain"),
            Map.entry("expectedGradients", "The expected gradients to retrieve from g(x, y)"),
            Map.entry("expectedInterpolations", "The expected interpolated value"),
            Map.entry("expectedResult", "The expected result of the compute method")
        );
    }

    @Override
    public TypeLink getTypeLink() {
        return BasicTypeLink.of(SimplePerlinNoise.class);
    }

    private void initNoise(JsonParameterSet parameters, SimplePerlinNoise noise) {
        Point2D[] gradients = parameters.get("gradients");
        FieldLink gradientsLink = Links.getField(getTypeLink().superType(), "gradients");
        gradientsLink.set(noise, gradients);
    }

    @DisplayName("Die Methode compute(double x, double y) verwendet für die Berechnung die korrekten Gradienten.")
    @Order(5)
    @ParameterizedTest
    @JsonParameterSetTest(value = "H1_3_Criterion_01.json", customConverters = CONVERTERS_FIELD_NAME)
    public void testCorrectGradients(JsonParameterSet parameters) {
        List<Point2D> actual = new ArrayList<>();
        SimplePerlinNoise noise = new SimplePerlinNoise(0, 0, new Random()) {

            @Override
            public Point2D getGradient(int x, int y) {
                Point2D gradient = super.getGradient(x, y);
                actual.add(gradient);
                return gradient;
            }
        };
        initNoise(parameters, noise);

        double x = parameters.get("x");
        double y = parameters.get("y");

        noise.compute(x, y);

        Context context = contextBuilder(methodLink, "testCorrectGradients")
            .add("gradients", TutorUtils.toString(parameters.get("gradients")))
            .add("x", x)
            .add("y", y)
            .build();

        Comparator<Point2D> cmp = Comparator.comparing(Point2D::getX).thenComparing(Point2D::getY);
        actual.sort(cmp);
        List<Point2D> expected = new ArrayList<>(List.of(parameters.get("expectedGradients")));
        expected.sort(cmp);
        Assertions2.assertEquals(expected, actual, context,
            result -> "The gradients %s are incorrect".formatted(actual));
    }

    @DisplayName("Die Methode compute(double x, double y) berechnet die korrekten Interpolationen lx0 und lx1")
    @Order(6)
    @ParameterizedTest
    @JsonParameterSetTest(value = "H1_3_Criterion_02.json", customConverters = CONVERTERS_FIELD_NAME)
    public void testCorrectInterpolations(JsonParameterSet parameters) {
        List<Double> actual = new ArrayList<>();
        SimplePerlinNoise noise = new SimplePerlinNoise(0, 0, new Random()) {

            @Override
            public double interpolate(double x1, double x2, double alpha) {
                double interpolated = super.interpolate(x1, x2, alpha);
                actual.add(interpolated);
                return interpolated;
            }
        };
        initNoise(parameters, noise);
        double x = parameters.get("x");
        double y = parameters.get("y");
        noise.compute(x, y);
        Context context = contextBuilder(methodLink, "testCorrectInterpolations")
            .add("gradients", TutorUtils.toString(parameters.get("gradients")))
            .add("x", x)
            .add("y", y)
            .build();
        List<Double> expected = parameters.get("expectedInterpolations");
        TutorAssertions.assertContains(expected, actual, context);
    }

    @DisplayName("Die Methode compute(double x, double y) gibt einen korrekten Rauschwert zurück.")
    @Order(8)
    @ParameterizedTest
    @JsonParameterSetTest(value = "H1_3_Criterion_03.json", customConverters = CONVERTERS_FIELD_NAME)
    public void testResult(JsonParameterSet parameters) {
        SimplePerlinNoise noise = new SimplePerlinNoise(0, 0, new Random());
        initNoise(parameters, noise);
        double x = parameters.get("x");
        double y = parameters.get("y");
        double expected = parameters.get("expectedResult");
        double actual = noise.compute(x, y);

        Context context = contextBuilder(methodLink, "testResult")
            .add("gradients", TutorUtils.toString(parameters.get("gradients")))
            .add("x", x)
            .add("y", y)
            .build();
        TutorAssertions.assertEquals(expected, actual, context);
    }
}
