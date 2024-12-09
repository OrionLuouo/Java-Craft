package OrionLuouo.Craft.data;

import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @param <E> : Element
 */
public interface Container<E> {
    int size();
    default E[] toArray(Class<E> type) {
        return toArray((E[]) Array.newInstance(type , size()));
    }
    default E[] toArray(E[] array) {
        array = array.length < size() ? (E[]) Array.newInstance(array.getClass (), size()) : array;
        AtomicInteger index = new AtomicInteger();
        E[] finalArray = array;
        iterate(element -> {
            finalArray[index.getAndIncrement()] = element;
        });
        return array;
    }
    Iterator<E> iterator();
    default void iterate(Processor<E> processor) {
        iterator().iterate(processor);
    }
    Container<E> copy();

    /**
     * The interruptible version of method:iterate(Processor<E>).
     *
     * @param processor If continues,
     *                  return true.
     */
    default void iterateInterruptibly(Function<Boolean , E> processor) {
        Iterator<E> iterator = iterator();
        while (iterator.hasNext() && processor.process(iterator.next())) {
        }
    }
}
