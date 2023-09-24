package h13.noise;

import h13.Package;
import h13.utils.TutorAssertions;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestInstance;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Random;

import static h13.utils.Links.type;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertSame;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DelegatePerlinNoiseTest {

    private static final Package PACKAGE = Package.NOISE;
    private static final Class<?> CLASS = DelegatePerlinNoise.class;
    private static final double EPSILON = 1e-6;
    private static final int WIDTH = 10;
    private static final int HEIGHT = 100;

    private static final double FREQUENCY = 1.5;
    private static final Random SEED = new Random(0);

    private static final double INTERPOLATE = Double.MAX_VALUE;

    private static final double FADE = Double.MIN_VALUE;

    private static final Point2D[] GRADIENTS = new Point2D[0];

    private static final Point2D GRADIENT = new Point2D(0, 0);

    private TypeLink type;

    @BeforeAll
    public void globalSetup() {
        type = type(PACKAGE, CLASS);
    }

    @DisplayName("Die Delegation der Implementierung wird korrekt an die PerlinNoise-Instanz weitergeleitet.")
    @Test
    public void testDelegate() {
        DelegatePerlinNoise noise = new MockDelegatePerlinNoise();
        Context.Builder<?> context = contextBuilder().subject(type);
        double actual = noise.getFrequency();
        TutorAssertions.assertEquals(FREQUENCY, actual, EPSILON,
            context.add("Frequency", FREQUENCY).add("Actual frequency", actual).build(),
            result -> "The frequency was not correctly delegated to the underlying Perlin noise object.");
        noise.setFrequency(2.0);

        actual = noise.getFrequency();
        TutorAssertions.assertEquals(2.0, actual, EPSILON,
            context.add("Frequency", 2.0).add("Actual frequency", actual).build(),
            result -> "The frequency was not correctly delegated to the underlying Perlin noise object.");

        double actualFade = noise.fade(0.0);
        TutorAssertions.assertEquals(FADE, actualFade, EPSILON,
            context.add("Fade", FADE).add("Actual fade", actualFade).build(),
            result -> "The fade was not correctly delegated to the underlying Perlin noise object.");

        double actualInterpolate = noise.interpolate(0.0, 0.0, 0.0);

        TutorAssertions.assertEquals(INTERPOLATE, actualInterpolate, EPSILON,
            context.add("Interpolate", INTERPOLATE).add("Actual interpolate", actualInterpolate).build(),
            result -> "The interpolate was not correctly delegated to the underlying Perlin noise object.");

        Random actualSeed = noise.getSeed();
        assertSame(SEED, actualSeed,
            context.add("Seed", SEED).add("Actual seed", actualSeed).build(),
            result -> "The seed was not correctly delegated to the underlying Perlin noise object.");

        Point2D[] actualGradients = noise.getGradients();
        assertSame(GRADIENTS, actualGradients,
            context.add("Gradients", GRADIENTS).add("Actual gradients", actualGradients).build(),
            result -> "The gradients were not correctly delegated to the underlying Perlin noise object.");

        Point2D actualGradient = noise.getGradient(0, 0);
        assertSame(GRADIENT, actualGradient,
            context.add("Gradient", GRADIENT).add("Actual gradient", actualGradient).build(),
            result -> "The gradient was not correctly delegated to the underlying Perlin noise object.");

        int actualWidth = noise.getWidth();
        assertEquals(WIDTH, actualWidth,
            context.add("Width", WIDTH).add("Actual width", actualWidth).build(),
            result -> "The width was not correctly delegated to the underlying Perlin noise object.");

        int actualHeight = noise.getHeight();
        assertEquals(HEIGHT, actualHeight,
            context.add("Height", HEIGHT).add("Actual height", actualHeight).build(),
            result -> "The height was not correctly delegated to the underlying Perlin noise object.");
    }


    private static class MockDelegatePerlinNoise extends DelegatePerlinNoise {
        public MockDelegatePerlinNoise() {
            super(new PerlinNoise() {

                private double frequency = 1.5;

                @Override
                public Random getSeed() {
                    return SEED;
                }

                @Override
                public Point2D[] getGradients() {
                    return GRADIENTS;
                }

                @Override
                public Point2D getGradient(int x, int y) {
                    return GRADIENT;
                }

                @Override
                public double getFrequency() {
                    return frequency;
                }

                @Override
                public void setFrequency(double frequency) {
                    this.frequency = frequency;
                }

                @Override
                public double fade(double t) {
                    return FADE;
                }

                @Override
                public double interpolate(double y1, double y2, double alpha) {
                    return INTERPOLATE;
                }

                @Override
                public double compute(double x, double y) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public int getWidth() {
                    return WIDTH;
                }

                @Override
                public int getHeight() {
                    return HEIGHT;
                }

                @Override
                public double compute(int x, int y) {
                    throw new UnsupportedOperationException();
                }
            });
        }


        @Override
        public double compute(int x, int y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double compute(double x, double y) {
            throw new UnsupportedOperationException();
        }
    }
}
