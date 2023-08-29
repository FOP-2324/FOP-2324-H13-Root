package h13.ui.controls;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.util.regex.Pattern;

public abstract class NumberField extends TextField {

    private final TextFormatter<Number> formatter;

    private final Property<Number> value = new SimpleObjectProperty<>(this, "NumericValue");

    public NumberField(TextFormatter<Number> formatter, StringConverter<Number> converter) {
        this.formatter = formatter;
        this.setTextFormatter(formatter);
        Bindings.bindBidirectional(textProperty(), this.value, converter);
    }

    public NumberField(Pattern pattern, StringConverter<Number> converter) {
        this(
            new TextFormatter<>(change -> pattern.matcher(change.getControlNewText()).matches() ? change : null),
            converter
        );
    }

    public TextFormatter<Number> getFormatter() {
        return formatter;
    }

    public Property<Number> valueProperty() {
        return value;
    }

    public void setValue(Number value) {
        this.value.setValue(value);
    }

    public void setPromptValue(Number value) {
        this.setPromptText(value.toString());
    }

}
