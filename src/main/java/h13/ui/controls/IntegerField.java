package h13.ui.controls;

import javafx.util.StringConverter;

import java.util.regex.Pattern;

public class IntegerField extends NumberField {

    public static final Pattern POSITIVE_ONLY = Pattern.compile("\\d*");

    public static final Pattern NEGATIVE_ONLY = Pattern.compile("-" + POSITIVE_ONLY.pattern());

    public static final Pattern ANY = Pattern.compile("-?" + POSITIVE_ONLY.pattern());


    protected IntegerField(Pattern pattern, StringConverter<Number> converter) {
        super(pattern, converter);
    }

    public IntegerField(Pattern pattern) {
        this(pattern, new NumberStringConverter(value -> Integer.toString(value.intValue()), Integer::parseInt));
    }

    public IntegerField() {
        this(ANY);
    }

}
