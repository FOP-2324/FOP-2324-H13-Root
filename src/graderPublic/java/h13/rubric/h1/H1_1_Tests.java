package h13.rubric.h1;

import h13.noise.AbstractPerlinNoise;
import h13.util.Links;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
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

import java.util.Random;

@DisplayName("H1.1 | Gradienten")
@TestForSubmission
public class H1_1_Tests extends H1_Tests {

    @Override
    public TypeLink getTypeLink() {
        return BasicTypeLink.of(AbstractPerlinNoise.class);
    }

    private void assertWithinUnitCircle(Point2D point, Context context) {
        double x = point.getX();
        double y = point.getY();
        PreCommentSupplier<ResultOfObject<Boolean>> comment =
            result -> "The gradient (%s, %s) is not within the unit circle (Interval [-1, 1]).".formatted(x, y);
        Assertions2.assertTrue(x <= 1.0 && x >= -1.0, context, comment);
        Assertions2.assertTrue(y <= 1.0 && y >= -1.0, context, comment);
    }

    @DisplayName("Die Methode createGradient() gibt einen Gradienten mit zufälligen Koordinaten im Einheitskreis zurück.")
    @Order(1)
    @ParameterizedTest
    @JsonParameterSetTest(value = "H1_1_Criterion_01.json", customConverters = CONVERTERS_FIELD_NAME)
    public void testCreateGradients(JsonParameterSet parameters) throws Throwable {
        int n = parameters.get("n");
        MethodLink methodLink = Links.getMethod(getTypeLink(), "createGradient");
        FieldLink fieldLink = Links.getField(getTypeLink(), "randomGenerator");
        AbstractPerlinNoise noise = Mockito.mock(AbstractPerlinNoise.class, Answers.CALLS_REAL_METHODS);
        fieldLink.set(noise, new Random(0));
        Context context = contextBuilder(methodLink, "H1_1_Criterion_01.json").add("n", n).build();
        for (int i = 0; i < n; i++) {
            Point2D gradient = methodLink.invoke(noise);
            assertWithinUnitCircle(gradient, context);
        }
    }
}
