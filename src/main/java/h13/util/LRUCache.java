package h13.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A least-recently used cache implementation.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 * @author Nhan Huynh
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> implements Cache<K, V> {

    /**
     * The maximum number of key-value mappings this cache can hold.
     */
    private final int capacity;

    /**
     * Constructs a new, empty cache with the specified maximum capacity.
     *
     * @param capacity the maximum number of key-value mappings this cache can hold
     */
    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

    @Override
    public int capacity() {
        return capacity;
    }
}
