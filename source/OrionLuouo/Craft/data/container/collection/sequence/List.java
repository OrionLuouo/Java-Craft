package OrionLuouo.Craft.data.container.collection.sequence;

public interface List<E> extends Queue<E> , Stack<E> {
    @Override
    List<E> copy();
}
