package h13.ui.controls;

import javafx.util.StringConverter;

import java.util.regex.Pattern;

/**
 * A {@link NumberField} that only accepts {@link Integer} values.
 */
public class IntegerField extends NumberField {

    /**
     * A {@link Pattern} that only accepts positive {@link Integer} values.
     */
    public static final Pattern POSITIVE_ONLY = Pattern.compile("\\d*");

    /**
     * A {@link Pattern} that only accepts negative {@link Integer} values.
     */
    public static final Pattern NEGATIVE_ONLY = Pattern.compile("-" + POSITIVE_ONLY.pattern());

    /**
     * A {@link Pattern} that accepts both positive and negative {@link Integer} values.
     */
    public static final Pattern ANY = Pattern.compile("-?" + POSITIVE_ONLY.pattern());


    /**
     * Creates an integer field with the given pattern.
     *
     * @param pattern The pattern to use for validating the input.
     */
    public IntegerField(Pattern pattern) {
        super(pattern);
    }

    /**
     * Creates an integer field that accepts any {@link Integer} value.
     */
    public IntegerField() {
        this(ANY);
    }

    @Override
    public StringConverter<Number> getConverter() {
        return new NumberStringConverter(value -> Integer.toString(value.intValue()), Integer::parseInt);
    }
}
