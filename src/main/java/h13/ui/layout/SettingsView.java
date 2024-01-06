package h13.ui.layout;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.util.Map;
import java.util.Set;

/**
 * The view for settings the options related to parameter visibilities.
 *
 * @author Nhan Huynh
 */
public class SettingsView extends AbstractView<SettingsView, VBox> implements View {

    /**
     * The options of the view.
     */
    private final Pair<Label, ChooserView> options;

    /**
     * The parameters of the view.
     */
    private final Pair<Label, ParameterView> parameters;

    /**
     * The button group for the view.
     */
    private final HBox buttonGroup = new HBox();

    /**
     * The button for generating some result of a task
     */
    private final Button generate = new Button("Generate");

    /**
     * The button for saving the current settings.
     */
    private final Button save = new Button("Save");

    /**
     * The view model of the view for handling the logic.
     */
    private final SettingsViewModel viewModel;

    /**
     * The configurations specifying when a parameter is visible.
     */
    private final Map<String, Set<String>> visibilities;

    /**
     * Creates a setting view with the given options and parameters and configurations specifying when a parameter is
     * visible.
     *
     * @param root          the root of the view
     * @param options       the view for choosing the options
     * @param parameters    the view for showing the parameters
     * @param visibilities  the configurations specifying when a parameter is visible
     * @param configuration the configuration of the view
     */
    public SettingsView(
        VBox root,
        Pair<String, ChooserView> options,
        Pair<String, ParameterView> parameters,
        Map<String, Set<String>> visibilities,
        ViewConfiguration<SettingsView> configuration
    ) {
        super(root, configuration);
        this.options = new Pair<>(new Label(options.getKey()), options.getValue());
        this.parameters = new Pair<>(new Label(parameters.getKey()), parameters.getValue());
        this.viewModel = new SettingsViewModel(
            options.getValue().selectedProperties()
            , parameters.getValue().disableProperties()
        );
        this.visibilities = visibilities;
        initialize();
        config(this);
    }

    /**
     * Creates a setting view with the given options and parameters and configurations specifying when a parameter is
     * visible.
     *
     * @param options       the view for choosing the options
     * @param parameters    the view for showing the parameters
     * @param visibilities  the configurations specifying when a parameter is visible
     * @param configuration the configuration of the view
     */
    public SettingsView(
        Pair<String, ChooserView> options,
        Pair<String, ParameterView> parameters,
        Map<String, Set<String>> visibilities,
        ViewConfiguration<SettingsView> configuration
    ) {
        this(new VBox(), options, parameters, visibilities, configuration);
    }


    @Override
    public void initialize() {
        root.getChildren().addAll(
            options.getKey(), options.getValue().getView(),
            parameters.getKey(), parameters.getValue().getView(),
            buttonGroup
        );
        buttonGroup.getChildren().addAll(generate, save);
        viewModel.addVisibilityListener(visibilities);
    }

    /**
     * Returns the options of the view.
     *
     * @return the options of the view
     */
    public Pair<Label, ChooserView> getOptions() {
        return options;
    }

    /**
     * Returns the parameters of the view.
     *
     * @return the parameters of the view
     */
    public Pair<Label, ParameterView> getParameters() {
        return parameters;
    }

    /**
     * Returns the button group of the view.
     *
     * @return the button group of the view
     */
    public HBox getButtonGroup() {
        return buttonGroup;
    }

    /**
     * Returns the button for generating some result of a task.
     *
     * @return the button for generating some result of a task
     */
    public Button getGenerate() {
        return generate;
    }

    /**
     * Returns the button for saving the current settings.
     *
     * @return the button for saving the current settings
     */
    public Button getSave() {
        return save;
    }

    /**
     * Sets the header font of the view.
     *
     * @param font the font to use
     */
    public void setHeaderFont(Font font) {
        for (Label label : new Label[]{options.getKey(), parameters.getKey()}) {
            label.setFont(font);
        }
    }
}
