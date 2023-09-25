package h13.noise;

import h13.Package;
import javafx.geometry.Point2D;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestInstance;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static h13.utils.Links.method;
import static h13.utils.Links.parameters;
import static h13.utils.Links.type;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DelegatePerlinNoiseTest {

    private static final Package PACKAGE = Package.NOISE;
    private static final Class<?> CLASS = DelegatePerlinNoise.class;

    private TypeLink type;

    @BeforeAll
    public void globalSetup() {
        type = type(PACKAGE, CLASS);
    }

    @DisplayName("Die Delegation der Implementierung wird korrekt an die PerlinNoise-Instanz weitergeleitet.")
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
                method(type, "getWidth", Matcher.of(m -> m.typeList().isEmpty())),
                new Object[0]
            ),
            "getHeight()", new Pair<>(
                method(type, "getHeight", Matcher.of(m -> m.typeList().isEmpty())),
                new Object[0]
            ),
            "getFrequency()", new Pair<>(
                method(type, "getFrequency", Matcher.of(m -> m.typeList().isEmpty())),
                new Object[0]
            ),
            "setFrequency(double)", new Pair<>(
                method(type, "setFrequency", Matcher.of(m -> m.typeList().equals(parameters(double.class)))),
                new Object[]{0}
            ),
            "getSeed()", new Pair<>(
                method(type, "getSeed", Matcher.of(m -> m.typeList().isEmpty())),
                new Object[0]
            ),
            "getGradients()", new Pair<>(
                method(type, "getGradients", Matcher.of(m -> m.typeList().isEmpty())),
                new Object[0]
            ),
            "getGradient(int,int)", new Pair<>(
                method(type, "getGradient", Matcher.of(m -> m.typeList().equals(parameters(int.class, int.class)))),
                new Object[]{0, 0}
            ),
            "fade(double)", new Pair<>(
                method(type, "fade", Matcher.of(m -> m.typeList().equals(parameters(double.class)))),
                new Object[]{0}
            ),
            "interpolate(double,double,double)", new Pair<>(
                method(type, "interpolate",
                    Matcher.of(m -> m.typeList().equals(parameters(double.class, double.class, double.class)))),
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

        public void reset() {
            invoked.clear();
        }
    }
}
