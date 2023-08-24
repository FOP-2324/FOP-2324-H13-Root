package h13.ui.layout;

import javafx.scene.layout.Pane;

/**
 * Represents on object that is viewable on a scene.
 *
 * @param <V> the type of the view
 * @author Nhan Huynh
 */
public interface View<V extends Pane> {

    /**
     * Returns the view that can be displayed on a scene.
     *
     * @return the view that can be displayed on a scene
     */
    V getView();

}
