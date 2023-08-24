package h13.ui.controls;

import javafx.util.StringConverter;

import java.util.regex.Pattern;

/**
 * A text field that only accepts longs.
 *
 * @author Nhan Huynh
 */
public class LongTextField extends NumberTextField {

    /**
     * A pattern that matches any positive long.
     */
    public static final Pattern POSITIVE = Pattern.compile("\\d*");

    /**
     * A pattern that matches any negative long.
     */
    public static final Pattern NEGATIVE = Pattern.compile("-" + POSITIVE.pattern());

    /**
     * A pattern that matches any long.
     */
    private static final Pattern LONG = Pattern.compile("-?" + POSITIVE.pattern());

    /**
     * Creates a new long valued text field which accepts longs that match the given pattern.
     *
     * @param pattern the pattern that is used to validate the input
     */
    public LongTextField(Pattern pattern) {
        super(pattern, new LongStringConverter());
    }

    /**
     * Creates a new long valued text field that accepts any long.
     */
    public LongTextField() {
        this(LONG);
    }

    /**
     * A custom String converter that converts the input to a long.
     */
    private static class LongStringConverter extends StringConverter<Number> {

        @Override
        public String toString(Number value) {
            return value == null ? "" : Long.toString(value.longValue());
        }

        @Override
        public Number fromString(String value) {
            if (value == null) {
                return null;
            }

            value = value.trim();

            if (value.isEmpty()) {
                return null;
            }

            return value.equals("-") ? -1L : Long.parseLong(value);
        }

    }

}
