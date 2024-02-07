package h13.ui.layout;

import h13.noise.PerlinNoise;
import h13.rubric.TutorPerlinNoise;
import h13.util.Links;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Map;
import java.util.function.DoubleFunction;

@DisplayName("H5.1 | P-I-C")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestForSubmission
public class H5_1_TestsPublic extends H5_Tests {

    private static final DoubleFunction<Color> COLOR_MAPPER = value -> ((int) value) == 1 ? Color.RED : Color.AZURE;
    private static final int WIDTH = 100;

    private static final int HEIGHT = 100;

    private TutorAlgorithmViewModel algorithmViewModel;

    @BeforeAll
    public void globalSetup() {
        algorithmViewModel = new TutorAlgorithmViewModel(COLOR_MAPPER) {
            @Override
            protected @Nullable PerlinNoise getAlgorithm() {
                return null;
            }
        };
    }

    @Override
    public TypeLink getTypeLink() {
        return BasicTypeLink.of(AlgorithmViewModel.class);
    }

    @DisplayName("Die Methode createImage(PerlinNoise, int, int, int, int) erstellt ein korrektes Bild für einen "
        + "gegebenen Perlin Noise Algorithmus.")
    @Order(23)
    @Test
    public void testCreateImage() throws Throwable {
        int x = 0;
        int y = 0;
        MethodLink methodLink = Links.getMethod(getTypeLink(), "createImage");
        TutorPerlinNoise noise = new TutorPerlinNoise();
        Context context = contextBuilder(methodLink, null)
            .add("Perlin Noise", "compute(x, y) = x * y % 2 == 0 ? 1 : -1")
            .add("Color Mapper", "value -> ((int) value) == 1 ? Color.RED : Color.AZURE")
            .add("x", x)
            .add("y", y)
            .add("width", WIDTH)
            .add("height", HEIGHT)
            .build();
        Image k = new WritableImage(WIDTH, HEIGHT);

        Image image = algorithmViewModel.createImage(noise, x, y, WIDTH, HEIGHT);
        PixelReader reader = image.getPixelReader();
        assertImage(context, reader);
    }

    private void assertImage(Context context, PixelReader reader) {
        for (int xi = 0; xi < WIDTH; xi++) {
            for (int yi = 0; yi < HEIGHT; yi++) {
                Color actual = reader.getColor(xi, yi);
                Color expected = COLOR_MAPPER.apply(xi * yi % 2 == 0 ? 1 : -1);
                int x1 = xi;
                int y1 = yi;
                Assertions2.assertEquals(expected, actual, context,
                    result -> "The color at position (%d, %d) is incorrect.".formatted(x1, y1));
            }
        }
    }

    private static abstract class TutorAlgorithmViewModel extends AlgorithmViewModel {

        public TutorAlgorithmViewModel(
            Map<String, BooleanProperty> options,
            Map<String, Property<Number>> parameters,
            DoubleFunction<Color> colorMapper) {
            super(options, parameters, colorMapper);
        }

        public TutorAlgorithmViewModel(DoubleFunction<Color> colorMapper) {
            super(Map.of(), Map.of(), colorMapper);
        }

        @Override
        public Image createImage(PerlinNoise algorithm, int x, int y, int w, int h) {
            return super.createImage(algorithm, x, y, w, h);
        }

        @Override
        public @Nullable PerlinNoise getLastAlgorithm() {
            return super.getLastAlgorithm();
        }
    }
}
