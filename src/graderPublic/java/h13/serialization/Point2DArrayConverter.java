package h13.serialization;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import java.util.stream.Stream;

public class Point2DArrayConverter implements ArgumentConverter {

    @Override
    public Point2D[] convert(Object source, ParameterContext context) throws ArgumentConversionException {
        if (!(source instanceof ArrayNode node)) {
            throw new ArgumentConversionException("Input type is not a JSON array");
        }
        Stream.Builder<JsonNode> streamBuilder = Stream.builder();
        node.forEach(streamBuilder::add);
        return streamBuilder.build()
            .map(n -> new Point2D(n.get("x").asDouble(), n.get("y").asDouble()))
            .toArray(Point2D[]::new);
    }

}
