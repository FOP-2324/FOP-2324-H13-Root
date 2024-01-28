package h13.json;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A collection of JSON converters for this assignment.
 *
 * @author Nhan Huynh
 */
public class JsonConverters extends org.tudalgo.algoutils.tutor.general.json.JsonConverters {

    /**
     * This class cannot be instantiated.
     */
    private JsonConverters() {

    }

    public static int toInt(JsonNode node) {
        if (!node.isInt()) {
            throw new IllegalArgumentException("Node %s is not an integer".formatted(node.getNodeType()));
        }
        return node.asInt();
    }
}
