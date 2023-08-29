package h13.ui.layout;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Map;
import java.util.Set;

public class SettingsView implements View {

    private final VBox root;
    private final SettingsViewModel viewModel;

    private final Label optionsLabel;
    private final ChooserView options;

    private final Label parametersLabel;

    private final ParameterView parameters;

    private final Button submitButton;

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

    public Label getOptionsLabel() {
        return optionsLabel;
    }

    public ChooserView getOptions() {
        return options;
    }

    public Label getParametersLabel() {
        return parametersLabel;
    }

    public ParameterView getParameters() {
        return parameters;
    }

    public Button getSubmitButton() {
        return submitButton;
    }

    public void setHeaderFont(Font font) {
        for (var label : new Label[]{optionsLabel, parametersLabel}) {
            label.setFont(font);
        }
    }

}
