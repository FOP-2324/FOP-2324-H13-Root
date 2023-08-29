package h13.ui.layout;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;

import java.util.NoSuchElementException;

public class ChooserView implements View {

    private final GridPane root;

    private final ObservableMap<String, CheckBox> options;

    private final int columnSize;
    private int nextRow = 0;
    private int nextColumn;

    public ChooserView(GridPane root, int columnSize) {
        this.root = root;
        this.options = FXCollections.observableHashMap();
        this.columnSize = columnSize;
        this.options.addListener(
            (MapChangeListener.Change<? extends String, ? extends CheckBox> change) -> {
                if (change.wasAdded()) {
                    var box = change.getValueAdded();
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

    public int columnSize() {
        return columnSize;
    }

    public boolean contains(String option) {
        return options.containsKey(option);
    }

    public boolean contains(CheckBox option) {
        return contains(option.getText());
    }

    public boolean add(CheckBox option) {
        if (contains(option)) {
            return false;
        }
        options.put(option.getText(), option);
        return true;
    }

    public boolean add(String option) {
        return add(new CheckBox(option));
    }

    public CheckBox get(String option) throws NoSuchElementException {
        if (!contains(option)) {
            throw new NoSuchElementException(option);
        }
        return options.get(option);
    }

    public ObservableMap<String, CheckBox> getOptions() {
        return options;
    }

}
