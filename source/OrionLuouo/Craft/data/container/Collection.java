package OrionLuouo.Craft.data.container;

import OrionLuouo.Craft.data.Container;
import OrionLuouo.Craft.data.Iterator;

import java.lang.ref.Cleaner;

public interface Collection<E> extends Container<E> , Cleaner.Cleanable {
    void add(E element);
    default void add(Container<E> container) {
        add(container.size() , container.iterator());
    }
    void add(Iterator<E> iterator);
    default void add(E[] array) {
        for (E element : array) {
            add(element);
        }
    }
    void add(int number , Iterator<E> iterator);
    @Override
    Collection<E> copy();
}
