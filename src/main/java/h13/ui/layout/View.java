package h13.ui.layout;

import javafx.scene.layout.Pane;

public interface View<V extends Pane> {

    V getView();

}
