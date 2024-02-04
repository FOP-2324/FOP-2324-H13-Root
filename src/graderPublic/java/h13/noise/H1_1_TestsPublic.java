package h13.noise;

import com.fasterxml.jackson.databind.JsonNode;
import h13.json.JsonConverters;
import h13.rubric.TutorUtils;
import h13.util.Links;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.PreCommentSupplier;
import org.tudalgo.algoutils.tutor.general.assertions.ResultOfObject;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Map;
import java.util.Random;
import java.util.function.Function;

@DisplayName("H1.1 | Gradienten")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestForSubmission
public class H1_1_TestsPublic extends H1_Tests {

    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.ofEntries(
        Map.entry("width", JsonNode::asInt),
        Map.entry("height", JsonNode::asInt),
        Map.entry("n", JsonNode::asInt),
        Map.entry("gradients", JsonConverters::toGradients),
        Map.entry("x", JsonNode::asInt),
        Map.entry("y", JsonNode::asInt),
        Map.entry("expected", JsonConverters::toGradient)
    );

    @Override
    public Map<String, String> getContextInformation() {
        return Map.ofEntries(
            Map.entry("Package", "The package of the tested method"),
            Map.entry("Type", "The class of the tested method"),
            Map.entry("width", "The width of the noise domain"),
            Map.entry("height", "The height of the noise domain"),
            Map.entry("n", "The number of gradients to generate"),
            Map.entry("gradients", "The gradients of a noise domain"),
            Map.entry("x", "The x-coordinate of the gradient in the gradient domain"),
            Map.entry("y", "The y-coordinate of the gradient in the gradient domain"),
            Map.entry("expected", "The expected gradient to retrieve from g(x, y)")
        );
    }

    @Override
    public TypeLink getTypeLink() {
        return BasicTypeLink.of(AbstractPerlinNoise.class);
    }

    private void assertWithinUnitCircle(Point2D point, Context context) {
        double x = point.getX();
        double y = point.getY();
        PreCommentSupplier<ResultOfObject<Boolean>> comment =
            result -> "The gradient g(%s, %s) is not within the unit circle (Interval [-1, 1]).".formatted(x, y);
        Assertions2.assertTrue(x <= 1.0 && x >= -1.0, context, comment);
        Assertions2.assertTrue(y <= 1.0 && y >= -1.0, context, comment);
    }

    @DisplayName("Die Methode createGradient() gibt einen Gradienten mit zufälligen Koordinaten im Einheitskreis "
        + "zurück.")
    @Order(1)
    @ParameterizedTest
    @JsonParameterSetTest(value = "H1_1_Criterion_01.json", customConverters = CONVERTERS_FIELD_NAME)
    public void testCreateGradient(JsonParameterSet parameters) throws Throwable {
        int n = parameters.get("n");

        MethodLink methodLink = Links.getMethod(getTypeLink(), "createGradient");
        FieldLink fieldLink = Links.getField(getTypeLink(), "randomGenerator");
        AbstractPerlinNoise noise = Mockito.mock(AbstractPerlinNoise.class, Answers.CALLS_REAL_METHODS);
        fieldLink.set(noise, new Random(0));

        Context context = contextBuilder(methodLink, "testCreateGradient").add("n", n).build();
        for (int i = 0; i < n; i++) {
            Point2D gradient = methodLink.invoke(noise);
            assertWithinUnitCircle(gradient, context);
        }
    }

    @DisplayName("Die Methode createGradients(int, int) gibt einen Array von Gradienten zurück.")
    @Order(2)
    @ParameterizedTest
    @JsonParameterSetTest(value = "H1_1_Criterion_02.json", customConverters = CONVERTERS_FIELD_NAME)
    public void testCreateGradients(JsonParameterSet parameters) throws Throwable {
        int width = parameters.get("width");
        int height = parameters.get("height");
        int n = width * height;

        MethodLink methodLink = Links.getMethod(getTypeLink(), "createGradients");
        FieldLink fieldLink = Links.getField(getTypeLink(), "randomGenerator");
        AbstractPerlinNoise noise = Mockito.mock(AbstractPerlinNoise.class, Answers.CALLS_REAL_METHODS);
        fieldLink.set(noise, new Random(0));

        Context context = contextBuilder(methodLink, "testCreateGradients")
            .add("width", width)
            .add("height", height)
            .add("n", "width * height  = %s".formatted(n))
            .build();

        Point2D[] gradients = methodLink.invoke(noise, width, height);
        Assertions2.assertEquals(n, gradients.length, context, result -> "The array length is not correct.");
        for (Point2D gradient : gradients) {
            assertWithinUnitCircle(gradient, context);
        }
    }

    @DisplayName("Die Methode getGradient(int, int) gibt den korrekten Gradienten an der Koordinate (x, y) zurück.")
    @Order(3)
    @ParameterizedTest
    @JsonParameterSetTest(value = "H1_1_Criterion_03.json", customConverters = CONVERTERS_FIELD_NAME)
    public void testGetGradient(JsonParameterSet parameters) throws Throwable {
        Point2D[] gradients = parameters.get("gradients");
        int width = parameters.get("width");
        int height = parameters.get("height");
        int x = parameters.get("x");
        int y = parameters.get("y");

        MethodLink methodLink = Links.getMethod(getTypeLink(), "createGradients");
        AbstractPerlinNoise noise = Mockito.mock(AbstractPerlinNoise.class, Answers.CALLS_REAL_METHODS);
        FieldLink gradientsLink = Links.getField(getTypeLink(), "gradients");
        gradientsLink.set(noise, gradients);
        FieldLink widthLink = Links.getField(getTypeLink(), "width");
        widthLink.set(noise, width);
        FieldLink heightLink = Links.getField(getTypeLink(), "height");
        heightLink.set(noise, height);

        Point2D expected = parameters.get("expected");
        Point2D actual = noise.getGradient(x, y);

        Context context = contextBuilder(methodLink, "testGetGradient")
            .add("gradients", TutorUtils.toString(gradients))
            .add("x", x)
            .add("y", "y")
            .add("Expected gradient", expected)
            .add("Actual gradient", actual)
            .build();

        Assertions2.assertEquals(expected, actual, context,
            result -> "The gradient g(%s, %s) is incorrect".formatted(x, y));
    }
}
