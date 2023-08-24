package h13.ui.layout;

import h13.ui.controls.ParameterTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.layout.GridPane;

import java.util.LinkedHashMap;

public class ParametersView extends AbstractView<GridPane> implements View<GridPane> {

    private final ObservableMap<String, ParameterTextField> parameters;

    private int nextRow = 0;

    public ParametersView(ParameterTextField... parameters) {
        super(new GridPane());
        this.parameters = FXCollections.observableMap(new LinkedHashMap<>(parameters.length));
        for (ParameterTextField parameter : parameters) {
            addParameter(parameter);
        }
    }

    public ParametersView() {
        this(new ParameterTextField[0]);
    }

    public ObservableMap<String, ParameterTextField> getParameters() {
        return parameters;
    }

    public ParameterTextField getParameter(String label) {
        return parameters.get(label);
    }

    public void addParameter(ParameterTextField parameter) {
        parameters.put(parameter.getLabel().getText(), parameter);
        getView().addRow(nextRow++, parameter.getControls());
    }

    public void addParameters(ParameterTextField... parameters) {
        for (ParameterTextField parameter : parameters) {
            addParameter(parameter);
        }
    }

}
