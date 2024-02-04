package h13.rubric;

import org.sourcegrade.jagr.api.rubric.Rubric;

/**
 * The base class for rubric providers for this assignment.
 *
 * <p>A subclass specifies the type of rubric (e.g., Public Tests or Private Tests).
 *
 * @author Nhan Huynh
 */
public abstract class RubricProvider implements org.sourcegrade.jagr.api.rubric.RubricProvider {

    /**
     * The file containing the rubric for this assignment.
     */
    private static final String RUBRIC_FILE = "rubric.json";
    /**
     * The prefix for the title of this rubric.
     */
    private static final String RUBRIC_TITLE_PREFIX = "H13 | Codecraft";
    /**
     * The rubric for this assignment.
     */
    private final Rubric rubric;
    /**
     * The type of this rubric (e.g., Public Tests or Private Tests).
     */

    private final String type;

    /**
     * Creates a new rubric provider.
     *
     * @param type the type of this rubric (e.g., Public Tests or Private Tests)
     * @throws IllegalStateException if the rubric file cannot be read
     */
    public RubricProvider(String type) {
        this.type = type;
        this.rubric = Rubrics.read(RUBRIC_TITLE_PREFIX + " - " + type, RUBRIC_FILE);
    }

    @Override
    public Rubric getRubric() {
        return rubric;
    }
}
