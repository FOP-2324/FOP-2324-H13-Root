package h13.ui.layout;

import h13.rubric.TutorUtils;
import h13.util.Links;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Map;
import java.util.Set;

@DisplayName("H4.3 | Konfigurations - Ansicht")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestForSubmission
public class H4_3_TestsPrivate extends H4_Tests {

    private MethodLink methodLink;

    @BeforeAll
    public void globalSetup() {
        methodLink = Links.getMethod(getTypeLink(), "initialize");
    }

    @Override
    public TypeLink getTypeLink() {
        return BasicTypeLink.of(SettingsView.class);
    }

    @DisplayName("Die Methode initialize() fügt die korrekten Elemente in die Konfigurationsansicht ein und "
            + "initialisert ebenfalls die Sichtbarkeiten.")
    @Order(22)
    @Test
    public void testView() {
        Pair<Label, ChooserView> algorithms = new Pair<>(new Label("Algorithms"), new ChooserView(3, view -> {
        }));
        Pair<Label, ParameterView> parameters = new Pair<>(new Label("Parameters"), new ParameterView(view -> {
        }));
        VBox root = new VBox();
        Map<String, Set<String>> visibilities = Map.of();
        HBox buttonGroup = new HBox();
        Button generate = new Button("Generate");
        Button save = new Button("Save");
        SettingsViewModel settingsViewModel = TutorUtils.createSettingsViewModel(Map.of(), Map.of());

        // Prepare view
        SettingsView settingsView = TutorUtils.createSettingsView(
                root,
                algorithms,
                parameters,
                buttonGroup,
                generate,
                save,
                settingsViewModel,
                Map.of()
        );

        // Test cases
        settingsView.initialize();
        Parent view = settingsView.getView();
        Assertions2.assertTrue(view.getChildrenUnmodifiable().contains(algorithms.getKey()),
                Assertions2.emptyContext(),
                result -> "Algorithms label  is not added to the settings view.");
        Assertions2.assertTrue(view.getChildrenUnmodifiable().indexOf(
                        algorithms.getKey()) < view.getChildrenUnmodifiable().indexOf(
                        parameters.getValue().getView()),
                Assertions2.emptyContext(),
                result -> "Algorithms label is not added before the algorithm view.");
        Assertions2.assertTrue(view.getChildrenUnmodifiable().contains(algorithms.getValue().getView()),
                Assertions2.emptyContext(),
                result -> "Algorithms view is not added to the settings view.");
        Assertions2.assertTrue(view.getChildrenUnmodifiable().contains(parameters.getKey()),
                Assertions2.emptyContext(),
                result -> "Parameter label is not added to the settings view.");
        Assertions2.assertTrue(view.getChildrenUnmodifiable().contains(algorithms.getKey()),
                Assertions2.emptyContext(),
                result -> "Algorithms label  is not added to the settings view.");
        Assertions2.assertTrue(view.getChildrenUnmodifiable().indexOf(
                        parameters.getKey()) < view.getChildrenUnmodifiable().indexOf(
                        parameters.getValue().getView()),
                Assertions2.emptyContext(),
                result -> "Parameters label is not added before the parameters view.");
        Assertions2.assertTrue(view.getChildrenUnmodifiable().contains(parameters.getValue().getView()),
                Assertions2.emptyContext(),
                result -> "Parameter view is not added to the settings view.");
        Assertions2.assertTrue(view.getChildrenUnmodifiable().indexOf(
                        algorithms.getValue().getView()) < view.getChildrenUnmodifiable().indexOf(
                        parameters.getValue().getView()),
                Assertions2.emptyContext(),
                result -> "Algorithms view is not added before the parameter view.");

        Assertions2.assertTrue(
                buttonGroup.getChildren().stream().anyMatch(node -> node instanceof Button button
                        && (button.getText().equals("Generate") || button.getText().equals("Save"))),
                Assertions2.emptyContext(),
                result -> "The button group does not contain the correct buttons."
        );
        Assertions2.assertTrue(view.getChildrenUnmodifiable().indexOf(
                        algorithms.getValue().getView()) < view.getChildrenUnmodifiable().indexOf(
                        buttonGroup),
                Assertions2.emptyContext(),
                result -> "Parameter view is not added before the button group.");
        Mockito.verify(settingsViewModel, Mockito.times(1)).addVisibilityListener(visibilities);
    }
}
