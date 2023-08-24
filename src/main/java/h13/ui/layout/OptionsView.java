package h13.ui.layout;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * An options view that displays check boxes in a grid.
 *
 * @author Nhan Huynh
 */
public class OptionsView extends AbstractView<GridPane> implements View<GridPane> {

    /**
     * The maximum number of check boxes that can be displayed in a row.
     */
    private final int columnSize;

    /**
     * The list of check boxes that are displayed in the view.
     */
    private final ObservableList<CheckBox> boxes;

    /**
     * The next row to add a checkbox to.
     */
    private int nextRow = 0;

    /**
     * The next column to add a checkbox to.
     */
    private int nextColumn = 0;

    /**
     * Constructs an options view with the specified column size.
     *
     * @param columnSize the maximum number of check boxes that can be displayed in a row
     */
    public OptionsView(int columnSize) {
        this(columnSize, new CheckBox[0]);
    }

    /**
     * Constructs an options view with the specified column size and check boxes.
     *
     * @param columnSize the maximum number of check boxes that can be displayed in a row
     * @param boxes      the checkboxes to display in the view
     */
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

    /**
     * Constructs an options view with the specified column size and check boxes.
     *
     * @param columnSize the maximum number of check boxes that can be displayed in a row
     * @param boxes      the checkboxes to display in the view
     */
    public OptionsView(int columnSize, String... boxes) {
        this(columnSize, Arrays.stream(boxes).map(CheckBox::new).toArray(CheckBox[]::new));
    }

    /**
     * Adds the specified checkbox to the view.
     *
     * @param box the checkbox to add
     */
    public void add(CheckBox box) {
        getView().add(box, nextColumn++, nextRow);
        if (nextColumn == columnSize) {
            nextRow++;
        }
    }

    /**
     * Returns the list of check boxes that are displayed in the view.
     * @return the list of check boxes that are displayed in the view
     */
    public ObservableList<CheckBox> getBoxes() {
        return boxes;
    }

    /**
     * Returns the maximum number of check boxes that can be displayed in a row.
     * @return the maximum number of check boxes that can be displayed in a row
     */
    public int getColumnSize() {
        return columnSize;
    }

}
