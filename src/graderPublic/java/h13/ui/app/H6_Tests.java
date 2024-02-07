package h13.ui.app;

import h13.noise.DelegatePerlinNoise;
import h13.noise.FractalPerlinNoise;
import h13.noise.ImprovedPerlinNoise;
import h13.noise.PerlinNoise;
import h13.noise.SimplePerlinNoise;
import h13.rubric.ContextInformaton;
import h13.rubric.H13_Tests;
import h13.rubric.TutorAssertions;
import h13.ui.layout.AlgorithmViewModel;
import h13.util.Links;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.testfx.api.FxToolkit;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.PackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@DisplayName("H6 | App")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestForSubmission
public class H6_Tests implements H13_Tests {

    protected static final PackageLink PACKAGE_LINK = Links.getPackage(BASE_PACKAGE_LINK, "ui", "app");

    private Map<String, BooleanProperty> options;

    private Map<String, Property<Number>> parameters;

    private PerlinNoiseViewModel viewModel;

    @BeforeEach
    public void setup() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        options = Arrays.stream(Algorithm.values())
                .map(algorithm -> Map.entry(
                        algorithm.toString(),
                        new SimpleBooleanProperty(algorithm, algorithm.toString(),
                                false))
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        parameters = Arrays.stream(Parameter.values())
                .map(parameter -> Map.entry(
                        parameter.toString(),
                        new SimpleObjectProperty<>(parameter.defaultValue()))
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        viewModel = new PerlinNoiseViewModel(options, parameters, value -> Color.WHITE, 10);
    }

    @Override
    public PackageLink getPackageLink() {
        return PACKAGE_LINK;
    }

    public TypeLink getTypeLink() {
        return BasicTypeLink.of(PerlinNoiseViewModel.class);
    }

    @Override
    public Map<String, String> getContextInformation() {
        return Map.of();
    }

    public ContextInformaton contextBuilder() {
        MethodLink methodLink = Links.getMethod(getTypeLink(), "getAlgorithm");
        return H13_Tests.super.contextBuilder(methodLink, null);
    }

    @DisplayName("Falls der gleiche Algorithmus mit den gleichen Konfigurationen ausgewählt wird, wird kein neues "
            + "Bild gezeichnet.")
    @Order(30)
    @Test
    public void testSameAlgorithm() {
        TypeLink baseTypeLink = BasicTypeLink.of(AlgorithmViewModel.class);
        FieldLink lastAlgorithmLink = Links.getField(baseTypeLink, "lastAlgorithm");
        options.values().forEach(option -> option.set(false));
        options.get(Algorithm.SIMPLE.toString()).set(true);
        PerlinNoise algorithm = viewModel.getAlgorithm();
        Context context = contextBuilder()
                .add("Selection", "Simple")
                .add("Frequency", SimplePerlinNoise.DEFAULT_FREQUENCY)
                .build();
        Assertions2.assertNotNull(algorithm, context, result -> "The algorithm is null.");
        Assertions2.assertEquals(SimplePerlinNoise.class, lastAlgorithmLink.get(viewModel).getClass(), context,
                result -> "Incorrect last algorithm stored.");
        Assertions2.assertEquals(algorithm, lastAlgorithmLink.get(viewModel), context,
                result -> "Incorrect last algorithm stored.");
        // Same algorithm
        Assertions2.assertNull(viewModel.getAlgorithm(), context, result -> "The algorithm is not the same.");
    }

    @DisplayName("Die Methode getAlgorithm() liefert einen korrekten Algorithmus für einfache Eingaben zurück.")
    @Order(31)
    @Test
    public void testSimple() {
        int seed = 42;
        double frequency = 0.1;
        double amplitude = 1.023;
        int octaves = 12;
        double lacunarity = 2.13120;
        double persistence = 0.5;
        assertSimple(Algorithm.SIMPLE, parameters -> {
                    parameters.get(Parameter.SEED.toString()).setValue(seed);
                    parameters.get(Parameter.FREQUENCY.toString()).setValue(frequency);
                },
                (algorithm, parameters) -> {
                    Context context = contextBuilder()
                            .add("Selection", "Simple")
                            .add("Seed", seed)
                            .add("Frequency", frequency)
                            .build();
                    Assertions2.assertEquals(SimplePerlinNoise.class, algorithm.getClass(), context,
                            result -> "Incorrect algorithm selection.");
                    TutorAssertions.assertEquals(frequency, algorithm.getFrequency(), context);
                }
        );
        assertSimple(Algorithm.IMPROVED, parameters -> {
                    parameters.get(Parameter.SEED.toString()).setValue(seed);
                    parameters.get(Parameter.FREQUENCY.toString()).setValue(frequency);
                },
                (algorithm, parameters) -> {
                    Context context = contextBuilder()
                            .add("Selection", "Simple")
                            .add("Seed", seed)
                            .add("Frequency", frequency)
                            .build();
                    Assertions2.assertNotNull(algorithm, context, result -> "The algorithm is null.");
                    Assertions2.assertEquals(ImprovedPerlinNoise.class, algorithm.getClass(), context,
                            result -> "Incorrect algorithm selection.");
                    TutorAssertions.assertEquals(frequency, algorithm.getFrequency(), context);
                }
        );

        assertSimple(Algorithm.FRACTAL, parameters -> {
                    parameters.get(Parameter.SEED.toString()).setValue(seed);
                    parameters.get(Parameter.FREQUENCY.toString()).setValue(frequency);
                    parameters.get(Parameter.AMPLITUDE.toString()).setValue(amplitude);
                    parameters.get(Parameter.OCTAVES.toString()).setValue(octaves);
                    parameters.get(Parameter.LACUNARITY.toString()).setValue(lacunarity);
                    parameters.get(Parameter.PERSISTENCE.toString()).setValue(persistence);
                },
                (algorithm, parameters) -> {
                    Context context = contextBuilder()
                            .add("Selection", "Simple")
                            .add("Seed", seed)
                            .add("Frequency", frequency)
                            .add("Amplitude", amplitude)
                            .add("Octaves", octaves)
                            .add("Lacunarity", lacunarity)
                            .add("Persistence", persistence)
                            .build();
                    Assertions2.assertNotNull(algorithm, context, result -> "The algorithm is null.");
                    Assertions2.assertEquals(FractalPerlinNoise.class, algorithm.getClass(), context,
                            result -> "Incorrect algorithm selection.");
                    FractalPerlinNoise fractalPerlinNoise = (FractalPerlinNoise) algorithm;
                    TutorAssertions.assertEquals(frequency, algorithm.getFrequency(), context);
                    TutorAssertions.assertEquals(amplitude, fractalPerlinNoise.getAmplitude(), context);
                    TutorAssertions.assertEquals(octaves, fractalPerlinNoise.getOctaves(), context);
                    TutorAssertions.assertEquals(lacunarity, fractalPerlinNoise.getLacunarity(), context);
                    TutorAssertions.assertEquals(persistence, fractalPerlinNoise.getPersistence(), context);
                }
        );

    }

    private void assertSimple(
            Algorithm selection,
            Consumer<Map<String, Property<Number>>> setter,
            BiConsumer<PerlinNoise, Map<String, Property<Number>>> assertions
    ) {
        options.values().forEach(option -> option.set(false));
        options.get(selection.toString()).set(true);
        parameters.forEach((s, property) -> property.setValue(Parameter.valueOf(s.toUpperCase()).defaultValue()));
        setter.accept(parameters);
        PerlinNoise algorithm = viewModel.getAlgorithm();
        assertions.accept(algorithm, parameters);
    }

    @DisplayName("Die Methode getAlgorithm() liefert einen korrekten Algorithmus für komplexe Eingaben zurück.")
    @Order(32)
    @Test
    public void testComplex() {
        options.values().forEach(option -> option.set(true));
        parameters.forEach((s, property) -> property.setValue(Parameter.valueOf(s.toUpperCase()).defaultValue()));
        int seed = 42;
        double frequency = 0.1;
        double amplitude = 1.023;
        int octaves = 12;
        double lacunarity = 2.13120;
        double persistence = 0.5;

        parameters.get(Parameter.SEED.toString()).setValue(seed);
        parameters.get(Parameter.FREQUENCY.toString()).setValue(frequency);
        parameters.get(Parameter.AMPLITUDE.toString()).setValue(amplitude);
        parameters.get(Parameter.OCTAVES.toString()).setValue(octaves);
        parameters.get(Parameter.LACUNARITY.toString()).setValue(lacunarity);
        parameters.get(Parameter.PERSISTENCE.toString()).setValue(persistence);

        Context context = contextBuilder()
                .add("Selection", "Simple, Improved, Fractal")
                .add("Seed", seed)
                .add("Frequency", frequency)
                .add("Amplitude", amplitude)
                .add("Octaves", octaves)
                .add("Lacunarity", lacunarity)
                .add("Persistence", persistence)
                .build();
        PerlinNoise algorithm = viewModel.getAlgorithm();
        Assertions2.assertNotNull(algorithm, context, result -> "The algorithm is null.");
        Assertions2.assertEquals(FractalPerlinNoise.class, algorithm.getClass(), context,
                result -> "Incorrect algorithm selection.");
        FractalPerlinNoise fractalPerlinNoise = (FractalPerlinNoise) algorithm;
        TypeLink delegateTypeLink = BasicTypeLink.of(DelegatePerlinNoise.class);
        FieldLink delegateLink = Links.getField(delegateTypeLink, "delegate");
        Assertions2.assertEquals(ImprovedPerlinNoise.class, delegateLink.get(fractalPerlinNoise).getClass(), context,
                result -> "The delegate algorithm is incorrect.");
        TutorAssertions.assertEquals(frequency, algorithm.getFrequency(), context);
        TutorAssertions.assertEquals(amplitude, fractalPerlinNoise.getAmplitude(), context);
        TutorAssertions.assertEquals(octaves, fractalPerlinNoise.getOctaves(), context);
        TutorAssertions.assertEquals(lacunarity, fractalPerlinNoise.getLacunarity(), context);
        TutorAssertions.assertEquals(persistence, fractalPerlinNoise.getPersistence(), context);
    }
}
