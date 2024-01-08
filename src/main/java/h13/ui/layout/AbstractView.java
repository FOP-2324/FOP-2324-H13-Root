package h13.ui.layout;

import javafx.scene.Parent;

/**
 * An abstract view that implements the {@link View} interface and provides a base implementation.
 *
 * @param <V> the type of the root node
 */
public abstract class AbstractView<V extends View, P extends Parent> implements View, Initializable,
    ViewConfiguration<V> {

    /**
     * The root node of this view.
     */
    protected final P root;

    /**
     * The configuration of this view.
     */
    protected final ViewConfiguration<V> configuration;

    public AbstractView(P root, ViewConfiguration<V> configuration) {
        this.root = root;
        this.configuration = configuration;
    }

    @Override
    public P getView() {
        return root;
    }

    @Override
    public void config(V view) {
        configuration.config(view);
    }
}
