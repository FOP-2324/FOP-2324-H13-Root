package h13.ui.controls;

import javafx.util.StringConverter;

import java.util.regex.Pattern;

/**
 * A {@link NumberField} that only accepts {@link Long} values.
 */
public class LongField extends IntegerField {

    /**
     * Creates a long field that accepts any {@link Long} value.
     */
    public LongField() {
        this(ANY);
    }

    /**
     * Creates a long field with the given pattern.
     *
     * @param pattern The pattern to use for validating the input.
     */
    public LongField(Pattern pattern) {
        super(pattern);
    }

    @Override
    public StringConverter<Number> getConverter() {
        // TODO H3.2
        return new NumberStringConverter(value -> Long.toString(value.longValue()), Long::parseLong);
    }
}
