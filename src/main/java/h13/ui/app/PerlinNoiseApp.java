package h13.ui.app;

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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Map;
import java.util.Set;
import java.util.function.DoubleFunction;
import java.util.stream.Collectors;

public class PerlinNoiseApp extends Application {

    private static final int SPACING = 10;

    private static final Insets PADDING = new Insets(10);

    private static final int VGAP = 5;

    private static final int HGAP = 10;

    private static final Font HEADER;


    static {
        Font font = Font.getDefault();
        HEADER = Font.font(font.getName(), FontWeight.BOLD, font.getSize() * 1.25);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Perlin Noise");
        primaryStage.setResizable(false);

        ChooserView options = new ChooserView(new GridPane(), 3);
        options.view().setPadding(PADDING);
        options.view().setHgap(HGAP);
        options.view().setVgap(VGAP);
        for (var algorithm : Algorithm.values()) {
            options.add(algorithm.toString());
        }

        CheckBox defaultOption = options.get(Algorithm.SIMPLE.toString());
        defaultOption.setSelected(true);
        defaultOption.setDisable(true);

        ParameterView parameters = new ParameterView(new GridPane());
        parameters.view().setPadding(PADDING);
        parameters.view().setHgap(HGAP);
        parameters.view().setVgap(VGAP);
        for (Parameter parameter : Parameter.values()) {
            var field = switch (parameter) {
                case SEED -> new LongField(LongField.POSITIVE_ONLY);
                case OCTAVES -> new IntegerField(IntegerField.POSITIVE_ONLY);
                default -> new DoubleField(DoubleField.POSITIVE_ONLY);
            };
            field.setValue(parameter.defaultValue());
            field.setPromptValue(parameter.defaultValue());
            parameters.add(parameter.toString(), field);
        }

        SettingsView settings = new SettingsView(
                new VBox(),
                "Algorithms", options,
                "Parameters", parameters,
                "Generate",
                Map.of(
                                Algorithm.SIMPLE, Set.of(Parameter.SEED, Parameter.FREQUENCY),
                                Algorithm.IMPROVED, Set.of(Parameter.SEED, Parameter.FREQUENCY), Algorithm.FRACTAL,
                                Set.of(
                                        Parameter.SEED, Parameter.FREQUENCY, Parameter.AMPLITUDE,
                                        Parameter.OCTAVES, Parameter.LACUNARITY, Parameter.PERSISTENCE)
                        ).entrySet().stream()
                        .collect(
                                Collectors.toMap(
                                        entry -> entry.getKey().toString(),
                                        entry -> entry.getValue().stream().map(Parameter::toString)
                                                .collect(Collectors.toSet()))
                        )
        );
        settings.view().setPadding(PADDING);
        settings.view().setSpacing(SPACING);
        settings.view().setAlignment(Pos.CENTER_LEFT);
        settings.setHeaderFont(HEADER);

        DoubleFunction<Color> colorMapper = value -> {
            if (value <= 0.5) {
                // Water color (blue)
                return Color.color(0, 0f, value * 2f);
            } else {
                // Land color (green)
                return Color.color(0f, value, 0f);
            }
        };

        AlgorithmView root = new AlgorithmView(
                new BorderPane(),
                settings,
                (o, p) -> new PerlinNoiseViewModel(o, p, colorMapper, 10)
        );
        root.view().setPadding(PADDING);

        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        int width = (int) screen.getWidth() / 2;
        int height = (int) screen.getHeight() / 2;
        Scene scene = new Scene(root.view(), width, height);

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

}