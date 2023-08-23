package h13.ui;

import h13.ui.controls.DoubleTextField;
import h13.ui.controls.IntegerTextField;
import h13.ui.controls.LongTextField;
import h13.ui.controls.ParameterTextField;
import javafx.beans.property.Property;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class AlgorithmPane extends BorderPane {

    private static final int SPACING = 10;

    private static final int VGAP = 5;

    private static final int HGAP = 10;

    private static final Insets PADDING = new Insets(10);

    private final AlgorithmController controller = new AlgorithmController();

    private final Canvas canvas = new Canvas();

    private final GridPane controls = new GridPane();

    private final ParameterTextField seed = new ParameterTextField("Seed", new LongTextField(LongTextField.POSITIVE));

    private final ParameterTextField frequency = new ParameterTextField("Frequency", new DoubleTextField(DoubleTextField.POSITIVE));

    private final ParameterTextField amplitude = new ParameterTextField("Amplitude", new DoubleTextField(DoubleTextField.POSITIVE));

    private final ParameterTextField octaves = new ParameterTextField("Octaves", new IntegerTextField(IntegerTextField.POSITIVE));

    private final ParameterTextField persistence = new ParameterTextField("Persistence", new DoubleTextField(DoubleTextField.POSITIVE));

    private final ParameterTextField lacunarity = new ParameterTextField("Lacunarity", new DoubleTextField(DoubleTextField.POSITIVE));

    private final Button generate = new Button("Generate");

    public AlgorithmPane() {
        setCenter(canvas);
        setRight(controls);

        initCanvas();
        initControls();
    }

    private void initCanvas() {
        setPadding(PADDING);
        canvas.widthProperty().bind(
            widthProperty()
                .subtract(controls.widthProperty())
                .subtract(PADDING.getLeft())
                .subtract(PADDING.getRight())
        );
        canvas.heightProperty().bind(
            heightProperty()
                .subtract(PADDING.getTop())
                .subtract(PADDING.getBottom())
        );
    }


    private void initControls() {
        // Layout
        controls.setPadding(PADDING);
        controls.setAlignment(Pos.CENTER_RIGHT);
        controls.setVgap(VGAP);
        controls.setHgap(HGAP);
        ParameterTextField[] allFields = {seed, frequency, amplitude, octaves, persistence, lacunarity};
        for (int i = 0; i < allFields.length; i++) {
            controls.addRow(i, allFields[i].getControls());
        }
        controls.addRow(allFields.length, generate);

        // Visibility of default parameters
        ParameterTextField[] fractalFields = {amplitude, octaves, persistence, lacunarity};
        for (ParameterTextField field : fractalFields) {
            field.setDisable(true);
        }

        // Bindings
        @SuppressWarnings("unchecked")
        Property<Number>[] properties = (Property<Number>[]) new Property<?>[]{
            controller.seedProperty(),
            controller.frequencyProperty(),
            controller.amplitudeProperty(),
            controller.octavesProperty(),
            controller.persistenceProperty(),
            controller.lacunarityProperty()
        };
        for (int i = 0; i < properties.length; i++) {
            allFields[i].valueProperty().bindBidirectional(properties[i]);
        }
        controller.addGenerateListener(generate.pressedProperty(), canvas);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public GridPane getControls() {
        return controls;
    }
}
