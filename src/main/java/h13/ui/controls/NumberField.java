package h13.ui.controls;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.util.regex.Pattern;

/**
 * A text field that only accepts numbers.
 *
 * @author Nhan Huynh
 */
public abstract class NumberField extends TextField {

    /**
     * The formatter that is used to validate the input.
     */
    private final TextFormatter<Number> formatter;

    /**
     * The numeric value of the field.
     */
    private final Property<Number> value = new SimpleObjectProperty<>(this, "NumericValue");

    /**
     * Creates a number field with the given formatter and converter.
     *
     * @param formatter the formatter that is used to validate the input
     * @param converter the converter that is used to convert the input to a number
     */
    public NumberField(TextFormatter<Number> formatter, StringConverter<Number> converter) {
        this.formatter = formatter;
        this.setTextFormatter(formatter);
        Bindings.bindBidirectional(textProperty(), this.value, converter);
    }

    /**
     * Creates a number field with the given pattern and converter.
     *
     * @param pattern   the pattern that is used to validate the input
     * @param converter the converter that is used to convert the input to a number
     */
    public NumberField(Pattern pattern, StringConverter<Number> converter) {
        this(
            new TextFormatter<>(change -> pattern.matcher(change.getControlNewText()).matches() ? change : null),
            converter
        );
    }

    /**
     * Returns the formatter that is used to validate the input.
     *
     * @return the formatter that is used to validate the input
     */
    public TextFormatter<Number> getFormatter() {
        return formatter;
    }

    /**
     * Returns the numeric value property of the field.
     *
     * @return the numeric value property of the field
     */
    public Property<Number> valueProperty() {
        return value;
    }

    /**
     * Sets the numeric value of the field.
     *
     * @param value the numeric value of the field
     */
    public void setValue(Number value) {
        this.value.setValue(value);
    }

    /**
     * Sets the prompt text of the field.
     *
     * @param value the prompt text of the field
     */
    public void setPromptValue(Number value) {
        this.setPromptText(value.toString());
    }

}
