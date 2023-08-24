package h13.ui.layout;

public abstract class AbstractView<V> {

    private final V view;

    public AbstractView(V view) {
        this.view = view;
    }

    public V getView() {
        return view;
    }
}
