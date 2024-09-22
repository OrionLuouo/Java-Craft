package OrionLuouo.Craft.data.java.container;

import java.util.*;

public class LinkedTreeMap<K , V> implements Map<K , V> {
     final TreeMap<K, Node<V>> map;
     Node last , first;

    {
        map = new TreeMap<>();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key).value;
    }

    @Override
    public V put(K key, V value) {
        Node<V> node = new Node<>(value);
        if (map.size() == 0) {
            first = last = node;
        }
        else {
            node.fore = last;
            last.after = node;
        }
        return map.put(key , node).value;
    }

    @Override
    public V remove(Object key) {
        Node<V> node = map.remove(key);
        if (node != first) {
            node.fore.after = node.after;
        }
        else {
            first = node.after;
        }
        if (node != last) {
            node.after.fore = node.fore;
        }
        else {
            last = node.fore;
        }
        return node.value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach((k , v) -> {
            put(k , v);
        });
    }

    @Override
    public void clear() {
        map.clear();
        first = last = null;
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        Set<V> set = new HashSet<>();
        map.values().iterator().forEachRemaining(v -> {
            set.add(v.value);
        });
        return set;
    }

    @Override
    @Deprecated
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    final static class Node<V> {
        Node fore , after;
        V value;

        Node(V value) {
            this.value = value;
        }
    }
}
