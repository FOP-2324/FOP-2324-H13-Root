package h13.ui.layout;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Arrays;

public class OptionsView extends AbstractView<GridPane> implements View<GridPane> {

    private final int columnSize;
    private final ObservableList<CheckBox> boxes;
    private int nextRow = 0;
    private int nextColumn = 0;

    public OptionsView(int columnSize) {
        this(columnSize, new CheckBox[0]);
    }

    public OptionsView(int columnSize, CheckBox... boxes) {
        super(new GridPane());
        this.columnSize = columnSize;
        this.boxes = FXCollections.observableList(new ArrayList<>(boxes.length));
        int column = 0;
        for (CheckBox box : boxes) {
            add(box);
            this.boxes.add(box);
        }
    }

    public OptionsView(int columnSize, String... boxes) {
        this(columnSize, Arrays.stream(boxes).map(CheckBox::new).toArray(CheckBox[]::new));
    }

    public void add(CheckBox box) {
        getView().add(box, nextColumn++, nextRow);
        if (nextColumn == columnSize) {
            nextRow++;
        }
    }

    public ObservableList<CheckBox> getBoxes() {
        return boxes;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public int getNextColumn() {
        return nextColumn;
    }

    public int getNextRow() {
        return nextRow;
    }

}
