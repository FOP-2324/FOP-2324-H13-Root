package h13.test;

import h13.Package;
import h13.noise.DelegatePerlinNoise;
import h13.noise.PerlinNoise;
import h13.utils.Links;
import javafx.geometry.Point2D;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static h13.utils.Links.convertParameters;
import static h13.utils.Links.getMethod;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Defines unit tests for task H2.2.
 *
 * @author Nhan Huynh
 */
@DisplayName("H2.2: Delegation")
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class H2_2_TutorTests {


    /**
     * The package containing the methods to test.
     */
    private static final Package PACKAGE = Package.NOISE;

    /**
     * The class containing the methods to test.
     */
    private static final Class<?> CLASS = DelegatePerlinNoise.class;

    /**
     * The type link to the class containing the methods to test.
     */
    private TypeLink type;

    /**
     * Sets up the needed components for the tests.
     */
    @BeforeAll
    public void globalSetup() {
        type = Links.getType(PACKAGE, CLASS);
    }

    /**
     * Tests if all methods are delegated correctly.
     *
     * @throws Exception if an error occurs during a method invocation
     */
    @DisplayName("09 | Die Delegation der Implementierung wird korrekt an die PerlinNoise-Instanz weitergeleitet.")
    @Test
    public void testDelegate() throws Exception {
        InvokedPerlinNoise underlying = new InvokedPerlinNoise();
        DelegatePerlinNoise noise = new DelegatePerlinNoise(underlying) {

            @Override
            public double compute(double x, double y) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double compute(int x, int y) {
                throw new UnsupportedOperationException();
            }

        };

        Map<String, Pair<MethodLink, Object[]>> methods = Map.of(
            "getWidth()", new Pair<>(
                getMethod(type, "getWidth", Matcher.of(m -> m.typeList().isEmpty())),
                new Object[0]
            ),
            "getHeight()", new Pair<>(
                getMethod(type, "getHeight", Matcher.of(m -> m.typeList().isEmpty())),
                new Object[0]
            ),
            "getFrequency()", new Pair<>(
                getMethod(type, "getFrequency", Matcher.of(m -> m.typeList().isEmpty())),
                new Object[0]
            ),
            "setFrequency(double)", new Pair<>(
                getMethod(type, "setFrequency", Matcher.of(m -> m.typeList().equals(convertParameters(double.class)))),
                new Object[]{0}
            ),
            "getSeed()", new Pair<>(
                getMethod(type, "getSeed", Matcher.of(m -> m.typeList().isEmpty())),
                new Object[0]
            ),
            "getGradients()", new Pair<>(
                getMethod(type, "getGradients", Matcher.of(m -> m.typeList().isEmpty())),
                new Object[0]
            ),
            "getGradient(int,int)", new Pair<>(
                getMethod(type, "getGradient", Matcher.of(m -> m.typeList().equals(convertParameters(int.class, int.class)))),
                new Object[]{0, 0}
            ),
            "fade(double)", new Pair<>(
                getMethod(type, "fade", Matcher.of(m -> m.typeList().equals(convertParameters(double.class)))),
                new Object[]{0}
            ),
            "interpolate(double,double,double)", new Pair<>(
                getMethod(type, "interpolate",
                    Matcher.of(m -> m.typeList().equals(convertParameters(double.class, double.class, double.class)))),
                new Object[]{0, 0, 0}
            )
        );

        for (Map.Entry<String, Pair<MethodLink, Object[]>> entry : methods.entrySet()) {
            String name = entry.getKey();
            MethodLink method = entry.getValue().getKey();
            Object[] args = entry.getValue().getValue();
            underlying.reset();
            method.invoke(noise, args);
            assertTrue(underlying.invoked.get(name), "The underlying method %s was not called.".formatted(name));
        }
    }

    /**
     * A {@link PerlinNoise} implementation that tracks which methods were invoked.
     */
    private static class InvokedPerlinNoise implements PerlinNoise {

        private final Map<String, Boolean> invoked = new HashMap<>();

        @Override
        public int getWidth() {
            invoked.putIfAbsent("getWidth()", true);
            return 0;
        }

        @Override
        public int getHeight() {
            invoked.putIfAbsent("getHeight()", true);
            return 0;
        }

        @Override
        public double compute(int x, int y) {
            invoked.putIfAbsent("compute(int,int)", true);
            return 0;
        }

        @Override
        public Random getSeed() {
            invoked.putIfAbsent("getSeed()", true);
            return null;
        }

        @Override
        public Point2D[] getGradients() {
            invoked.putIfAbsent("getGradients()", true);
            return new Point2D[0];
        }

        @Override
        public Point2D getGradient(int x, int y) {
            invoked.putIfAbsent("getGradient(int,int)", true);
            return null;
        }

        @Override
        public double getFrequency() {
            invoked.putIfAbsent("getFrequency()", true);
            return 0;
        }

        @Override
        public void setFrequency(double frequency) {
            invoked.putIfAbsent("setFrequency(double)", true);
        }

        @Override
        public double fade(double t) {
            invoked.putIfAbsent("fade(double)", true);
            return 0;
        }

        @Override
        public double interpolate(double y1, double y2, double alpha) {
            invoked.putIfAbsent("interpolate(double,double,double)", true);
            return 0;
        }

        @Override
        public double compute(double x, double y) {
            invoked.putIfAbsent("compute(double,double)", true);
            return 0;
        }

        /**
         * Resets the tracking of which methods were invoked.
         */
        public void reset() {
            invoked.clear();
        }

    }

}
