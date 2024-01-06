package h13;

import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;

/**
 * Defines the rubric for task H13.
 *
 * @author Nhan Huynh
 */
public class H13_RubricProvider implements RubricProvider {

    /**
     * The rubric for task H13.
     */
    public static final Rubric RUBRIC = Rubric.builder()
        .title("H13")
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }
}
