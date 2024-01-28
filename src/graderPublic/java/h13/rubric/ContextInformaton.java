package h13.rubric;

import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.Property;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Collectors;

public class ContextInformaton implements Context.Builder<ContextInformaton> {

    private final Context.Builder<?> delegate;

    private final Map<String, String> descriptions;

    private final Map<String, Boolean> visited;
    private final Method method;

    public ContextInformaton(Context.Builder<?> delegate, Map<String, String> descriptions, Method method) {
        this.delegate = delegate;
        this.descriptions = descriptions;
        this.visited = descriptions.keySet().stream().collect(Collectors.toMap(key -> key, key -> false));
        this.method = method;
    }

    @Override
    public Context build() {
        Context.Builder<?> information = Assertions2.contextBuilder();
        for (Map.Entry<String, String> entry : descriptions.entrySet()) {
            if (visited.get(entry.getKey())) {
                information.add(entry.getKey(), entry.getValue());
            }
        }
        information.add("Resource data path", method.getAnnotation(JsonParameterSetTest.class).value());

        Context actual = delegate.build();

        Context.Builder<?> testCase = Assertions2.contextBuilder();
        testCase.add(actual.properties().toArray(Property[]::new));

        return Assertions2.contextBuilder().subject(actual.subject())
            .add("Test Information", information.build())
            .add("Test case", testCase.build())
            .build();
    }

    @Override
    public ContextInformaton add(String key, Object value) {
        delegate.add(key, value);
        if (visited.containsKey(key)) {
            visited.put(key, true);
        }
        return this;
    }

    @Override
    public ContextInformaton add(Property... property) {
        delegate.add(property);
        return this;
    }

    @Override
    public ContextInformaton add(Context... context) {
        delegate.add(context);
        return this;
    }

    @Override
    public ContextInformaton subject(Object subject) {
        delegate.subject(subject);
        return this;
    }
}
