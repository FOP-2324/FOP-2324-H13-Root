package h13.ui.controls;

import javafx.util.StringConverter;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.util.function.Function;

/**
 * A {@link StringConverter} that converts between {@link Number} and {@link String}.
 *
 * @author Nhan Huynh
 */
public class NumberStringConverter extends StringConverter<Number> {

    /**
     * A {@link Function} that converts a {@link Number} to a {@link String}.
     */
    private final Function<Number, String> stringifier;

    /**
     * A {@link Function} that converts a {@link String} to a {@link Number}.
     */
    private final Function<String, Number> numericizer;

    /**
     * Constructs a number/string converter with the given functions for converting between {@link Number} and
     * {@link String} and vice versa.
     *
     * @param stringifier a function that converts a {@link Number} to a {@link String}
     * @param numericizer a function that converts a {@link String} to a {@link Number}
     */
    public NumberStringConverter(Function<Number, String> stringifier, Function<String, Number> numericizer) {
        this.stringifier = stringifier;
        this.numericizer = numericizer;
    }

    @Override
    @StudentImplementationRequired
    public String toString(Number value) {
        // TODO H3.1
        return value == null ? "" : stringifier.apply(value);
    }

    @Override
    @StudentImplementationRequired
    public Number fromString(String value) {
        // TODO H3.1
        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.isEmpty()) {
            return null;
        }

        return value.equals("-") ? numericizer.apply("-1") : numericizer.apply(value);
    }
}
