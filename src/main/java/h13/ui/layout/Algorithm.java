package h13.ui.layout;

public enum Algorithm {
    SIMPLE,
    IMPROVED,
    FRACTAL;

    @Override
    public String toString() {
        String name = name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

}
