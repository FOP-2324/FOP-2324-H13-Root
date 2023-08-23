package h13.ui.controls;


import javafx.beans.property.Property;
import javafx.scene.control.Control;
import javafx.scene.control.Label;

/**
 * A parameter text field that consists of a label and a numeric text field.
 *
 * @author Nhan Huynh
 */
public class ParameterTextField {

    /**
     * The label of the text field that describes the parameter.
     */
    private final Label label;

    /**
     * The numeric text field of the parameter.
     */
    private final NumberTextField textField;

    /**
     * Creates a new ParameterTextField with the given label and text field.
     *
     * @param label     the label of the text field that describes the parameter
     * @param textField the numeric text field of the parameter
     */
    public ParameterTextField(Label label, NumberTextField textField) {
        this.label = label;
        this.textField = textField;
    }

    /**
     * Creates a new ParameterTextField with the given label and text field.
     *
     * @param label     the label of the text field that describes the parameter
     * @param textField the numeric text field of the parameter
     */
    public ParameterTextField(String label, NumberTextField textField) {
        this(new Label(label), textField);
    }

    /**
     * Returns the label of the text field that describes the parameter.
     *
     * @return the label of the text field that describes the parameter
     */
    public Label getLabel() {
        return label;
    }

    /**
     * Returns the numeric text field of the parameter.
     *
     * @return the numeric text field of the parameter
     */
    public NumberTextField getTextField() {
        return textField;
    }

    /**
     * Returns all controls of the parameter text field.
     *
     * @return all controls of the parameter text field
     */
    public Control[] getControls() {
        return new Control[]{label, textField};
    }

    /**
     * Returns the numeric value of the text field property of the parameter.
     *
     * @return the numeric value of the text field property of the parameter
     */
    public Property<Number> valueProperty() {
        return textField.valueProperty();
    }

    /**
     * Set the disability of the parameter text field. If the parameter text field is disabled, the user cannot change
     * the value of the text field.
     *
     * @param value the disability of the parameter text field
     */
    public void setDisable(boolean value) {
        label.setDisable(value);
        textField.setDisable(value);
    }

}
