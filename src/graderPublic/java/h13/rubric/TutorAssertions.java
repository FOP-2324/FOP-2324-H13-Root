package h13.rubric;

import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

public class TutorAssertions {

    private static final double EPSILON = 1e-6;

    private TutorAssertions() {

    }

    public static void assertEquals(double expected, double actual, double epsilon, Context context) {
        Assertions2.assertFalse(Math.abs(expected - actual) > EPSILON, context,
            result -> "The expected value %s is not within range [%s, %s], got %s"
                .formatted(expected, expected - epsilon, expected + epsilon, actual));
    }

    public static void assertEquals(double expected, double actual, Context context) {
        assertEquals(expected, actual, EPSILON, context);
    }
}
