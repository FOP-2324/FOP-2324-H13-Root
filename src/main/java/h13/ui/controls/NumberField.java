package h13.ui.controls;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

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
     */
    //@StudentImplementationRequired
    public NumberField(TextFormatter<Number> formatter) {
        this.formatter = formatter;
        this.setTextFormatter(formatter);
        // TODO H3.2
        Bindings.bindBidirectional(textProperty(), this.value, getConverter());
    }

    /**
     * Creates a number field with the given pattern and converter.
     *
     * @param pattern the pattern that is used to validate the input
     */
    public NumberField(Pattern pattern) {
        this(new TextFormatter<>(change -> pattern.matcher(change.getControlNewText()).matches() ? change : null));
    }

    /**
     * Returns the converter used to convert the input to a number.
     *
     * @return the converter used to convert the input to a number
     */
    public abstract StringConverter<Number> getConverter();

    /**
     * Returns the formatter used to validate the input.
     *
     * @return the formatter used to validate the input
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
     * Returns the value of the field.
     *
     * @return the value of the field
     */
    public Number getValue() {
        return value.getValue();
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
