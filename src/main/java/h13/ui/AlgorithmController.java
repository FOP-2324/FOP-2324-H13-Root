package h13.ui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class AlgorithmController {

    private final IntegerProperty seed = new SimpleIntegerProperty(this, "seed");

    private final DoubleProperty frequency = new SimpleDoubleProperty(this, "frequency");

    private final DoubleProperty amplitude = new SimpleDoubleProperty(this, "amplitude");

    private final IntegerProperty octaves = new SimpleIntegerProperty(this, "octaves");
    private final DoubleProperty persistence = new SimpleDoubleProperty(this, "persistence");

    private final DoubleProperty lacunarity = new SimpleDoubleProperty(this, "lacunarity");

    public IntegerProperty seedProperty() {
        return seed;
    }

    public DoubleProperty frequencyProperty() {
        return frequency;
    }

    public DoubleProperty amplitudeProperty() {
        return amplitude;
    }

    public IntegerProperty octavesProperty() {
        return octaves;
    }

    public DoubleProperty persistenceProperty() {
        return persistence;
    }

    public DoubleProperty lacunarityProperty() {
        return lacunarity;
    }
}
