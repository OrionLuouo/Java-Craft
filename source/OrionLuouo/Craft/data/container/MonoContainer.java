package OrionLuouo.Craft.data.container;

import OrionLuouo.Craft.data.Container;
import OrionLuouo.Craft.data.Iterator;

import java.lang.reflect.Array;

public class MonoContainer<V> implements Container<V> {
    V value;

    public MonoContainer(V value) {
        this.value = value;
    }

    public MonoContainer() {
    }

    public void set(V value) {
        this.value = value;
    }

    public V get() {
        return value;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public V[] toArray(V[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            array = (V[]) Array.newInstance(array.getClass().getComponentType(), 1);
        }
        array[0] = value;
        return array;
    }

    @Override
    public Iterator<V> iterator() {
        return new Iterator<V>() {
            boolean got = false;

            @Override
            public boolean hasNext() {
                return !got;
            }

            @Override
            public V next() {
                if (!got) {
                    got = true;
                    return value;
                }
                return null;
            }
        };
    }

    @Override
    public Container<V> copy() {
        return new MonoContainer<>(value);
    }
}
