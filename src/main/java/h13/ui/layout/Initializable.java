package h13.ui.layout;

/**
 * An interface that allows an object to be initialized.
 */
@FunctionalInterface
public interface Initializable {

    /**
     * Initializes this object. This must be called only once.
     */
    void initialize();
}
