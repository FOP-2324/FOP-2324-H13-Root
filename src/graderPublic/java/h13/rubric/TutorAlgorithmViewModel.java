package h13.rubric;

import h13.noise.PerlinNoise;
import h13.ui.layout.AlgorithmViewModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.DoubleFunction;

public abstract class TutorAlgorithmViewModel extends AlgorithmViewModel {

    public TutorAlgorithmViewModel(
        Map<String, BooleanProperty> options,
        Map<String, Property<Number>> parameters,
        DoubleFunction<Color> colorMapper) {
        super(options, parameters, colorMapper);
    }

    public TutorAlgorithmViewModel(DoubleFunction<Color> colorMapper) {
        super(Map.of(), Map.of(), colorMapper);
    }

    @Override
    public Image createImage(PerlinNoise algorithm, int x, int y, int w, int h) {
        return super.createImage(algorithm, x, y, w, h);
    }

    @Override
    public @Nullable PerlinNoise getLastAlgorithm() {
        return super.getLastAlgorithm();
    }
}
