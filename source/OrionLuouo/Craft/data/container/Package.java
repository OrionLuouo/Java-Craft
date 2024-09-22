package OrionLuouo.Craft.data.container;

import OrionLuouo.Craft.data.Processor;
import OrionLuouo.Craft.data.Container;
import OrionLuouo.Craft.data.Iterator;
import OrionLuouo.Craft.data.Stream;

import java.lang.reflect.Array;

public interface Package<E> extends Container<E> {
    E get(int index);
    void set(int index , E element);
    default E[] get(int index , int number , Class<E> type) {
        return get(index , number , (E[]) Array.newInstance(type , number));
    }
    E[] get(int index , int number , E[] array);
    E replace(int index , E newElement);
    default void replace(int index , E[] newElements) {
        int end = index + newElements.length;
        int ei = 0;
        while (index < end) {
            newElements[ei] = replace(index++ , newElements[ei++]);
        }
    }
    void replace(int index , Iterator<E> iterator);
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
    @Override
    Package<E> copy();
}
