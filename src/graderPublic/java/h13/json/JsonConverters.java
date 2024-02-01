package h13.json;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.geometry.Point2D;

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

    public static Point2D toGradient(JsonNode node) {
        if (!node.has("x") || !node.get("x").isDouble()) {
            throw new IllegalArgumentException("Node %s does not have a field x of type double".formatted(node));
        }
        if (!node.has("y") || !node.get("y").isDouble()) {
            throw new IllegalArgumentException("Node %s does not have a field y of type double".formatted(node));
        }
        return new Point2D(node.get("x").asDouble(), node.get("y").asDouble());
    }

    public static Point2D[] toGradients(JsonNode node) {
        if (!node.isArray()) {
            throw new IllegalArgumentException("Node %s is not an array".formatted(node.getNodeType()));
        }
        return toList(node, JsonConverters::toGradient).toArray(Point2D[]::new);
    }

    public static int[] toPermutation(JsonNode node) {
        if (!node.isArray()) {
            throw new IllegalArgumentException("Node %s is not an array".formatted(node.getNodeType()));
        }
        return toList(node, JsonNode::asInt).stream().mapToInt(i -> i).toArray();
    }
}
