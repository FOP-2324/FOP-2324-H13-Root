package h13.ui.controls;

import java.util.regex.Pattern;

public class DoubleField extends NumberField {

    public static final Pattern POSITIVE_ONLY = Pattern.compile("\\d*(\\.\\d*)?");

    public static final Pattern NEGATIVE_ONLY = Pattern.compile("-" + POSITIVE_ONLY.pattern());

    public static final Pattern ANY = Pattern.compile("-?" + POSITIVE_ONLY.pattern());


    public DoubleField(Pattern pattern) {
        super(pattern, new NumberStringConverter(value -> Double.toString(value.doubleValue()), Double::parseDouble));
    }

    public DoubleField() {
        this(ANY);
    }

}
