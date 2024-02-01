package h13.rubric;

import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class TutorAssertions {

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

    public static void assertContains(Collection<Double> expected, Collection<Double> actual, double epsilon, Context context) {
        List<Double> expectedCopy = new ArrayList<>(expected);
        List<Double> actualCopy = new ArrayList<>(actual);
        Iterator<Double> expectedIterator = expectedCopy.iterator();
        while (expectedIterator.hasNext()) {
            Double expectedValue = expectedIterator.next();
            Iterator<Double> actualIterator = actualCopy.iterator();
            while (actualIterator.hasNext()) {
                Double actualValue = actualIterator.next();
                if (Math.abs(expectedValue - actualValue) < epsilon) {
                    expectedIterator.remove();
                    actualIterator.remove();
                    break;
                }
            }
        }
        Assertions2.assertTrue(expectedCopy.isEmpty(), context,
            result -> "The expected values %s are not contained in the actual values %s"
                .formatted(expected, actual));
    }

    public static void assertContains(List<Double> expected, List<Double> actual, Context context) {
        assertContains(expected, actual, EPSILON, context);
    }
}
