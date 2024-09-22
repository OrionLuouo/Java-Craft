package OrionLuouo.Craft.data;

import OrionLuouo.Craft.system.annotation.Unfinished;

@Unfinished
public interface Stream<E> {
    Stream<E> connect(Stream<E> stream);
    Stream<E> filter(Stream<E> stream);
    Iterator<E> iterator();
}
