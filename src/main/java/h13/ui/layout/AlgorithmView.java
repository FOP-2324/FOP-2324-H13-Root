package h13.ui.layout;

import h13.noise.PerlinNoise;
import h13.ui.controls.NumberField;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * A {@link PerlinNoise} algorithm visualization view.
 *
 * @author Nhan Huynh
 */
public class AlgorithmView implements View {

    /**
     * The root layout.
     */
    private final BorderPane root;

    /**
     * The view model of the view for handling the logic.
     */
    private final AlgorithmViewModel viewModel;

    /**
     * The canvas for visualizing the algorithm.
     */
    private final Canvas visualization;

    /**
     * The view for settings that are related to the algorithm configurations.
     */
    private final SettingsView settings;

    /**
     * Creates a new algorithm view with the given root, settings and factory for creating the view model.
     *
     * @param root     the root layout
     * @param settings the settings view
     * @param factory  the factory for creating the view model
     */
    public AlgorithmView(
        BorderPane root,
        SettingsView settings,
        BiFunction<Map<String, BooleanProperty>, Map<String, NumberField>, AlgorithmViewModel> factory) {
        this.root = root;
        this.visualization = new Canvas();
        this.settings = settings;

        // Layout
        root.setCenter(visualization);
        root.setRight(settings.view());

        // View model
        var options = settings.getOptions().getOptions().entrySet()
            .stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().selectedProperty()));
        var parameters = settings.getParameters().getParameters().entrySet()
            .stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getValue()));
        this.viewModel = factory.apply(options, parameters);

        // Bindings
        this.viewModel.addDrawListener(settings.getSubmitButton().pressedProperty(), visualization);
        visualization.widthProperty().bind(
            root.widthProperty()
                .subtract(settings.view().widthProperty())
        );

        // Bindings for padding since they can be changed dynamically. Therefore, we need to listen to changes in
        // order th adjust the height of the canvas accordingly.
        DoubleProperty paddingTop = new SimpleDoubleProperty();
        DoubleProperty paddingBottom = new SimpleDoubleProperty();

        root.paddingProperty().addListener((observable, oldValue, newValue) -> {
            paddingTop.set(newValue.getTop());
            paddingBottom.set(newValue.getBottom());
        });

        visualization.heightProperty().bind(
            root.heightProperty().subtract(paddingTop).subtract(paddingBottom)
        );
    }

    @Override
    public BorderPane view() {
        return root;
    }

    /**
     * Returns the canvas for visualizing the algorithm.
     *
     * @return the canvas for visualizing the algorithm
     */
    public Canvas getVisualization() {
        return visualization;
    }

    /**
     * Returns the settings view.
     *
     * @return the settings view
     */
    public SettingsView getSettings() {
        return settings;
    }

}
