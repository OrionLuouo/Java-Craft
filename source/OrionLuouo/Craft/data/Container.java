package OrionLuouo.Craft.data;

import java.lang.reflect.Array;

/**
 *
 * @param <E> : Element
 */
public interface Container<E> {
    int size();
    default E[] toArray(Class<E> type) {
        return toArray((E[]) Array.newInstance(type , size()));
    }
    E[] toArray(E[] array);
    Iterator<E> iterator();
    default void iterate(Processor<E> processor) {
        iterator().iterate(processor);
    }
    Container<E> copy();
}
