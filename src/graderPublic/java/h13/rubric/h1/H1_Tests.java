package h13.rubric.h1;

import com.fasterxml.jackson.databind.JsonNode;
import h13.json.JsonConverters;
import h13.rubric.H13_Tests;
import h13.util.Links;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.Link;
import org.tudalgo.algoutils.tutor.general.reflections.PackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Map;
import java.util.function.Function;

public abstract class H1_Tests extends H13_Tests {

    protected static final PackageLink PACKAGE_LINK = Links.getPackage(H13_Tests.PACKAGE_LINK, "noise");

    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "width", JsonConverters::toInt,
        "height", JsonConverters::toInt,
        "n", JsonConverters::toInt
    );

    @Override
    protected Context.Builder<?> contextBuilderTestDataStructure() {
        return Assertions2.contextBuilder()
            .add("width", "The width of the noise domain")
            .add("height", "The height of the noise domain")
            .add("n", "The number of gradients to generate");
    }

    @Override
    public PackageLink getPackageLink() {
        return PACKAGE_LINK;
    }

    public abstract TypeLink getTypeLink();

    @Override
    protected Context.Builder<?> contextBuilder(Link subject, String resourcePath) {
        return super.contextBuilder(subject, resourcePath).add("Type", getTypeLink().reflection().getName());
    }
}
