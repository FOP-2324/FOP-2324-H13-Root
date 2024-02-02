package h13.rubric.h3;

import h13.ui.controls.NumberStringConverter;
import h13.util.Links;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

@DisplayName("3.1 | Converter")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestForSubmission
public class H3_1_Tests extends H3_Tests {

    TypeLink typeLink;

    @BeforeAll
    public void globalSetup() {
        typeLink = Links.getType(PACKAGE_LINK, NumberStringConverter.class);
    }

    @Override
    public Map<String, String> getContextInformation() {
        return Map.of();
    }

    private void assertToString(Number value, String expected) {
        NumberStringConverter converter = new NumberStringConverter(Object::toString, null);
        String actual = converter.toString(value);

        List<TypeLink> parameterLinks = Stream.of(Number.class).<TypeLink>map(BasicTypeLink::of).toList();
        MethodLink methodLink = Links.getMethod(typeLink, "toString",
            Matcher.of(method -> method.typeList().equals(parameterLinks)));
        Context context = contextBuilder(methodLink, null)
            .add("value", String.valueOf(value))
            .build();
        Assertions2.assertEquals(expected, actual, context,
            result -> "Expected %s, but got %s".formatted(expected, actual));
    }

    @DisplayName("Die Methode toString(Number) gibt das korrekte Ergebnis zurück.")
    @Order(11)
    @Test
    public void testToString() {
        assertToString(null, "");
        assertToString(1.0, "1.0");
        assertToString(1f, "1.0");
        assertToString(1, "1");
        assertToString(1L, "1");
    }

    private void assertFromValue(Function<String, Number> numericizer, String value, Number expected) {
        NumberStringConverter converter = new NumberStringConverter(null, numericizer);
        Number actual = converter.fromString(value);

        MethodLink methodLink = Links.getMethod(typeLink, "fromString");
        Context context = contextBuilder(methodLink, null)
            .add("value", String.valueOf(value))
            .build();
        Assertions2.assertEquals(expected, actual, context,
            result -> "Expected %s, but got %s".formatted(expected, actual));
    }

    @DisplayName("Die Methode fromString(String value) gibt das korrekte Ergebnis für einfache Eingaben zurück.")
    @Order(12)
    @Test
    public void testFromValue() {
        assertFromValue(Double::valueOf, null, null);
        assertFromValue(Double::valueOf, "", null);
        assertFromValue(Double::valueOf, " ", null);
        assertFromValue(Double::valueOf, "1", 1.0);
        assertFromValue(Double::valueOf, " 1 ", 1.0);
        assertFromValue(Double::valueOf, "-", -1.0);
        assertFromValue(Double::valueOf, "-1", -1.0);
        assertFromValue(Double::valueOf, "1", 1.0);
        assertFromValue(Long::valueOf, " 1 ", 1L);
        assertFromValue(Long::valueOf, "-", -1L);
        assertFromValue(Long::valueOf, "-1", -1L);
    }
}
