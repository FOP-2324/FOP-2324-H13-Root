package h13.rubric.h1;

import com.fasterxml.jackson.databind.JsonNode;
import h13.json.JsonConverters;
import h13.rubric.ContextInformaton;
import h13.rubric.H13_Tests;
import h13.util.Links;
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

    protected static final Map<String, String> INFORMATION = Map.of(
        "width", "The width of the noise domain",
        "height", "The height of the noise domain",
        "n", "The number of gradients to generate",
        "Package", "The package of the tested method",
        "Type", "The class of the tested method"
    );

    @Override
    public PackageLink getPackageLink() {
        return PACKAGE_LINK;
    }

    public abstract TypeLink getTypeLink();

    protected ContextInformaton contextBuilder(Link subject, String methodName) {
        return contextBuilder(subject, methodName, INFORMATION)
            .add("Package", getPackageLink().name())
            .add("Type", getTypeLink().reflection().getName());
    }
}
