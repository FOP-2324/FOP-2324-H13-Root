package h13.ui;

import h13.ui.controls.ParameterTextField;
import javafx.beans.property.Property;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
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

    private final ParameterTextField seed = ParameterTextField.create("Seed", ParameterTextField.Type.INTEGER);

    private final ParameterTextField frequency = ParameterTextField.create("Frequency", ParameterTextField.Type.DOUBLE);

    private final ParameterTextField amplitude = ParameterTextField.create("Amplitude", ParameterTextField.Type.DOUBLE);

    private final ParameterTextField octaves = ParameterTextField.create("Octaves", ParameterTextField.Type.INTEGER);

    private final ParameterTextField persistence = ParameterTextField.create("Persistence", ParameterTextField.Type.DOUBLE);

    private final ParameterTextField lacunarity = ParameterTextField.create("Lacunarity", ParameterTextField.Type.DOUBLE);

    public AlgorithmPane() {
        setCenter(canvas);
        setRight(controls);

        initCanvas();
        initControls();
    }

    private void initCanvas() {
        setPadding(PADDING);
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
    }

}
