package h13.ui.controls;

import h13.util.Links;
import javafx.util.StringConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

@DisplayName("H3.2 | Nummerfeld")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestForSubmission
public class H3_2_TestsPublic extends H3_Tests {

    private static final Number NUMBER_VALUE = 69;
    private static final String TEXT_VALUE = NUMBER_VALUE.toString();


    private NumberField createNumberField() {
        return new NumberField(Pattern.compile("\\d+")) {
            @Override
            @SuppressWarnings("unchecked")
            public StringConverter<Number> getConverter() {
                StringConverter<Number> converter = Mockito.mock(StringConverter.class);
                Mockito.when(converter.fromString(TEXT_VALUE)).thenReturn(NUMBER_VALUE);
                Mockito.when(converter.toString(NUMBER_VALUE)).thenReturn(TEXT_VALUE);
                return converter;
            }
        };
    }

    @DisplayName("Der Textwert (String) des Nummernfeldes entspricht immer den numerischen Wert (Number).")
    @Order(13)
    @Test
    public void TestBindingStringNumber() {
        NumberField field = createNumberField();
        field.setText(TEXT_VALUE);

        TypeLink typeLink = Links.getType(getPackageLink(), NumberField.class);
        MethodLink methodLink = Links.getMethod(typeLink, "initBindings");
        Context context = contextBuilder(methodLink, null).build();
        Assertions2.assertEquals(NUMBER_VALUE, field.getValue(), context,
            result -> "The numeric value was not correctly updated.");
    }

    @DisplayName("Der numerische Wert (Number) des Nummernfeldes entspricht immer den Textwert (String).")
    @Order(14)
    @Test
    public void TestBindingNumberString() {
        NumberField field = createNumberField();
        field.setValue(NUMBER_VALUE);

        TypeLink typeLink = Links.getType(getPackageLink(), NumberField.class);
        MethodLink methodLink = Links.getMethod(typeLink, "initBindings");
        Context context = contextBuilder(methodLink, null).build();
        Assertions2.assertEquals(TEXT_VALUE, field.getText(), context,
            result -> "The text value was not correctly updated.");
    }

    @DisplayName("Die Methode getConverter() in der Klasse IntegerField, LongField und DoubleField geben den jeweils "
        + "korrekten Converter zurück.")
    @Order(15)
    @Test
    public void testConverters() {
        Map<NumberField, Function<Number, Number>> data = Map.ofEntries(
            Map.entry(new DoubleField(), Number::doubleValue),
            Map.entry(new IntegerField(), Number::intValue),
            Map.entry(new LongField(), Number::longValue)
        );

        data.forEach((field, conversion) -> {
            TypeLink typeLink = Links.getType(getPackageLink(), field.getClass());
            MethodLink methodLink = Links.getMethod(typeLink, "getConverter");
            Context context = contextBuilder(methodLink, null).build();
            StringConverter<Number> converter = field.getConverter();
            Number expected = conversion.apply(NUMBER_VALUE);
            Number actual = converter.fromString(expected.toString());
            Assertions2.assertEquals(expected.getClass(), actual.getClass(), context,
                result -> "The conversion to %s is incorrect.".formatted(expected.getClass()));
        });
    }
}
