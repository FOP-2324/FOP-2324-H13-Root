package h13.utils;

import h13.Package;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.match.MatcherFactories;
import org.tudalgo.algoutils.tutor.general.reflections.BasicPackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.ConstructorLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Arrays;
import java.util.List;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions3.assertConstructorExists;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions3.assertFieldExists;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions3.assertMethodExists;

public final class Links {

    private static final MatcherFactories.StringMatcherFactory MATCHER_FACTORY = BasicStringMatchers::identical;

    private Links() {

    }

    public static TypeLink getType(Package p, Class<?> clazz) {
        return Assertions3.assertTypeExists(
            BasicPackageLink.of(p.toString()),
            MATCHER_FACTORY.matcher(clazz.getSimpleName())
        );
    }

    @SafeVarargs
    public static ConstructorLink getConstructor(TypeLink type, Matcher<ConstructorLink>... matchers) {
        return assertConstructorExists(type, Arrays.stream(matchers).reduce(Matcher.always(), Matcher::and));
    }

    @SafeVarargs
    public static FieldLink getField(TypeLink type, String fieldName, Matcher<FieldLink>... matchers) {
        return assertFieldExists(
            type,
            Arrays.stream(matchers).reduce(MATCHER_FACTORY.matcher(fieldName), Matcher::and)
        );
    }

    @SafeVarargs
    public static MethodLink getMethod(TypeLink type, String methodName, Matcher<MethodLink>... matchers) {
        return assertMethodExists(
            type,
            Arrays.stream(matchers).reduce(MATCHER_FACTORY.matcher(methodName), Matcher::and)
        );
    }

    public static List<TypeLink> convertParameters(Class<?>... classes) {
        return Arrays.stream(classes).<TypeLink>map(BasicTypeLink::of).toList();
    }

}
