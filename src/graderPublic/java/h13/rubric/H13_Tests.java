package h13.rubric;

import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.reflections.BasicPackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.Link;
import org.tudalgo.algoutils.tutor.general.reflections.PackageLink;

import java.util.Arrays;
import java.util.Map;

public interface H13_Tests {

    PackageLink BASE_PACKAGE_LINK = BasicPackageLink.of("h13");
    String CONVERTERS_FIELD_NAME = "CONVERTERS";

    PackageLink getPackageLink();

    Map<String, String> getContextInformation();

    default ContextInformaton contextBuilder(Link subject, String methodName) {
        return new ContextInformaton(
            Assertions2.contextBuilder().subject(subject),
            getContextInformation(),
            methodName == null ? null : Arrays.stream(getClass().getDeclaredMethods())
                .filter(method -> method.getName().equals(methodName))
                .findFirst()
                .orElseThrow()
        );
    }
}
