package h13.ui.layout;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

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
     * The options that can be selected.
     */
    private final Map<String, BooleanProperty> algorithmsOptions;

    /**
     * The parameters that can be visible.
     */
    private final Map<String, BooleanProperty> availableParameters;

    /**
     * Creates a new view model with the given options and parameters.
     *
     * @param algorithmsOptions   the options that can be selected
     * @param availableParameters the parameters that can be visible
     */
    public SettingsViewModel(
        Map<String, BooleanProperty> algorithmsOptions,
        Map<String, BooleanProperty> availableParameters
    ) {
        this.algorithmsOptions = algorithmsOptions;
        this.availableParameters = availableParameters;
    }

    /**
     * Adds visibility listeners to the given parameters based on the given configurations.
     *
     * @param configurations the configurations that specify which parameters are visible for which options
     */
    @StudentImplementationRequired
    public void addVisibilityListener(Map<String, Set<String>> configurations) {
        // TODO H4.3
        Map<String, BooleanBinding> visibilities = new HashMap<>(availableParameters.size());
        // Create state binding for parameters when an option is selected, the binding is true
        for (var parameter : availableParameters.entrySet()) {
            String parameterName = parameter.getKey();
            BooleanBinding current = visibilities.computeIfAbsent(
                parameterName,
                key -> {
                    BooleanProperty never = new SimpleBooleanProperty(SettingsViewModel.this, parameterName, false);
                    return Bindings.createBooleanBinding(never::get, never);
                }
            );
            for (Map.Entry<String, Set<String>> option : configurations.entrySet()) {
                String optionName = option.getKey();
                BooleanProperty state = algorithmsOptions.get(optionName);
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
            String parameterName = visibility.getKey();
            BooleanBinding visible = visibility.getValue();
            BooleanProperty fields = availableParameters.get(parameterName);
            fields.bind(visible.not());
        }
    }
}
