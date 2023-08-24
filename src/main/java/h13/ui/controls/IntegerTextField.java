package h13.ui.controls;

import javafx.util.StringConverter;

import java.util.regex.Pattern;

/**
 * A text field that only accepts integers.
 *
 * @author Nhan Huynh
 */
public class IntegerTextField extends NumberTextField {

    /**
     * A pattern that matches any positive integer.
     */
    public static final Pattern POSITIVE = Pattern.compile("\\d*");

    /**
     * A pattern that matches any negative integer.
     */
    public static final Pattern NEGATIVE = Pattern.compile("-" + POSITIVE.pattern());

    /**
     * A pattern that matches any integer.
     */
    private static final Pattern INTEGER = Pattern.compile("-?" + POSITIVE.pattern());

    /**
     * Creates a new integer valued text field which accepts doubles that match the given pattern.
     *
     * @param pattern the pattern that is used to validate the input
     */
    public IntegerTextField(Pattern pattern) {
        super(pattern, new IntegerStringConverter());
    }

    /**
     * Creates a new integer valued text field that accepts any integer.
     */
    public IntegerTextField() {
        this(INTEGER);
    }

    /**
     * A custom String converter that converts the input to an integer.
     */
    private static class IntegerStringConverter extends StringConverter<Number> {

        @Override
        public String toString(Number value) {
            return value == null ? "" : Integer.toString(value.intValue());
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

            return value.equals("-") ? -1 : Integer.parseInt(value);
        }

    }

}
