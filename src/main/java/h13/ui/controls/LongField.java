package h13.ui.controls;

import java.util.regex.Pattern;

/**
 * A {@link NumberField} that only accepts {@link Long} values.
 */
public class LongField extends IntegerField {

    /**
     * Creates an long field with the given pattern.
     *
     * @param pattern The pattern to use for validating the input.
     */
    public LongField(Pattern pattern) {
        super(pattern, new NumberStringConverter(value -> Long.toString(value.longValue()), Long::parseLong));
    }

    /**
     * Creates an long field that accepts any {@link Long} value.
     */
    public LongField() {
        this(ANY);
    }

}
