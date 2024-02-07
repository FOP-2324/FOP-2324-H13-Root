package h13.ui.layout;

import h13.noise.PerlinNoise;
import h13.util.Links;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.testfx.api.FxToolkit;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DisplayName("H5.2 | Zoom in, zoom out")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestForSubmission
public class H5_2_TestsPrivate extends H5_Tests {
    @BeforeEach
    public void setup() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
    }

    @Override
    public TypeLink getTypeLink() {
        return BasicTypeLink.of(AlgorithmView.class);
    }

    @DisplayName("Falls sich die Größe der Zeichenfläche verändert, wird nur die neue noch nicht gezeichnete Fläche "
            + "gezeichnet.")
    @Order(29)
    @Test
    public void testDraw() {
        List<List<Integer>> area = new ArrayList<>();
        TutorAlgorithmViewModel viewModel = new TutorAlgorithmViewModel() {

            @Override
            protected @Nullable PerlinNoise getAlgorithm() {
                return Mockito.mock(PerlinNoise.class);
            }

            @Override
            public void draw(@Nullable PerlinNoise algorithm, GraphicsContext context, int x, int y, int w, int h) {
                area.add(List.of(x, y, w, h));
            }
        };
        TutorSettingsView settings = new TutorSettingsView();
        TutorVBox layout = (TutorVBox) settings.getView();
        TutorAlgorithmView algorithm = new TutorAlgorithmView(settings, (algorithms, parameters) -> viewModel, view -> {
        }) {
            @Override
            public void initializeButtons() {

            }
        };
        TutorBorderPane root = (TutorBorderPane) algorithm.getView();
        root.setWidth(1000);
        root.setHeight(1000);

        // Format: {width, height} of the canvas and the expected area data (x, y, w, h)
        List<Pair<Pair<Integer, Integer>, List<List<Integer>>>> data =
                Stream.<Pair<Pair<Integer, Integer>, List<List<Integer>>>>of(
                                new Pair<>(new Pair<>(100, 100), List.of()),
                                new Pair<>(new Pair<>(1001, 100), List.of(List.of(1000, 0, 1001, 1000))),
                                new Pair<>(new Pair<>(5112, 100), List.of(List.of(1000, 0, 5112, 1000))),
                                new Pair<>(new Pair<>(100, 1001), List.of(List.of(0, 1000, 100, 1001))),
                                new Pair<>(new Pair<>(100, 5112), List.of(List.of(0, 1000, 100, 5112))),
                                new Pair<>(new Pair<>(3213, 5112), List.of(List.of(1000, 0, 3213, 1000),
                                        List.of(0, 1000, 3213, 5112)))
                        )
                        .map(pair -> new Pair<>(pair.getKey(), pair.getValue().stream()
                                .<List<Integer>>map(ArrayList::new).collect(Collectors.toList())
                        ))
                        .toList();
        Comparator<List<Integer>> comparator = Comparator.comparingInt((List<Integer> list) -> list.get(0))
                .thenComparingInt(list -> list.get(1))
                .thenComparingInt(list -> list.get(2))
                .thenComparingInt(list -> list.get(3));
        data.forEach(pair -> {
            root.setWidth(1000);
            root.setHeight(1000);
            area.clear();
            root.setWidth(pair.getKey().getKey());
            root.setHeight(pair.getKey().getValue());

            Context context = contextBuilder(Links.getMethod(getTypeLink(), "initializeButtons"), null)
                    .add("Initial width", root.getWidth())
                    .add("Initial height", root.getHeight())
                    .add("New width", pair.getKey().getKey())
                    .add("New height", pair.getKey().getValue())
                    .build();
            area.sort(comparator);
            pair.getValue().sort(comparator);
            Assertions2.assertEquals(pair.getValue(), area, context, result -> "Incorrect drawing area");
        });
    }

    private static class TutorBorderPane extends BorderPane {

        @Override
        public void setWidth(double v) {
            super.setWidth(v);
        }

        @Override
        public void setHeight(double v) {
            super.setHeight(v);
        }
    }

    private static class TutorVBox extends VBox {

        @Override
        public void setWidth(double v) {
            super.setWidth(v);
        }

        @Override
        public void setHeight(double v) {
            super.setHeight(v);
        }
    }

    private static class TutorSettingsView extends SettingsView {

        public TutorSettingsView() {
            super(
                    new TutorVBox(),
                    new Pair<>("Algorithms", new ChooserView(3, view -> {
                    })),
                    new Pair<>("Parameters", new ParameterView(view -> {
                    })),
                    Map.of(),
                    view -> {
                    }
            );
        }
    }

    private static class TutorAlgorithmViewModel extends AlgorithmViewModel {

        public static final PerlinNoise NOISE = Mockito.mock(PerlinNoise.class);

        public TutorAlgorithmViewModel() {
            super(Map.of(), Map.of(), value -> Color.WHITE);
        }

        @Override
        protected @Nullable PerlinNoise getAlgorithm() {
            return NOISE;
        }
    }

    private static class TutorAlgorithmView extends AlgorithmView {

        public TutorAlgorithmView(SettingsView settings, BiFunction<Map<String, BooleanProperty>, Map<String, Property<Number>>, AlgorithmViewModel> factory, ViewConfiguration<AlgorithmView> configuration) {
            super(new TutorBorderPane(), settings, factory, configuration);
        }

        public TutorAlgorithmView(SettingsView settings, ViewConfiguration<AlgorithmView> configuration) {
            super(new TutorBorderPane(), settings, (algorithms, parameters) -> new TutorAlgorithmViewModel(), configuration);
        }

        public TutorAlgorithmView(ViewConfiguration<AlgorithmView> configuration) {
            this(new TutorSettingsView(), configuration);
        }


        @Override
        public void initializeButtons() {
            super.initializeButtons();
        }

        @Override
        protected void initializeSize() {
            super.initializeSize();
        }
    }


}
