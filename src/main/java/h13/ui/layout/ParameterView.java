package h13.ui.layout;

import h13.ui.controls.NumberField;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.NoSuchElementException;

/**
 * A view that contains a list of parameters. The parameters are uniquely identified by their label.
 *
 * @author Nhan Huynh
 */
public class ParameterView implements View {

    /**
     * The root pane of this view.
     */
    private final GridPane root;

    /**
     * The list of parameters.
     */
    private final ObservableMap<String, Pair<Label, NumberField>> parameters;

    /**
     * The next row to add a parameter to.
     */
    private int nextRow = 0;

    /**
     * Creates a new parameter view.
     *
     * @param root the root pane of this view
     */
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

    /**
     * Returns {@code true} if this view contains a parameter with the given label.
     *
     * @param label the label of the parameter
     * @return {@code true} if this view contains a parameter with the given label
     */
    public boolean contains(String label) {
        return parameters.containsKey(label);
    }

    /**
     * Returns {@code true} if this view contains a parameter with the given label.
     *
     * @param label the label of the parameter
     * @return {@code true} if this view contains a parameter with the given label
     */
    public boolean contains(Label label) {
        return contains(label.getText());
    }

    /**
     * Returns {@code true} if the given parameter is added to this view and {@code false} if this view already
     * contains a parameter with the same label.
     *
     * @param label the label of the parameter
     * @param field the numeric field of the parameter
     * @return {@code true} if the given parameter is added to this view and {@code false} if this view already
     */
    public boolean add(Label label, NumberField field) {
        if (contains(label)) {
            return false;
        }
        parameters.put(label.getText(), new Pair<>(label, field));
        return true;
    }

    /**
     * Returns {@code true} if the given parameter is added to this view and {@code false} if this view already
     * contains a parameter with the same label.
     *
     * @param label the label of the parameter
     * @param field the numeric field of the parameter
     * @return {@code true} if the given parameter is added to this view and {@code false} if this view already
     */
    public boolean add(String label, NumberField field) {
        return add(new Label(label), field);
    }

    /**
     * Returns the parameter with the given label.
     *
     * @param label the label of the parameter
     * @return the parameter with the given label
     * @throws NoSuchElementException if this view does not contain a parameter with the given label
     */
    public Pair<Label, NumberField> get(String label) throws NoSuchElementException {
        if (!contains(label)) {
            throw new NoSuchElementException(label);
        }
        return parameters.get(label);
    }

    /**
     * Returns the parameters of this view.
     *
     * @return the parameters of this view
     */
    public ObservableMap<String, Pair<Label, NumberField>> getParameters() {
        return parameters;
    }

}
