package h13.ui.layout;

import javafx.scene.layout.Pane;

/**
 * An abstract base class that provides a common implementation for views.
 *
 * @param <V> the type of the view
 * @author Nhan Huynh
 */
public abstract class AbstractView<V extends Pane> implements View<V> {

    /**
     * The view that can be displayed on a scene.
     */
    private final V view;

    /**
     * Constructs an abstract view with the specified view.
     *
     * @param view the view that can be displayed on a scene
     */
    public AbstractView(V view) {
        this.view = view;
    }

    @Override
    public V getView() {
        return view;
    }

}
