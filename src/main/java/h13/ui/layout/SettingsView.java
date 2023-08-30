package h13.ui.layout;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Map;
import java.util.Set;

/**
 * The view for settings the options related to parameters visibilities.
 *
 * @author Nhan Huynh
 */
public class SettingsView implements View {

    /**
     * The root of the view.
     */
    private final VBox root;

    /**
     * The view model of the view for handling the logic.
     */
    private final SettingsViewModel viewModel;

    /**
     * The label of the options.
     */
    private final Label optionsLabel;

    /**
     * The view for choosing the options.
     */
    private final ChooserView options;

    /**
     * The label of the parameters.
     */
    private final Label parametersLabel;

    /**
     * The view for showing the parameters.
     */
    private final ParameterView parameters;

    /**
     * The button for submitting the options and parameters.
     */
    private final Button submitButton;

    /**
     * Creates a setting view with the given options and parameters and configurations specifying when a parameter is
     * visible.
     *
     * @param root            the root of the view
     * @param optionsLabel    the label of the options
     * @param options         the view for choosing the options
     * @param parametersLabel the label of the parameters
     * @param parameters      the view for showing the parameters
     * @param submitButton    the button for submitting the options and parameters
     * @param configurations  the configurations specifying when a parameter is visible
     */
    public SettingsView(
        VBox root,
        Label optionsLabel,
        ChooserView options,
        Label parametersLabel,
        ParameterView parameters,
        Button submitButton,
        Map<String, Set<String>> configurations
    ) {
        this.root = root;
        this.optionsLabel = optionsLabel;
        this.options = options;
        this.parametersLabel = parametersLabel;
        this.parameters = parameters;
        this.submitButton = submitButton;
        this.viewModel = new SettingsViewModel(options.getOptions().values());
        viewModel.addVisibilityListener(configurations, parameters.getParameters());

        root.getChildren().addAll(optionsLabel, options.view(), parametersLabel, parameters.view(), submitButton);
    }

    /**
     * Creates a setting view with the given options and parameters and configurations specifying when a parameter is
     * visible.
     *
     * @param root            the root of the view
     * @param optionsLabel    the label of the options
     * @param options         the view for choosing the options
     * @param parametersLabel the label of the parameters
     * @param parameters      the view for showing the parameters
     * @param submitButton    the button for submitting the options and parameters
     * @param configurations  the configurations specifying when a parameter is visible
     */
    public SettingsView(
        VBox root,
        String optionsLabel,
        ChooserView options,
        String parametersLabel,
        ParameterView parameters,
        String submitButton,
        Map<String, Set<String>> configurations
    ) {
        this(
            root,
            new Label(optionsLabel), options,
            new Label(parametersLabel), parameters,
            new Button(submitButton),
            configurations
        );
    }

    @Override
    public VBox view() {
        return root;
    }

    /**
     * Returns the label of the options.
     *
     * @return the label of the options
     */
    public Label getOptionsLabel() {
        return optionsLabel;
    }

    /**
     * Returns the view for choosing the options.
     *
     * @return the view for choosing the options
     */
    public ChooserView getOptions() {
        return options;
    }

    /**
     * Returns the label of the parameters.
     *
     * @return the label of the parameters
     */
    public Label getParametersLabel() {
        return parametersLabel;
    }

    /**
     * Returns the view for showing the parameters.
     *
     * @return the view for showing the parameters
     */
    public ParameterView getParameters() {
        return parameters;
    }

    /**
     * Returns the button for submitting the options and parameters.
     *
     * @return the button for submitting the options and parameters
     */
    public Button getSubmitButton() {
        return submitButton;
    }

    /**
     * Sets the font of the headers in this view.
     *
     * @param font the font of the headers in this view
     */
    public void setHeaderFont(Font font) {
        for (Label label : new Label[]{optionsLabel, parametersLabel}) {
            label.setFont(font);
        }
    }

}
