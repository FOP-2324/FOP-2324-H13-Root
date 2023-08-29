package h13.ui.layout;

import h13.ui.controls.NumberField;
import javafx.beans.property.BooleanProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class AlgorithmView implements View {

    private final BorderPane root;
    private final AlgorithmViewModel viewModel;

    private final Canvas visualization;

    private final SettingsView settings;

    public AlgorithmView(
        BorderPane root,
        SettingsView settings,
        BiFunction<Map<String, BooleanProperty>, Map<String, NumberField>, AlgorithmViewModel> factory) {
        this.root = root;
        this.visualization = new Canvas();
        this.settings = settings;

        root.setCenter(visualization);
        root.setRight(settings.view());

        var options = settings.getOptions().getOptions().entrySet()
            .stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().selectedProperty()));
        var parameters = settings.getParameters().getParameters().entrySet()
            .stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getValue()));
        this.viewModel = factory.apply(options, parameters);

        this.viewModel.addDrawListener(settings.getSubmitButton().pressedProperty(), visualization);

        visualization.widthProperty().bind(
            root.widthProperty()
                .subtract(settings.view().widthProperty())
                .subtract(root.getPadding().getLeft())
                .subtract(root.getPadding().getRight())
        );
        visualization.heightProperty().bind(
            root.heightProperty()
                .subtract(root.getPadding().getTop())
                .subtract(root.getPadding().getBottom())
        );
    }

    @Override
    public BorderPane view() {
        return root;
    }

    public Canvas getVisualization() {
        return visualization;
    }

    public SettingsView getSettings() {
        return settings;
    }

}
