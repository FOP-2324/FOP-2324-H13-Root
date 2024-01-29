package h13.rubric;

import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.reflections.BasicPackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.Link;
import org.tudalgo.algoutils.tutor.general.reflections.PackageLink;

import java.util.Arrays;
import java.util.Map;

public abstract class H13_Tests {

    protected static final PackageLink PACKAGE_LINK = BasicPackageLink.of("h13");

    protected static final String CONVERTERS_FIELD_NAME = "CONVERTERS";

    protected abstract PackageLink getPackageLink();

    protected abstract Map<String, String> getContextInformation();

    protected ContextInformaton contextBuilder(Link subject, String methodName) {
        return new ContextInformaton(
            Assertions2.contextBuilder().subject(subject),
            getContextInformation(),
            Arrays.stream(getClass().getDeclaredMethods())
                .filter(method -> method.getName().equals(methodName))
                .findFirst()
                .orElseThrow()
        );
    }
}
