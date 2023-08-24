package h13.ui.controls;

import javafx.util.StringConverter;

import java.util.regex.Pattern;

/**
 * A text field that only accepts doubles.
 *
 * @author Nhan Huynh
 */
public class DoubleTextField extends NumberTextField {

    /**
     * A pattern that matches any positive doubles.
     */
    public static final Pattern POSITIVE = Pattern.compile("\\d*(\\.\\d*)?");

    /**
     * A pattern that matches any negative doubles.
     */
    public static final Pattern NEGATIVE = Pattern.compile("-" + POSITIVE.pattern());

    /**
     * A pattern that matches any doubles.
     */
    private static final Pattern DOUBLE = Pattern.compile("-?" + POSITIVE.pattern());

    /**
     * Creates a new double valued text field which accepts doubles that match the given pattern.
     *
     * @param pattern the pattern that is used to validate the input
     */
    public DoubleTextField(Pattern pattern) {
        super(pattern, new DoubleStringConverter());
    }

    /**
     * Creates a new double valued text field that accepts any double.
     */
    public DoubleTextField() {
        this(DOUBLE);
    }

    /**
     * A custom String converter that converts the input to a double.
     */
    private static class DoubleStringConverter extends StringConverter<Number> {

        @Override
        public String toString(Number value) {
            return value == null ? "" : Double.toString(value.doubleValue());
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

            return value.equals("-") ? -1d : Double.parseDouble(value);
        }

    }

}
