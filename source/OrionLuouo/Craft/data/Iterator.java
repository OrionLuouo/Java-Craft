package OrionLuouo.Craft.data;

/**
 * @param <E> : Element
 */
public interface Iterator<E> {
    default void iterate(Processor<E> processor) {
        while(hasNext()) {
            processor.process(next());
        }
    }
    boolean hasNext();
    E next();

    class Util {
        public static final void fillArray(Object[] array , int index , Iterator<?> iterator , int number) {
            while (number-- > 0) {
                array[index++] = iterator.next();
            }
        }
    }
}
