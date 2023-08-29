package h13.ui.app;

import h13.utils.Strings;

 enum Algorithm {

    SIMPLE,
    IMPROVED,
    FRACTAL,
    ;

    @Override
    public String toString() {
        return Strings.capitalize(name());
    }
}
