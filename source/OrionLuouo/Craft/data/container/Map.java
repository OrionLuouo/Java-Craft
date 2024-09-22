package OrionLuouo.Craft.data.container;

import OrionLuouo.Craft.data.Iterator;
import OrionLuouo.Craft.data.container.collection.Set;

public interface Map<K , V> extends Set<Map.Entry<K , V>> {
    record Entry<K , V>(K key , V value) {
        public int hashCode() {
            return key.hashCode();
        }
    }

    V get(K key);
    boolean containsKey(K key);
    default void add(K key , V value) {
        add(new Entry<>(key , value));
    }
    V remove(K key);
    Iterator<V> valueIterator();

    @Override
    default boolean contains(Entry<K , V> entry) {
        return containsKey(entry.key);
    }

    @Override
    default void intersect(Set<Entry<K , V>> set) {
        set.iterate(e -> {
            if (!contains(e)) {
                remove(e.key);
            }

        });
    }

    @Override
    default void unite(Set<Entry<K , V>> set) {
        set.iterate(e -> {
            add(e);
        });
    }

    @Override
    default void exclude(Set<Entry<K , V>> set) {
        set.iterate(e -> {
            remove(e.key);
        });
    }

    @Override
    default void exclusiveOr(Set<Entry<K , V>> set) {
        set.iterate(e -> {
            if (containsKey(e.key)) {
                remove(e.key);
            }
            else {
                add(e);
            }
        });
    }
}
