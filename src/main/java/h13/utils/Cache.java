package h13.utils;

import java.util.function.Function;

public interface Cache<K, V> {
    V get(K key);

    V put(K key, V value);

    V computeIfAbsent(K key, Function<? super K, ? extends V> mapper);

    void clear();

    int size();

    int capacity();

}
