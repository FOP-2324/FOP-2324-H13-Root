package h13.ui.layout;

import h13.ui.controls.ParameterTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.layout.GridPane;

import java.util.LinkedHashMap;

/**
 * A view that lists parameters in a table like layout.
 *
 * @author Nhan Huynh
 */
public class ParametersView extends AbstractView<GridPane> implements View<GridPane> {

    /**
     * The parameters of this view.
     */
    private final ObservableMap<String, ParameterTextField> parameters;

    /**
     * The next row to add a parameter to.
     */
    private int nextRow = 0;

    /**
     * Creates a new parameters view with the given parameters.
     *
     * @param parameters The parameters to add to this view.
     */
    public ParametersView(ParameterTextField... parameters) {
        super(new GridPane());
        this.parameters = FXCollections.observableMap(new LinkedHashMap<>(parameters.length));
        for (ParameterTextField parameter : parameters) {
            addParameter(parameter);
        }
    }

    /**
     * Creates a new parameters view with no parameters.
     */
    public ParametersView() {
        this(new ParameterTextField[0]);
    }

    /**
     * Returns the parameters of this view.
     *
     * @return the parameters of this view.
     */
    public ObservableMap<String, ParameterTextField> getParameters() {
        return parameters;
    }

    /**
     * Returns the parameter with the given label.
     *
     * @param label The label of the parameter to return.
     * @return the parameter with the given label.
     */
    public ParameterTextField getParameter(String label) {
        return parameters.get(label);
    }

    /**
     * Adds a parameter to this view.
     *
     * @param parameter the parameter to add
     */
    public void addParameter(ParameterTextField parameter) {
        parameters.put(parameter.getLabel().getText(), parameter);
        getView().addRow(nextRow++, parameter.getControls());
    }

    /**
     * Adds multiple parameters to this view.
     *
     * @param parameters the parameters to add
     */
    public void addParameters(ParameterTextField... parameters) {
        for (ParameterTextField parameter : parameters) {
            addParameter(parameter);
        }
    }

}
