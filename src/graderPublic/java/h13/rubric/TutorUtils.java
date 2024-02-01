package h13.rubric;

import javafx.geometry.Point2D;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class TutorUtils {

    private TutorUtils() {

    }

    public static String toString(Point2D[] gradients) {
        return Arrays.stream(gradients)
            .map(g -> "(%s, %s)".formatted(g.getX(), g.getY()))
            .collect(Collectors.joining(", "));
    }
}
