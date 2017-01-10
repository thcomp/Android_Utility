package jp.co.thcomp.util;

import java.util.List;

public interface Multimap<K, V> {
    void add(K key, V value);
    void addAll(K key, List<V> values);
    void clear(K key);
    void clearAll();
    boolean containsKey(Object key);
    boolean containsValue(Object value);
    List<V> get(K key);
    List<V> getByIndex(int index);
    List<V> getBySortedIndex(int index);
    boolean isEmpty();
    boolean replace(K key, V prevValue, V nextValue);
    List<V> remove(K key);
    boolean removeValue(V value);
    int size();
    int keySize();
    List<K> keyList();
}
