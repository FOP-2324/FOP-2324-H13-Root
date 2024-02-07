package h13.rubric;

import h13.ui.layout.ChooserView;
import h13.ui.layout.ParameterView;
import h13.ui.layout.SettingsView;
import h13.ui.layout.SettingsViewModel;
import h13.util.Links;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.mockito.Mockito;
import org.tudalgo.algoutils.tutor.general.reflections.BasicPackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public final class TutorUtils {

    private TutorUtils() {

    }

    public static String toString(Point2D[] gradients) {
        return Arrays.stream(gradients)
            .map(g -> "(%s, %s)".formatted(g.getX(), g.getY()))
            .collect(Collectors.joining(", "));
    }

    public static SettingsViewModel createSettingsViewModel(
        Map<String, BooleanProperty> algorithms,
        Map<String, BooleanProperty> parameters
    ) {
        SettingsViewModel settingsViewModel = Mockito.mock(SettingsViewModel.class);
        TypeLink typeLink = Links.getType(BasicPackageLink.of("h13.ui.layout"), SettingsViewModel.class);
        FieldLink algorithmsLink = Links.getField(typeLink, "algorithms");
        algorithmsLink.set(settingsViewModel, algorithms);
        FieldLink parametersLink = Links.getField(typeLink, "parameters");
        parametersLink.set(settingsViewModel, parameters);
        return settingsViewModel;
    }

    public static SettingsView createSettingsView(
        VBox root,
        Pair<Label, ChooserView> algorithms,
        Pair<Label, ParameterView> parameters,
        HBox buttonGroup,
        Button generate,
        Button save,
        SettingsViewModel settingsViewModel,
        Map<String, BooleanProperty> visibilities
    ) {
        SettingsView settingsView = Mockito.mock(SettingsView.class, Mockito.CALLS_REAL_METHODS);
        TypeLink typeLink = Links.getType(BasicPackageLink.of("h13.ui.layout"), SettingsView.class);
        FieldLink rootLink = Links.getField(typeLink.superType(), "root");
        rootLink.set(settingsView, root);
        FieldLink algorithmsLink = Links.getField(typeLink, "algorithms");
        algorithmsLink.set(settingsView, algorithms);
        FieldLink parametersLink = Links.getField(typeLink, "parameters");
        parametersLink.set(settingsView, parameters);
        FieldLink buttonGroupLink = Links.getField(typeLink, "buttonGroup");
        buttonGroupLink.set(settingsView, buttonGroup);
        FieldLink generateLink = Links.getField(typeLink, "generate");
        generateLink.set(settingsView, generate);
        FieldLink saveLink = Links.getField(typeLink, "save");
        saveLink.set(settingsView, save);
        FieldLink viewModelLink = Links.getField(typeLink, "viewModel");
        viewModelLink.set(settingsView, settingsViewModel);
        FieldLink visibilitiesLink = Links.getField(typeLink, "visibilities");
        visibilitiesLink.set(settingsView, visibilities);
        return settingsView;
    }
}
