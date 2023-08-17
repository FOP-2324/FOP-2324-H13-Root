package h13.ui;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;


public class AlgorithmView extends BorderPane {

    private final Canvas canvas = new Canvas();
    private final GridPane controlPane = new GridPane();
    private final CheckBox improvedCheckBox = new CheckBox("Improved");
    private final CheckBox fractalCheckBox = new CheckBox("Fractal");

    private final Label seedLabel = new Label("Seed: ");
    private final TextField seedTextField = new TextField();

    private final Label frequencyLabel = new Label("Frequency: ");
    private final TextField frequencyTextField = new TextField();

    private final Label amplitudeLabel = new Label("Amplitude: ");
    private final TextField amplitudeTextField = new TextField();
    private final Label octavesLabel = new Label("Octaves: ");
    private final TextField octavesTextField = new TextField();

    private final Label persistenceLabel = new Label("Persistence: ");
    private final TextField persistenceTextField = new TextField();

    private final Label lacunarityLabel = new Label("Lacunarity: ");
    private final TextField lacunarityTextField = new TextField();

    private final Button generateButton = new Button("Generate");
    private final AlgorithmViewModel viewModel = new AlgorithmViewModel();

    public AlgorithmView() {
        // Root settings
        setPadding(new Insets(10));
        setCenter(canvas);
        setRight(controlPane);

        // Control pane settings
        controlPane.setPadding(new Insets(10));
        controlPane.setHgap(5);
        controlPane.setVgap(5);
        controlPane.setAlignment(Pos.CENTER_LEFT);

        // Control pane content
        controlPane.addRow(1, improvedCheckBox, fractalCheckBox);
        controlPane.addRow(2, seedLabel, seedTextField);
        controlPane.addRow(3, frequencyLabel, frequencyTextField);
        controlPane.addRow(4, amplitudeLabel, amplitudeTextField);
        controlPane.addRow(5, octavesLabel, octavesTextField);
        controlPane.addRow(6, persistenceLabel, persistenceTextField);
        controlPane.addRow(7, lacunarityLabel, lacunarityTextField);
        controlPane.addRow(8, generateButton);

        // Canvas binding
        viewModel.bindGenerate(canvas.getGraphicsContext2D());

        // Disable controls by default
        Control[] controls = new Control[]{
            frequencyLabel, frequencyTextField,
            amplitudeLabel, amplitudeTextField,
            octavesLabel, octavesTextField,
            persistenceLabel, persistenceTextField,
            lacunarityLabel, lacunarityTextField
        };
        for (Control control : controls) {
            control.setDisable(true);
        }

        // Size binding
        widthProperty().addListener((observable, oldValue, newValue) -> {
            double width = newValue.doubleValue();
            viewModel.widthProperty().set(width);
            canvas.setWidth(width - controlPane.getWidth());
        });
        heightProperty().addListener((observable, oldValue, newValue) -> {
            double height = newValue.doubleValue();
            viewModel.heightProperty().setValue(height);
            canvas.setHeight(height);
        });

        // Checkbox binding
        improvedCheckBox.selectedProperty().bindBidirectional(viewModel.isImprovedProperty());
        fractalCheckBox.selectedProperty().bindBidirectional(viewModel.isFractalProperty());

        // Text formatter
        seedTextField.setTextFormatter(viewModel.getIntegerFormatter());
        frequencyTextField.setTextFormatter(viewModel.getDoubleFormatter());
        octavesTextField.setTextFormatter(viewModel.getIntegerFormatter());
        persistenceTextField.setTextFormatter(viewModel.getDoubleFormatter());
        lacunarityTextField.setTextFormatter(viewModel.getDoubleFormatter());

        // Parameter binding
        Bindings.bindBidirectional(
            seedTextField.textProperty(),
            viewModel.seedProperty(),
            viewModel.getIntegerConverter()
        );
        Bindings.bindBidirectional(
            frequencyTextField.textProperty(),
            viewModel.frequencyProperty(),
            viewModel.getDoubleConverter()
        );
        Bindings.bindBidirectional(
            amplitudeTextField.textProperty(),
            viewModel.amplitudeProperty(),
            viewModel.getDoubleConverter()
        );
        Bindings.bindBidirectional(
            octavesTextField.textProperty(),
            viewModel.octavesProperty(),
            viewModel.getIntegerConverter()
        );
        Bindings.bindBidirectional(
            persistenceTextField.textProperty(),
            viewModel.persistenceProperty(),
            viewModel.getDoubleConverter()
        );
        Bindings.bindBidirectional(
            lacunarityTextField.textProperty(),
            viewModel.lacunarityProperty(),
            viewModel.getDoubleConverter()
        );

        // Generate binding
        generateButton.pressedProperty().addListener((observable, oldValue, newValue) -> {
            viewModel.isGenerateProperty().set(newValue);
        });

        // Show/hide controls binding
        viewModel.bindFrequency(frequencyLabel, frequencyTextField);
        viewModel.bindFractal(
            octavesLabel, octavesTextField,
            amplitudeLabel, amplitudeTextField,
            persistenceLabel, persistenceTextField,
            lacunarityLabel, lacunarityTextField
        );
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public GridPane getControlPane() {
        return controlPane;
    }

    public CheckBox getImprovedCheckBox() {
        return improvedCheckBox;
    }

    public CheckBox getFractalCheckBox() {
        return fractalCheckBox;
    }

}
