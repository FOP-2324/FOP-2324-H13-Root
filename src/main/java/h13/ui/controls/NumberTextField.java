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
public abstract class NumberTextField extends TextField {

    /**
     * The formatter that is used to validate the input.
     */
    private final TextFormatter<Number> formatter;

    /**
     * The numeric value of the TextField.
     */
    private final Property<Number> value = new SimpleObjectProperty<>(this, "NumericValue");

    /**
     * Creates a new number valued text field with the given formatter and converter which accepts any number that
     * matches the given pattern and converts the input to a numeric value from String and vice versa.
     *
     * @param formatter the formatter that is used to validate the input
     * @param converter the converter that is used to convert the input (string) to a numeric value
     */
    public NumberTextField(TextFormatter<Number> formatter, StringConverter<Number> converter) {
        this.formatter = formatter;
        this.setTextFormatter(formatter);
        Bindings.bindBidirectional(textProperty(), this.value, converter);
    }

    /**
     * Creates a new number valued text field with the given pattern and converter which accepts any number that
     * matches the given pattern and converts the input to a numeric value from String and vice versa.
     *
     * @param pattern   the pattern that is used to validate the input
     * @param converter the converter that is used to convert the input (string) to a numeric value
     */
    public NumberTextField(Pattern pattern, StringConverter<Number> converter) {
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
     * Returns the numeric value of the text field property.
     *
     * @return the numeric value of the text field  property
     */
    public Property<Number> valueProperty() {
        return value;
    }

    /**
     * Sets the numeric value of the text field.
     *
     * @param value the numeric value of the text field
     */
    public void setValue(Number value) {
        this.value.setValue(value);
    }

    /**
     * Sets the prompt text of the text field to the given value.
     *
     * @param value the value to set the prompt text to
     */
    public void setPromptValue(Number value) {
        this.setPromptText(value.toString());
    }
}
