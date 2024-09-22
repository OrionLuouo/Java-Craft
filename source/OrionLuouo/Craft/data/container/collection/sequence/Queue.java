package OrionLuouo.Craft.data.container.collection.sequence;

import OrionLuouo.Craft.data.container.collection.Sequence;

public interface Queue<E> extends Sequence<E> {
    E getFirst();
    E poll();
    @Override
    Queue<E> copy();
}
