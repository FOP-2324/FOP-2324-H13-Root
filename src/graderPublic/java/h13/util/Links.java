package h13.util;

import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.match.MatcherFactories;
import org.tudalgo.algoutils.tutor.general.reflections.BasicPackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.PackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Arrays;

public class Links {

    private static final MatcherFactories.StringMatcherFactory STRING_MATCHER_FACTORY = BasicStringMatchers::identical;

    private Links() {
    }

    public static PackageLink getPackage(PackageLink base, String... parts) {
        return Arrays.stream(parts)
            .reduce(base, (acc, subpackage) -> BasicPackageLink.of(acc.name() + "." + subpackage), (a, b) -> b);
    }

    public static TypeLink getType(PackageLink packageLink, Class<?> clazz) {
        return Assertions3.assertTypeExists(
            packageLink,
            STRING_MATCHER_FACTORY.matcher(clazz.getSimpleName())
        );
    }

    @SafeVarargs
    public static FieldLink getField(PackageLink packageLink, Class<?> clazz, String fieldName, Matcher<FieldLink>... matchers) {
        return getField(getType(packageLink, clazz), fieldName, matchers);
    }

    @SafeVarargs
    public static FieldLink getField(TypeLink type, String fieldName, Matcher<FieldLink>... matchers) {
        return Assertions3.assertFieldExists(
            type,
            Arrays.stream(matchers).reduce(STRING_MATCHER_FACTORY.matcher(fieldName), Matcher::and)
        );
    }

    @SafeVarargs
    public static MethodLink getMethod(PackageLink packageLink, Class<?> clazz, String methodName, Matcher<MethodLink>... matchers) {
        return getMethod(getType(packageLink, clazz), methodName, matchers);
    }

    @SafeVarargs
    public static MethodLink getMethod(TypeLink type, String methodName, Matcher<MethodLink>... matchers) {
        return Assertions3.assertMethodExists(
            type,
            Arrays.stream(matchers).reduce(STRING_MATCHER_FACTORY.matcher(methodName), Matcher::and)
        );
    }
}
