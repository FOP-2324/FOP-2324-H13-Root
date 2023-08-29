package h13.ui.controls;

import javafx.util.StringConverter;

import java.util.function.Function;

public class NumberStringConverter extends StringConverter<Number> {

    private final Function<Number, String> toString;

    private final Function<String, Number> toNumber;

    public NumberStringConverter(Function<Number, String> toString, Function<String, Number> toNumber) {
        this.toString = toString;
        this.toNumber = toNumber;
    }

    @Override
    public String toString(Number value) {
        return value == null ? "" : toString.apply(value);
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

        return value.equals("-") ? toNumber.apply("-1") : toNumber.apply(value);
    }

}
