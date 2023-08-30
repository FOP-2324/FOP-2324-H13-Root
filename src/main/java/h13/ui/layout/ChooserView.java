package h13.ui.layout;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;

import java.util.NoSuchElementException;

/**
 * A view that allows the user to choose from a set of options. The options are uniquely identified by their name.
 */
public class ChooserView implements View {

    /**
     * The root pane of this view.
     */
    private final GridPane root;

    /**
     * The options that can be chosen from.
     */
    private final ObservableMap<String, CheckBox> options;

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
     * Creates a new chooser view with the given root pane and the given maximum number of columns in a row.
     *
     * @param root       the root pane
     * @param columnSize the maximum number of columns in a row
     */
    public ChooserView(GridPane root, int columnSize) {
        this.root = root;
        this.options = FXCollections.observableHashMap();
        this.columnSize = columnSize;
        this.options.addListener(
            (MapChangeListener.Change<? extends String, ? extends CheckBox> change) -> {
                if (change.wasAdded()) {
                    root.add(change.getValueAdded(), nextColumn++, nextRow);
                    if (nextColumn == columnSize) {
                        nextColumn = 0;
                        nextRow++;
                    }
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
     * Returns the maximum number of columns in a row.
     *
     * @return the maximum number of columns in a row
     */
    public int getColumnSize() {
        return columnSize;
    }

    /**
     * Returns {@code true} if this view contains the given option.
     *
     * @param option the option to check
     * @return {@code true} if this view contains the given option
     */
    public boolean contains(String option) {
        return options.containsKey(option);
    }

    /**
     * Returns {@code true} if this view contains the given option.
     *
     * @param option the option to check
     * @return {@code true} if this view contains the given option
     */
    public boolean contains(CheckBox option) {
        return contains(option.getText());
    }

    /**
     * Returns {@code true} if the view added the option and returns {@code false} if the view already contained the
     * option.
     *
     * @param option the option to add
     * @return {@code true} if the view added the option and returns {@code false} if the view already contained the
     * option
     */
    public boolean add(CheckBox option) {
        if (contains(option)) {
            return false;
        }
        options.put(option.getText(), option);
        return true;
    }

    /**
     * Returns {@code true} if the view added the option and returns {@code false} if the view already contained the
     * option.
     *
     * @param option the option to add
     * @return {@code true} if the view added the option and returns {@code false} if the view already contained the
     * option
     */
    public boolean add(String option) {
        return add(new CheckBox(option));
    }

    /**
     * Returns the option with the given name.
     *
     * @param option the name of the option
     * @return the option with the given name
     * @throws NoSuchElementException if the view does not contain the option
     */
    public CheckBox get(String option) throws NoSuchElementException {
        if (!contains(option)) {
            throw new NoSuchElementException(option);
        }
        return options.get(option);
    }

    /**
     * Returns the options that can be chosen from this view.
     *
     * @return the options that can be chosen from this view
     */
    public ObservableMap<String, CheckBox> getOptions() {
        return options;
    }

}
