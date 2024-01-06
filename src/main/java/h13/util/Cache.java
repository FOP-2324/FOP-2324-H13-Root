package h13.util;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * A cache is a collection that stores data in memory for fast access.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 * @author Nhan Huynh
 */
public interface Cache<K, V> {

    /**
     * Returns the value to which the specified key is mapped, or {@code null} if this cache contains no mapping for
     * the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or {@code null} if this cache contains no mapping for
     * the key
     */
    @Nullable V get(K key);

    /**
     * Associates the specified value with the specified key in this cache.
     *
     * @param key   the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @return the previous value associated with key, or {@code null} if there was no mapping for key
     */
    @Nullable V put(K key, V value);

    /**
     * If the specified key is not already associated with a value (or is mapped to null) associates it with the
     * given value and returns {@code null}, else returns the current value.
     *
     * @param key    key with which the specified value is to be associated
     * @param mapper function to compute a value
     * @return the previous value associated with the specified key, or {@code null} if there was no mapping for the key
     */
    V computeIfAbsent(K key, Function<? super K, ? extends V> mapper);

    /**
     * Removes the mapping for all keys from this cache.
     */
    void clear();

    /**
     * Returns the number of key-value mappings in this cache.
     *
     * @return the number of key-value mappings in this cache
     */
    int size();

    /**
     * Returns the maximum number of key-value mappings this cache can hold.
     *
     * @return the maximum number of key-value mappings this cache can hold
     */
    int capacity();

}
