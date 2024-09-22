package OrionLuouo.Craft.data.container.collection;

import OrionLuouo.Craft.data.container.Collection;

public interface Set<E> extends Collection<E> {
    void intersect(Set<E> set);
    default Set<E> intersection(Set<E> set) {
        Set<E> newSet = copy();
        newSet.intersect(set);
        return newSet;
    }
    void unite(Set<E> set);
    default Set<E> union(Set<E> set) {
        Set<E> newSet = copy();
        newSet.unite(set);
        return newSet;
    }
    void exclude(Set<E> set);
    default Set<E> exclusion(Set<E> set) {
        Set<E> newSet = copy();
        newSet.exclude(set);
        return newSet;
    }
    void exclusiveOr(Set<E> set);
    default Set<E> exclusiveOrSet(Set<E> set) {
        Set<E> newSet = copy();
        newSet.exclusiveOr(set);
        return newSet;
    }
    @Override
    Set<E> copy();
    boolean contains(E element);
}
