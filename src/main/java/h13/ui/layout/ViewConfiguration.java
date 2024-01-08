package h13.ui.layout;

/**
 * A functional interface for configuring a view.
 *
 * @param <V> the type of the view
 * @author Nhan Huynh
 */
@FunctionalInterface
public interface ViewConfiguration<V extends View> {

    /**
     * Configures the given view. This must be called once after the initialization of the view.
     *
     * @param view the view to configure
     */
    void config(V view);
}
