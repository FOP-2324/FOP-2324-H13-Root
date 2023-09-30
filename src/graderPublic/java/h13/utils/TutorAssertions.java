package h13.utils;

import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.PreCommentSupplier;
import org.tudalgo.algoutils.tutor.general.assertions.ResultOfObject;
import org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedObject;

public final class TutorAssertions {

    public static void assertWithin(double actual, double min, double max, Context context) {
        if (actual < min || actual > max) {
            Assertions2.fail(context, result -> "Expected value to be within [%s, %s] but was %s"
                .formatted(min, max, actual));
        }
    }

    public static void assertEquals(
        double expected,
        double actual,
        double delta,
        Context context,
        PreCommentSupplier<? super ResultOfObject<Double>> preCommentSupplier) {
        Assertions2.<Double>testOfObjectBuilder()
            .expected(ExpectedObject.of(expected, other -> Math.abs(expected - other) <= delta))
            .build()
            .run(actual)
            .check(context, preCommentSupplier).object();
    }
    
}
