package OrionLuouo.Craft.data.container.collection;

import OrionLuouo.Craft.data.Processor;
import OrionLuouo.Craft.data.Iterator;
import OrionLuouo.Craft.data.Stream;
import OrionLuouo.Craft.data.container.Collection;

import java.lang.reflect.Array;

public interface Sequence<E> extends Collection<E> {
    /**
     * @param <E> : Element
     */
    interface Handler<E> extends Iterator<E> {
        boolean hasFore();
        E fore();
        default void iterateFore(Processor<E> processor) {
            while (hasFore()) {
                processor.process(fore());
            }
        }
    }

    E get(int index);
    E remove(int index);
    void remove(int index , int number);
    void insert(int index , E element);
    void insert(int index , E[] array);
    void insert(int index , int number , Iterator<E> iterator);
    void insert(int index , Iterator<E> iterator);
    default E[] toArray(int index , int number , Class<E> type) {
        return toArray(index , number , (E[]) Array.newInstance(type , number));
    }
    E[] toArray(int index , int number , E[] array);
    default E[] removeAndGet(int index , int number , Class<E> type) {
        return removeAndGet(index , number , (E[]) Array.newInstance(type , number));
    }
    E[] removeAndGet(int index , int number , E[] array);
    Iterator<E> iterator(int index);
    @Override
    default Iterator<E> iterator() {
        return iterator(0);
    }
    @Override
    default void iterate(Processor<E> processor) {
        iterate(processor, 0);
    }
    default void iterate(Processor<E> processor, int index) {
        iterator(index).iterate(processor);
    }
    Handler<E> handler(int index);
    default Handler<E> handler() {
        return handler(0);
    }
    @Override
    Sequence<E> copy();
}
