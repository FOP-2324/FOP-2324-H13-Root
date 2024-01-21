package h13.ui.layout;

import h13.ui.controls.NumberField;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * A view that contains a list of parameters. The parameters are uniquely identified by their label.
 *
 * @author Nhan Huynh
 */
public class ParameterView extends AbstractView<ParameterView, GridPane> implements View {

    /**
     * The list of parameters.
     */
    private final ObservableMap<String, Pair<Label, NumberField>> parameters = FXCollections.observableHashMap();

    /**
     * The next row to add a parameter to.
     */
    private int nextRow = 0;

    /**
     * Creates a new parameter view.
     *
     * @param configuration the configuration of this view
     */
    public ParameterView(ViewConfiguration<ParameterView> configuration) {
        this(new GridPane(), configuration);
    }

    /**
     * Creates a new parameter view.
     *
     * @param root          the root pane of this view
     * @param configuration the configuration of this view
     */
    public ParameterView(GridPane root, ViewConfiguration<ParameterView> configuration) {
        super(root, configuration);
        initialize();
        config(this);
    }

    @Override
    @StudentImplementationRequired
    public void initialize() {
        // TODO H4.2
        parameters.addListener(
            (MapChangeListener.Change<? extends String, ? extends Pair<Label, NumberField>> change) -> {
                if (change.wasAdded()) {
                    var fields = change.getValueAdded();
                    root.addRow(nextRow++, fields.getKey(), fields.getValue());
                } else if (change.wasRemoved()) {
                    var fields = change.getValueRemoved();
                    root.getChildren().removeAll(fields.getKey(), fields.getValue());
                    nextRow--;
                }
            }
        );
    }

    /**
     * Adds a new parameter with the given label and field.
     *
     * @param text  the label of the parameter
     * @param field the numeric field of the parameter
     */
    public void add(String text, NumberField field) {
        if (contains(text)) {
            throw new IllegalArgumentException("Parameter: duplicate parameter added: " + text);
        }
        Label label = new Label(text);
        parameters.put(text, new Pair<>(label, field));
    }

    /**
     * Returns {@code true} if this view contains a parameter with the given label.
     *
     * @param text the label of the parameter
     * @return {@code true} if this view contains a parameter with the given label
     */
    public boolean contains(String text) {
        return parameters.containsKey(text);
    }

    /**
     * Removes the parameter with the given label.
     *
     * @param text the label of the parameter
     * @return the removed parameter
     */
    public @Nullable Pair<Label, NumberField> remove(String text) {
        return parameters.remove(text);
    }

    /**
     * Returns the parameter with the given label.
     *
     * @param text the label of the parameter
     * @return the parameter with the given label
     * @throws NoSuchElementException if this view does not contain a parameter with the given label
     */
    public Pair<Label, NumberField> get(String text) throws NoSuchElementException {
        if (!contains(text)) {
            throw new NoSuchElementException(text);
        }
        return parameters.get(text);
    }

    /**
     * Returns the value properties of the parameters.
     *
     * @return the value properties of the parameters
     */
    public Map<String, Property<Number>> valueProperties() {
        return parameters.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getValue().valueProperty()));
    }

    /**
     * Returns the 'disable' properties of the parameters.
     *
     * @return the 'disable' properties of the parameters
     */
    public Map<String, BooleanProperty> disableProperties() {
        return parameters.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                Pair<Label, NumberField> e = entry.getValue();
                BooleanProperty label = e.getKey().disableProperty();
                BooleanProperty field = e.getValue().disableProperty();
                label.bindBidirectional(field);
                return label;
            }));
    }

    /**
     * Returns the next row to add a parameter to.
     *
     * @return the next row to add a parameter to
     */
    public int getNextRow() {
        return nextRow;
    }

    /**
     * Returns the parameters.
     *
     * @return the parameters
     */
    public ObservableMap<String, Pair<Label, NumberField>> getParameters() {
        return parameters;
    }

    /**
     * Adds a listener to the parameters.
     *
     * @param listener the listener to add
     */
    public void addListener(
        MapChangeListener<? super String, ? super Pair<? extends Label, ? extends NumberField>> listener
    ) {
        parameters.addListener(listener);
    }

    /**
     * Removes a listener from the parameters.
     *
     * @param listener the listener to remove
     */
    public void removeListener(
        MapChangeListener<? super String, ? super Pair<? extends Label, ? extends NumberField>> listener
    ) {
        parameters.addListener(listener);
    }
}
