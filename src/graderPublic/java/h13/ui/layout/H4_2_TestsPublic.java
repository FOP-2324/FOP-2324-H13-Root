package h13.ui.layout;

import com.fasterxml.jackson.databind.JsonNode;
import h13.json.JsonConverters;
import h13.util.Links;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@DisplayName("H4.2 | Welche Parameter für welchen Algorithmus?")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestForSubmission
public class H4_2_TestsPublic extends H4_Tests {

    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.ofEntries(
        Map.entry("algorithms", node -> JsonConverters.toList(node, JsonNode::asText)),
        Map.entry("parameters", node -> JsonConverters.toList(node, JsonNode::asText)),
        Map.entry("configurations", node -> JsonConverters.toMap(node, Function.identity(),
            valueNode -> new LinkedHashSet<>(JsonConverters.toList(valueNode, JsonNode::asText)))),
        Map.entry("selections", node -> new LinkedHashSet<>(JsonConverters.toList(node, JsonNode::asText))),
        Map.entry("active", node -> new LinkedHashSet<>(JsonConverters.toList(node, JsonNode::asText))),
        Map.entry("disable", node -> new LinkedHashSet<>(JsonConverters.toList(node, JsonNode::asText)))
    );

    private MethodLink methodLink;

    @BeforeAll
    public void globalSetup() {
        methodLink = Links.getMethod(getTypeLink(), "addVisibilityListener");
    }

    @Override
    public TypeLink getTypeLink() {
        return BasicTypeLink.of(SettingsViewModel.class);
    }

    @Override
    public Map<String, String> getContextInformation() {
        return Map.ofEntries(
            Map.entry("algorithms", "The algorithms that can be selected."),
            Map.entry("parameters", "The parameters that can be visible."),
            Map.entry("configurations", "The configurations that specify which parameters are visible for "
                + "which options."),
            Map.entry("selections", "The selected algorithms."),
            Map.entry("active", "The expected visibility of the parameters for the algorithms."),
            Map.entry("disable", "The expected non visibility of the parameters for the algorithms.")
        );
    }

    private void assertVisibility(JsonParameterSet parameterSet, String resource) {
        Map<String, BooleanProperty> algorithms = parameterSet.<List<String>>get("algorithms").stream()
            .collect(Collectors.toMap(
                Function.identity(),
                s -> new SimpleBooleanProperty(this, s, false))
            );
        Map<String, BooleanProperty> parameters = parameterSet.<List<String>>get("parameters").stream()
            .collect(Collectors.toMap(
                Function.identity(),
                s -> new SimpleBooleanProperty(this, s, false))
            );
        SettingsViewModel viewModel = new SettingsViewModel(algorithms, parameters);
        Map<String, Set<String>> configurations = parameterSet.get("configurations");
        viewModel.addVisibilityListener(configurations);

        // Test cases
        Set<String> selections = parameterSet.get("selections");
        Set<String> active = parameterSet.get("active");
        Set<String> disable = parameterSet.get("disable");


        algorithms.values().forEach(algorithm -> algorithm.set(false));
        selections.forEach(algorithm -> algorithms.get(algorithm).set(true));

        Context context = contextBuilder(methodLink, resource)
            .add("Algorithms", algorithms.keySet())
            .add("Parameters", parameters.keySet())
            .add("Configurations", configurations)
            .add("Selections", selections)
            .build();

        // Check active
        // Negate since the properties defines disabled parameters
        Set<String> actualActive = parameters.entrySet().stream()
            .filter(entry -> !entry.getValue().get())
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
        Assertions2.assertEquals(active, actualActive, context,
            result -> "The visibility (active) of the parameters for the selected algorithms %s are incorrect"
                .formatted(selections));

        // Check disable
        Set<String> actualDisable = parameters.entrySet().stream()
            .filter(entry -> entry.getValue().get())
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
        Assertions2.assertEquals(disable, actualDisable, context,
            result -> "The visibility (disabled) of the parameters for the selected algorithms %s are incorrect"
                .formatted(algorithms));
    }

    @DisplayName("Die Methode addVisibilityListener(Map) gibt das korrekte Ergebnis für einen Parameter und einen "
        + "Option zurück.")
    @Order(17)
    @ParameterizedTest
    @JsonParameterSetTest(value = "H4_2_Criterion_01.json", customConverters = CONVERTERS_FIELD_NAME)
    public void testSingleParameterAndOption(JsonParameterSet parameterSet) {
        assertVisibility(parameterSet, "testSingleParameterAndOption");
    }

    @DisplayName("Die Methode addVisibilityListener(Map) gibt das korrekte Ergebnis für einen Parameter und "
        + "mehrere Optionen zurück.")
    @Order(18)
    @ParameterizedTest
    @JsonParameterSetTest(value = "H4_2_Criterion_02.json", customConverters = CONVERTERS_FIELD_NAME)
    public void testSingleParameterAndManyOption(JsonParameterSet parameterSet) {
        assertVisibility(parameterSet, "testSingleParameterAndManyOption");
    }
}
