package h13.ui.layout;

import h13.ui.controls.NumberField;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.util.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The viw model for {@link SettingsView} that handles the visibility of parameters based on the selected options.
 *
 * @author Nhan Huynh
 */
public class SettingsViewModel {

    /**
     * The available options.
     */
    private final Map<String, BooleanProperty> options;

    /**
     * Creates a settings view model with the given available options.
     *
     * @param options the available options
     */
    public SettingsViewModel(Collection<CheckBox> options) {
        this.options = new HashMap<>(options.size());

        for (CheckBox option : options) {
            this.options.put(option.getText(), option.selectedProperty());
        }
    }

    /**
     * Adds visibility listeners to the given parameters based on the given configurations.
     *
     * @param configurations the configurations that specify which parameters are visible for which options
     * @param parameters     the parameters to add visibility listeners to
     */
    public void addVisibilityListener(Map<String, Set<String>> configurations, Map<String, Pair<Label, NumberField>> parameters) {
        Map<String, BooleanBinding> visibilities = new HashMap<>(parameters.size());

        // Create state binding for parameters when an option is selected, the binding is true
        for (var parameter : parameters.entrySet()) {
            String parameterName = parameter.getKey();
            BooleanBinding current = visibilities.computeIfAbsent(
                parameterName,
                key -> {
                    BooleanProperty never = new SimpleBooleanProperty(SettingsViewModel.this, parameterName, false);
                    return Bindings.createBooleanBinding(never::get, never);
                }
            );
            for (var option : configurations.entrySet()) {
                String optionName = option.getKey();
                BooleanProperty state = options.get(optionName);
                Set<String> visibleParameters = option.getValue();
                // Selected options are visible
                if (visibleParameters.contains(parameterName)) {
                    current = current.or(state);
                }
            }
            visibilities.put(parameterName, current);
        }

        // Since the binding is true when an option is selected, the parameter is disabled when the binding is false
        for (var visibility : visibilities.entrySet()) {
            var parameterName = visibility.getKey();
            var visible = visibility.getValue();
            var fields = parameters.get(parameterName);
            for (var node : new Node[]{fields.getKey(), fields.getValue()}) {
                node.disableProperty().bind(visible.not());
            }
        }
    }

}
