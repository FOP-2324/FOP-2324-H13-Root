package h13.utils;

import h13.Package;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.match.MatcherFactories;
import org.tudalgo.algoutils.tutor.general.reflections.BasicPackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Arrays;
import java.util.List;

public final class Links {

    private static final MatcherFactories.StringMatcherFactory MATCHER_FACTORY = BasicStringMatchers::identical;

    private Links() {

    }

    public static TypeLink type(Package p, Class<?> clazz) {
        return Assertions3.assertTypeExists(
            BasicPackageLink.of(p.toString()),
            MATCHER_FACTORY.matcher(clazz.getSimpleName())
        );
    }

    public static FieldLink field(TypeLink type, String fieldName) {
        return Assertions3.assertFieldExists(type, MATCHER_FACTORY.matcher(fieldName));
    }

    public static FieldLink field(Package p, Class<?> clazz, String fieldName) {
        return field(type(p, clazz), fieldName);
    }

    @SafeVarargs
    public static MethodLink method(TypeLink type, String methodName, Matcher<MethodLink>... matchers) {
        return Assertions3.assertMethodExists(
            type,
            Arrays.stream(matchers).reduce(MATCHER_FACTORY.matcher(methodName), Matcher::and)
        );
    }

    @SafeVarargs
    public static MethodLink method(Package p, Class<?> clazz, String methodName, Matcher<MethodLink>... matchers) {
        return method(type(p, clazz), methodName, matchers);
    }

    public static List<TypeLink> parameters(Class<?>... classes) {
        return Arrays.stream(classes).<TypeLink>map(BasicTypeLink::of).toList();
    }

}
