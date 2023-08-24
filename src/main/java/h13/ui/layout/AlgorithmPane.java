package h13.ui.layout;

import h13.ui.controls.DoubleTextField;
import h13.ui.controls.LongTextField;
import h13.ui.controls.NumberTextField;
import h13.ui.controls.ParameterTextField;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Arrays;

/**
 * A pane that visualizes a Perlin noise algorithm which can be configured.
 *
 * @author Mhan Huynh
 */
public class AlgorithmPane extends BorderPane {

    /**
     * The spacing between controls.
     */
    private static final int SPACING = 10;

    /**
     * The padding of the pane.
     */
    private static final Insets PADDING = new Insets(10);

    /**
     * The vertical gap between controls.
     */
    private static final int VGAP = 5;

    /**
     * The horizontal gap between controls.
     */
    private static final int HGAP = 10;

    /**
     * The font of the header labels.
     */
    private final static Font HEADER;


    static {
        Font font = Font.getDefault();
        HEADER = Font.font(font.getName(), FontWeight.BOLD, font.getSize() * 1.25);
    }

    /**
     * The controller of this pane which handles the logic.
     */
    private final AlgorithmPaneController controller = new AlgorithmPaneController();

    /**
     * The visualization of the algorithm.
     */
    private final Canvas visualization = new Canvas();

    /**
     * The controls of the pane.
     */
    private final VBox controls = new VBox();

    /**
     * The label of the algorithm selection.
     */
    private final Label algorithmLabel = new Label("Algorithm");

    /**
     * The algorithm selection.
     */
    private final OptionsView algorithm = new OptionsView(
        3,
        Arrays.stream(Algorithm.values())
            .map(Algorithm::toString).toArray(String[]::new)
    );

    /**
     * The label of the parameters' selection.
     */
    private final Label parametersLabel = new Label("Parameters");

    /**
     * The parameters' selection.
     */
    private final ParametersView parameters = new ParametersView();

    /**
     * The generate button.
     */
    private final Button generate = new Button("Generate");

    /**
     * Creates a new algorithm pane.
     */
    public AlgorithmPane() {
        setCenter(visualization);
        setRight(controls);

        initVisualization();
        initAlgorithm();
        initParameters();
        initControls();
    }

    /**
     * Initializes the visualization.
     */
    private void initVisualization() {
        setPadding(PADDING);
        visualization.widthProperty().bind(
            widthProperty()
                .subtract(controls.widthProperty())
                .subtract(PADDING.getLeft())
                .subtract(PADDING.getRight())
        );
        visualization.heightProperty().bind(
            heightProperty()
                .subtract(PADDING.getTop())
                .subtract(PADDING.getBottom())
        );
    }

    /**
     * Initializes the controls.
     */
    private void initControls() {
        controls.setSpacing(SPACING);
        controls.setPadding(PADDING);
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.getChildren().addAll(
            algorithmLabel, algorithm.getView(),
            parametersLabel, parameters.getView(),
            generate);
        controller.addGenerateListener(generate.pressedProperty(), visualization);
    }

    /**
     * Initializes the algorithm selection.
     */
    private void initAlgorithm() {
        algorithmLabel.setFont(HEADER);
        GridPane view = algorithm.getView();
        view.setVgap(VGAP);
        view.setHgap(HGAP);

        // Default selection
        CheckBox first = algorithm.getBoxes().get(0);
        first.setSelected(true);
        first.setDisable(true);

        // Bindings
        algorithm.getBoxes()
            .forEach(box -> controller.getAlgorithm(box.getText()).bind(box.selectedProperty()));
    }

    /**
     * Initializes the parameters' selection.
     */
    private void initParameters() {
        parametersLabel.setFont(HEADER);
        GridPane view = parameters.getView();
        view.setVgap(VGAP);
        view.setHgap(HGAP);

        NumberTextField[] fields = {
            new LongTextField(),
            new DoubleTextField(),
            new DoubleTextField(),
            new LongTextField(),
            new DoubleTextField(),
            new DoubleTextField(),
        };
        Parameter[] params = Parameter.values();
        for (int i = 0; i < params.length; i++) {
            Parameter parameter = params[i];
            NumberTextField field = fields[i];
            ParameterTextField parameterTextField = new ParameterTextField(parameter.toString(), field);

            // Add to pane
            parameters.addParameter(parameterTextField);

            // Bindings
            controller.getParameterProperty(parameter).bind(parameterTextField.getNumberTextField().valueProperty());

            // Default values
            Number defaultValue = parameter.getDefaultValue();
            field.setValue(defaultValue);
            field.setPromptValue(defaultValue);

           BooleanProperty fractal =  controller.getAlgorithm(Algorithm.FRACTAL.toString());
            // Visibility of parameters
            switch (parameter) {
                case SEED, FREQUENCY -> {
                }
                default -> {
                    field.setDisable(true);
                    field.disableProperty().bind(fractal.not());
                }
            }
        }
    }

}
