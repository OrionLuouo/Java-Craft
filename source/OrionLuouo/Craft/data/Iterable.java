package OrionLuouo.Craft.data;

public interface Iterable<E> {
    Iterator<E> iterator();
    default void iterate(Processor<E> processor) {
        iterator().iterate(processor);
    }
}
