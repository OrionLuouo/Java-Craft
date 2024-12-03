package OrionLuouo.Craft.data.container;

import OrionLuouo.Craft.data.Processor;
import OrionLuouo.Craft.data.Container;
import OrionLuouo.Craft.data.Iterator;

import java.lang.reflect.Array;

public interface Pack<E> extends Container<E> {
    E get(int index);
    void set(int index , E element);
    default E[] toArray(int index , int number , Class<E> type) {
        return toArray(index , number , (E[]) Array.newInstance(type , number));
    }
    E replace(int index , E newElement);
    default void set(int index , E[] newElements) {
        int end = index + newElements.length;
        int ei = 0;
        while (index < end) {
            newElements[ei] = replace(index++ , newElements[ei++]);
        }
    }
    void set(int index , Iterator<E> iterator);
    @Override
    default void iterate(Processor<E> processor) {
        int size = size();
        for (int index = 0 ; index < size ;) {
            processor.process(get(index++));
        }
    }
    @Override
    default Iterator<E> iterator() {
        return new Iterator<E>() {
            int size = size() , index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                return get(index++);
            }

            @Override
            public void iterate(Processor<E> processor) {
                while (index < size) {
                    processor.process(get(index++));
                }
            }
        };
    }
    Iterator<E> iterator(int index);
    default void iterate(int index , Processor<E> processor) {
        Iterator<E> iterator = iterator(index);
        iterator.iterate(processor);
    }
    @Override
    Pack<E> copy();
    default E[] toArray(int index , int number , E[] array) {
        array = array.length < number ? (E[]) Array.newInstance(array.getClass().getComponentType(), number) : array;
        Iterator<E> iterator = iterator(index);
        index = 0;
        while (number-- > 0) {
            array[index++] = iterator.next();
        }
        return array;
    }
}
