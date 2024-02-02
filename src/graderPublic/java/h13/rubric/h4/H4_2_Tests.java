package h13.rubric.h4;

import com.fasterxml.jackson.databind.JsonNode;
import h13.json.JsonConverters;
import h13.ui.layout.SettingsViewModel;
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

@DisplayName("4.2 | Welche Parameter für welchen Algorithmus?")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestForSubmission
public class H4_2_Tests extends H4_Tests {

    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.ofEntries(
        Map.entry("algorithms", node -> JsonConverters.toList(node, JsonNode::asText)),
        Map.entry("parameters", node -> JsonConverters.toList(node, JsonNode::asText)),
        Map.entry("configurations", node -> JsonConverters.toMap(node, Function.identity(),
            valueNode -> new LinkedHashSet<>(JsonConverters.toList(valueNode, JsonNode::asText)))),
        Map.entry("visible", node -> JsonConverters.toMap(node, Function.identity(),
            valueNode -> new LinkedHashSet<>(JsonConverters.toList(valueNode, JsonNode::asText))))
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
            Map.entry("visible", "The expected visibility of the parameters for the algorithms.")
        );
    }

    private void assertVisibility(JsonParameterSet parameterSet, String resource) {
        Map<String, BooleanProperty> algorithms = parameterSet.<List<String>>get("algorithms").stream()
            .collect(Collectors.toMap(
                Function.identity(),
                s -> new SimpleBooleanProperty(null, s, false))
            );
        Map<String, BooleanProperty> parameters = parameterSet.<List<String>>get("parameters").stream()
            .collect(Collectors.toMap(
                Function.identity(),
                s -> new SimpleBooleanProperty(null, s, false))
            );
        SettingsViewModel viewModel = new SettingsViewModel(algorithms, parameters);
        Map<String, Set<String>> configurations = parameterSet.get("configurations");
        viewModel.addVisibilityListener(configurations);
        Map<String, Set<String>> visible = parameterSet.get("visible");
        algorithms.forEach((algorithm, selection) -> {
            selection.set(true);
            Set<String> actual = parameters.entrySet().stream()
                .filter(entry -> entry.getValue().get())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
            Set<String> expected = visible.get(algorithm);
            Context context = contextBuilder(methodLink, resource)
                .add("Algorithms", algorithms.keySet())
                .add("Parameters", parameters.keySet())
                .add("Configurations", configurations)
                .build();
            Assertions2.assertEquals(expected, actual, context,
                result -> "The visibility of the parameters for the algorithm (selected) %s are incorrect"
                    .formatted(algorithm));
            selection.set(false);
            actual = parameters.entrySet().stream()
                .filter(entry -> !entry.getValue().get())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
            Assertions2.assertEquals(parameters.keySet().removeAll(expected), actual, context,
                result -> "The visibility of the parameters for the algorithm (selected) %s are incorrect"
                    .formatted(algorithm));
        });
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

    @DisplayName("Die Methode addVisibilityListener(Map) gibt das korrekte Ergebnis für mehre Parameter und "
        + "eine Option zurück.")
    @Order(19)
    @ParameterizedTest
    @JsonParameterSetTest(value = "H4_2_Criterion_03.json", customConverters = CONVERTERS_FIELD_NAME)
    public void testManyParameterAndSingleOption(JsonParameterSet parameterSet) {
        assertVisibility(parameterSet, "testManyParameterAndSingleOption");
    }

    @DisplayName("Die Methode addVisibilityListener(Map) gibt das korrekte Ergebnis für einfache Abhängigkeiten "
        + "zurück.")
    @Order(20)
    @ParameterizedTest
    @JsonParameterSetTest(value = "H4_2_Criterion_04.json", customConverters = CONVERTERS_FIELD_NAME)
    public void testSimple(JsonParameterSet parameterSet) {
        assertVisibility(parameterSet, "testSimple");
    }

    @DisplayName("Die Methode addVisibilityListener(Map) gibt das korrekte Ergebnis für komplexe Abhängigkeiten "
        + "zurück.")
    @Order(21)
    @ParameterizedTest
    @JsonParameterSetTest(value = "H4_2_Criterion_05.json", customConverters = CONVERTERS_FIELD_NAME)
    public void testComplex(JsonParameterSet parameterSet) {
        assertVisibility(parameterSet, "testComplex");
    }
}