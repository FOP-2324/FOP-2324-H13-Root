package h13.ui.layout;

import h13.ui.controls.NumberField;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.NoSuchElementException;

public class ParameterView implements View {

    private final GridPane root;

    private final ObservableMap<String, Pair<Label, NumberField>> parameters;

    private int nextRow = 0;

    public ParameterView(GridPane root) {
        this.root = root;
        this.parameters = FXCollections.observableHashMap();
        this.parameters.addListener(
            (MapChangeListener.Change<? extends String, ? extends Pair<Label, NumberField>> change) -> {
                if (change.wasAdded()) {
                    var fields = change.getValueAdded();
                    root.addRow(nextRow++, fields.getKey(), fields.getValue());
                    return;
                }
                throw new UnsupportedOperationException("Not supported yet.");
            }
        );
    }

    @Override
    public GridPane view() {
        return root;
    }

    public boolean contains(String label) {
        return parameters.containsKey(label);
    }

    public boolean contains(Label label) {
        return contains(label.getText());
    }

    public boolean add(Label label, NumberField field) {
        if (contains(label)) {
            return false;
        }
        parameters.put(label.getText(), new Pair<>(label, field));
        return true;
    }

    public boolean add(String label, NumberField field) {
        return add(new Label(label), field);
    }

    public Pair<Label, NumberField> get(String label) throws NoSuchElementException {
        if (!contains(label)) {
            throw new NoSuchElementException(label);
        }
        return parameters.get(label);
    }

    public ObservableMap<String, Pair<Label, NumberField>> getParameters() {
        return parameters;
    }

}
