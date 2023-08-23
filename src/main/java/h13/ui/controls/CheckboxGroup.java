package h13.ui.controls;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.Arrays;


/**
 * A group of checkboxes with a label.
 */
public class CheckboxGroup {

    private final Label label;

    private final HBox boxGroup = new HBox();
    private final CheckBox[] boxes;

    public CheckboxGroup(Label label, CheckBox... boxes) {
        this.label = label;
        this.boxes = boxes;
        boxGroup.getChildren().addAll(boxes);
    }

    public CheckboxGroup(String label, String... boxes) {
        this(new Label(label), Arrays.stream(boxes).map(CheckBox::new).toArray(CheckBox[]::new));
    }


    public Label getLabel() {
        return label;
    }

    public HBox getBoxGroup() {
        return boxGroup;
    }

    public CheckBox[] getBoxes() {
        return boxes;
    }

    public Node[] getControls() {
        return new Node[]{label, boxGroup};
    }

}
