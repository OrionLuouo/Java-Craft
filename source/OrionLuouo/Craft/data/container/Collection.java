package OrionLuouo.Craft.data.container;

import OrionLuouo.Craft.data.Container;
import OrionLuouo.Craft.data.Iterator;

public interface Collection<E> extends Container<E> {
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
