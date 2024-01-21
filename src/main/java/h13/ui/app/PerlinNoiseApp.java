package h13.ui.app;

import h13.noise.PerlinNoise;
import h13.ui.controls.DoubleField;
import h13.ui.controls.IntegerField;
import h13.ui.controls.LongField;
import h13.ui.layout.AlgorithmView;
import h13.ui.layout.ChooserView;
import h13.ui.layout.ParameterView;
import h13.ui.layout.SettingsView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The {@link PerlinNoise} visualization application.
 *
 * @author Nhan Huynh
 */
public class PerlinNoiseApp extends Application {

    /**
     * The spacing between nodes.
     */
    private static final int SPACING = 10;

    /**
     * The padding of the nodes.
     */
    private static final Insets PADDING = new Insets(10);

    /**
     * The vertical gap between nodes.
     */
    private static final int VGAP = 5;

    /**
     * The horizontal gap between nodes.
     */
    private static final int HGAP = 10;

    /**
     * The header font.
     */
    private static final Font HEADER;
    /**
     * The coloring of the noise.
     */
    private static final Coloring COLORING = Coloring.MOUNTAIN;

    static {
        Font font = Font.getDefault();
        HEADER = Font.font(font.getName(), FontWeight.BOLD, font.getSize() * 1.25);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Perlin Noise");

        // Algorithms - Specify the available algorithms
        ChooserView options = new ChooserView(new GridPane(), 3, view -> {
            var underlying = view.getView();
            underlying.setPadding(PADDING);
            underlying.setHgap(HGAP);
            underlying.setVgap(VGAP);
        });

        for (var algorithm : Algorithm.values()) {
            options.add(algorithm.toString());
        }

        CheckBox defaultOption = options.get(Algorithm.SIMPLE.toString());
        defaultOption.setSelected(true);
        defaultOption.setDisable(true);

        // Parameters - Specify the available parameters
        ParameterView parameters = new ParameterView(new GridPane(), view -> {
            var underlying = view.getView();
            underlying.setPadding(PADDING);
            underlying.setHgap(HGAP);
            underlying.setVgap(VGAP);
        });
        for (Parameter parameter : Parameter.values()) {
            var field = switch (parameter) {
                case SEED -> new LongField(LongField.ANY);
                case OCTAVES -> new IntegerField(IntegerField.POSITIVE_ONLY);
                default -> new DoubleField(DoubleField.POSITIVE_ONLY);
            };
            field.setValue(parameter.defaultValue());
            field.setPromptValue(parameter.defaultValue());
            parameters.add(parameter.toString(), field);
        }

        // Settings - Specify, when parameters are enabled
        SettingsView settings = new SettingsView(
            new Pair<>("Algorithms", options),
            new Pair<>("Parameters", parameters),
            Map.of(
                    Algorithm.SIMPLE, Set.of(Parameter.SEED, Parameter.FREQUENCY),
                    Algorithm.IMPROVED, Set.of(Parameter.SEED, Parameter.FREQUENCY),
                    Algorithm.FRACTAL, Set.of(
                        Parameter.SEED, Parameter.FREQUENCY, Parameter.AMPLITUDE,
                        Parameter.OCTAVES, Parameter.LACUNARITY, Parameter.PERSISTENCE)
                ).entrySet().stream()
                .map(entry -> new Pair<>(
                        entry.getKey().toString(),
                        entry.getValue().stream().map(Parameter::toString).collect(Collectors.toSet())
                    )
                ).collect(Collectors.toMap(Pair::getKey, Pair::getValue)),
            view -> {
                var underlying = view.getView();
                underlying.setPadding(PADDING);
                underlying.setSpacing(SPACING);
                underlying.setAlignment(Pos.CENTER_LEFT);
                var buttons = view.getButtonGroup();
                buttons.setPadding(PADDING);
                buttons.setSpacing(SPACING);
                buttons.setAlignment(Pos.CENTER_LEFT);
                view.setHeaderFont(HEADER);
            }
        );

        // Main view
        AlgorithmView root = new AlgorithmView(
            new BorderPane(),
            settings,
            (o, p) -> new PerlinNoiseViewModel(o, p, COLORING.getMapper(), 10),
            view -> {
                view.getView().setPadding(PADDING);
            }
        );

        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        int width = (int) screen.getWidth() / 2;
        int height = (int) screen.getHeight() / 2;
        Scene scene = new Scene(root.getView(), width, height);

        primaryStage.setMinHeight(height);
        primaryStage.setMinWidth(width);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();

        primaryStage.show();
    }
}
