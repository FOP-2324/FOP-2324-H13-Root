package h13.ui.layout;

import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * A view that allows the user to choose from a set of options. The options are uniquely identified by their name.
 */
public class ChooserView extends AbstractView<ChooserView, GridPane> implements View {

    /**
     * The options that can be chosen from.
     */
    private final ObservableMap<String, CheckBox> options = FXCollections.observableMap(new LinkedHashMap<>());

    /**
     * The maximum number of columns in a row.
     */
    private final int columnSize;

    /**
     * The next free row for a new option.
     */
    private int nextRow = 0;

    /**
     * The next free column for a new option.
     */
    private int nextColumn;

    /**
     * Creates a new chooser view the given maximum number of columns in a row.
     *
     * @param columnSize    the maximum number of columns in a row
     * @param configuration the configuration for this view
     */
    public ChooserView(int columnSize, ViewConfiguration<ChooserView> configuration) {
        this(new GridPane(), columnSize, configuration);
    }

    /**
     * Creates a new chooser view with the given root pane and the given maximum number of columns in a row.
     *
     * @param root          the root pane
     * @param columnSize    the maximum number of columns in a row
     * @param configuration the configuration for this view
     */
    public ChooserView(GridPane root, int columnSize, ViewConfiguration<ChooserView> configuration) {
        super(root, configuration);
        this.columnSize = columnSize;
        initialize();
        config(this);
    }

    @Override
    @StudentImplementationRequired
    public void initialize() {
        // TODO H4.1
        options.addListener(
            (MapChangeListener.Change<? extends String, ? extends CheckBox> change) -> {
                if (change.wasAdded()) {
                    root.add(change.getValueAdded(), nextColumn++, nextRow);
                    if (nextColumn == columnSize) {
                        nextColumn = 0;
                        nextRow++;
                    }
                } else if (change.wasRemoved()) {
                    nextColumn--;
                    if (nextColumn == -1) {
                        nextColumn = columnSize - 1;
                        nextRow--;
                    }
                    root.getChildren().remove(change.getValueRemoved());
                }
            }
        );
    }

    /**
     * Returns the maximum number of columns in a row.
     *
     * @return the maximum number of columns in a row
     */
    public int getColumnSize() {
        return columnSize;
    }

    /**
     * Adds the given option to this chooser view.
     *
     * @param text the name of the option
     */
    public void add(String text) {
        if (contains(text)) {
            throw new IllegalArgumentException("Checkbox Group: duplicate option added: " + text);
        }
        options.put(text, new CheckBox(text));
    }

    /**
     * Returns whether the given option is contained in this chooser view.
     *
     * @param text the name of the option
     * @return {@code true} if the given option is contained in this chooser view
     */
    public boolean contains(String text) {
        return options.containsKey(text);
    }

    /**
     * Removes the given option from this chooser view.
     *
     * @param text the name of the option
     * @return the removed option
     */
    public @Nullable CheckBox remove(String text) {
        return options.remove(text);
    }

    /**
     * Returns the option with the given name.
     *
     * @param text the name of the option
     * @return the option with the given name
     * @throws NoSuchElementException if the option is not contained in this chooser view
     */
    public CheckBox get(String text) {
        if (!contains(text)) {
            throw new NoSuchElementException(text);
        }
        return options.get(text);
    }

    /**
     * Returns the selected properties of the options.
     *
     * @return the selected properties of the options
     */
    public Map<String, BooleanProperty> selectedProperties() {
        return options.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().selectedProperty()));
    }

    /**
     * Returns the next column for a new option.
     *
     * @return the next column for a new option
     */
    public int getNextColumn() {
        return nextColumn;
    }

    /**
     * Returns the next row for a new option.
     *
     * @return the next row for a new option
     */
    public int getNextRow() {
        return nextRow;
    }

    /**
     * Returns the options of this chooser view.
     *
     * @return the options of this chooser view
     */
    public ObservableMap<String, CheckBox> getOptions() {
        return options;
    }

    /**
     * Adds the given listener to the options.
     *
     * @param listener the listener to add
     */
    public void addListener(MapChangeListener<? super String, ? super CheckBox> listener) {
        options.addListener(listener);
    }

    /**
     * Removes the given listener from the options.
     *
     * @param listener the listener to remove
     */
    public void removeListener(MapChangeListener<? super String, ? super CheckBox> listener) {
        options.addListener(listener);
    }
}
