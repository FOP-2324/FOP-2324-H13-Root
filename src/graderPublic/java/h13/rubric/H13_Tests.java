package h13.rubric;

import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.BasicPackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.Link;
import org.tudalgo.algoutils.tutor.general.reflections.PackageLink;

public abstract class H13_Tests {

    protected static final PackageLink PACKAGE_LINK = BasicPackageLink.of("h13");

    protected abstract Context.Builder<?> contextBuilderTestDataStructure();

    protected abstract PackageLink getPackageLink();

    protected Context.Builder<?> contextBuilder(Link subject, String resourcePath) {
        return Assertions2.contextBuilder().subject(subject)
            .add("Test data structure", contextBuilderTestDataStructure().add("Test resource", resourcePath).build())
            .add("Package", getPackageLink());
    }
}
