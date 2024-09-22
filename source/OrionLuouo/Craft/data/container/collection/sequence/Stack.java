package OrionLuouo.Craft.data.container.collection.sequence;

import OrionLuouo.Craft.data.container.collection.Sequence;

public interface Stack<E> extends Sequence<E> {
    E getLast();
    E pop();
    @Override
    Stack<E> copy();
}
