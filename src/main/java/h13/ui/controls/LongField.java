package h13.ui.controls;

import javafx.util.StringConverter;

import java.util.regex.Pattern;

public class LongField extends IntegerField {

    protected LongField(Pattern pattern, StringConverter<Number> converter) {
        super(pattern, converter);
    }

    public LongField(Pattern pattern) {
        this(pattern, new NumberStringConverter(value -> Long.toString(value.longValue()), Long::parseLong));
    }

    public LongField() {
        this(ANY);
    }

}
