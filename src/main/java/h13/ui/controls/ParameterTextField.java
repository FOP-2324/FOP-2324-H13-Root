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
    private final NumberTextField numberTextField;

    /**
     * Creates a new parameter text field with the given label and text field.
     *
     * @param label           the label of the text field that describes the parameter
     * @param numberTextField the numeric text field of the parameter
     */
    public ParameterTextField(Label label, NumberTextField numberTextField) {
        this.label = label;
        this.numberTextField = numberTextField;
    }

    /**
     * Creates a new parameter text field with the given label and text field.
     *
     * @param label           the label of the text field that describes the parameter
     * @param numberTextField the numeric text field of the parameter
     */
    public ParameterTextField(String label, NumberTextField numberTextField) {
        this(new Label(label), numberTextField);
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
    public NumberTextField getNumberTextField() {
        return numberTextField;
    }

    /**
     * Returns all controls of the parameter text field.
     *
     * @return all controls of the parameter text field
     */
    public Control[] getControls() {
        return new Control[]{label, numberTextField,};
    }

    /**
     * Returns the numeric value of the text field property of the parameter.
     *
     * @return the numeric value of the text field property of the parameter
     */
    public Property<Number> valueProperty() {
        return numberTextField.valueProperty();
    }

    /**
     * Set the disability of the parameter text field. If the parameter text field is disabled, the user cannot change
     * the value of the text field.
     *
     * @param value the disability of the parameter text field
     */
    public void setDisable(boolean value) {
        label.setDisable(value);
        numberTextField.setDisable(value);
    }

}
