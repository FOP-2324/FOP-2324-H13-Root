package h13.ui.controls;

import javafx.util.StringConverter;

import java.util.regex.Pattern;

/**
 * A {@link NumberField} that only accepts {@link Double} values.
 *
 * @author Nhan Huynh
 */
public class DoubleField extends NumberField {

    /**
     * A {@link Pattern} that only accepts positive {@link Double} values.
     */
    public static final Pattern POSITIVE_ONLY = Pattern.compile("\\d*(\\.\\d*)?");

    /**
     * A {@link Pattern} that only accepts negative {@link Double} values.
     */
    public static final Pattern NEGATIVE_ONLY = Pattern.compile("-" + POSITIVE_ONLY.pattern());

    /**
     * A {@link Pattern} that accepts both positive and negative {@link Double} values.
     */
    public static final Pattern ANY = Pattern.compile("-?" + POSITIVE_ONLY.pattern());

    /**
     * Creates a double field with the given pattern.
     *
     * @param pattern The pattern to use for validating the input.
     */
    public DoubleField(Pattern pattern) {
        super(pattern);
    }

    /**
     * Creates a double field that accepts any {@link Double} value.
     */
    public DoubleField() {
        this(ANY);
    }

    @Override
    public StringConverter<Number> getConverter() {
        // TODO H3.2
        return new NumberStringConverter(value -> Double.toString(value.doubleValue()), Double::parseDouble);
    }
}
