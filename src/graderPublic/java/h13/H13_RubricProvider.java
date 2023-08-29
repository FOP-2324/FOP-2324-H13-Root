package h13;

import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;

public class H13_RubricProvider implements RubricProvider {

    public static final Rubric RUBRIC = Rubric.builder()
        .title("H13")
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }
}
