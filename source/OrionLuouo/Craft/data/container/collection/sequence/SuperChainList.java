package OrionLuouo.Craft.data.container.collection.sequence;

import OrionLuouo.Craft.data.Iterator;
import OrionLuouo.Craft.system.annotation.Unfinished;

public class SuperChainList<E> implements List<E> {
    static class Node<E> {
        Node<E> next;
        E value;
    }

    static class GuardNode<E> {
        GuardNode<E> fore , next;
        int step;
        Node<E> node;

        GuardNode(Node<E> node) {
            this.node = node;
        }
    }

    public static final int LEAP_STEP = 16;

    int leapStep , splitStep , size;
    final Node<E> root;
    final GuardNode<E> guard;
    Node<E> tail;
    GuardNode<E> tailGuard;


    public SuperChainList() {
        this(LEAP_STEP);
    }

    public SuperChainList(int leapStep) {
        this.leapStep = leapStep;
        splitStep = leapStep >> 1;
        root = new Node<E>();
        guard = new GuardNode<E>(root);
        tailGuard = guard;
    }

    private void checkSurMerge(GuardNode<E> guard) {
        for (int step = guard.step + guard.next.step ; step > leapStep && step < splitStep && guard.next != null ; step = guard.step + guard.next.step) {
            guard.step = step;
            guard.next = guard.next.next;
            if (guard.next == null) {
                tailGuard = guard;
                return;
            }
            guard = guard.next;
        }
    }

    private void checkForeMerge(GuardNode<E> guard) {
        for (int step = guard.step + guard.next.step ; step > leapStep && step < splitStep ; step = guard.step + guard.next.step) {
            guard = guard.fore;
            guard.step = step;
            guard.next = guard.next.next;
        }
        if (guard.next == null) {
            tailGuard = guard;
        }
    }

    private void checkSplit(GuardNode<E> guard) {
        while (guard.step > splitStep) {
            Node<E> next = guard.node;
            int surStep = guard.step - leapStep , looper = leapStep;
            while (looper-- > 0) {
                next = next.next;
            }
            GuardNode<E> surGuard = new GuardNode<>(next);
            surGuard.step = surStep;
            guard.step = leapStep;
            surGuard.next = guard.next;
            guard.next = surGuard;
            guard = surGuard;
        }
        if (guard.next == null) {
            tailGuard = guard;
        }
    }

    @Override
    public E getFirst() {
        return root.next.value;
    }

    @Override
    public E poll() {
        Node<E> node = root.next;
        root.next = node.next;
        guard.node = node.next;
        guard.step--;
        size--;
        checkSurMerge(guard);
        if (root.next == null) {
            tail = null;
        }
        return node.value;
    }

    @Override
    public E getLast() {
        return tail.value;
    }

    @Override
    public E pop() {
        if (tailGuard.node == tail) {
            E value = tail.value;
            GuardNode<E> foreGuard = tailGuard.fore;
            Node<E> fore = foreGuard.node;
            while (fore.next != tail) {
                fore = fore.next;
            }
            tail = fore;
            tailGuard = foreGuard;
            foreGuard.next = null;
            size--;
            tail.next = null;
            return value;
        }
        else {
            Node<E> fore = tailGuard.node;
            while (fore.next != tail) {
                fore = fore.next;
            }
            E value = tail.value;
            tail = fore;
            tail.next = null;
            tailGuard.step--;
            checkForeMerge(guard);
            return value;
        }
    }

    @Override
    public E get(int index) {
        GuardNode<E> guardNode = guard;
        while (index >= guardNode.step) {
            index -= guardNode.step;
            guardNode = guardNode.next;
        }
        Node<E> node = guardNode.node;
        while (index > 0) {
            node = node.next;
            index--;
        }
        return node.value;
    }

    @Unfinished
    @Override
    public E remove(int index) {
        if (index == size - 1) {
            return pop();
        }
        GuardNode<E> guardNode = guard;
        while (index > guardNode.step) {
            index -= guardNode.step;
            guardNode = guardNode.next;
        }
        Node<E> node = guardNode.node;
        while (--index > 0) {
            node = node.next;
        }

        return null;
    }

    @Override
    public void remove(int index, int number) {

    }

    @Override
    public void insert(int index, E element) {

    }

    @Override
    public void insert(int index, E[] array) {

    }

    @Override
    public void insert(int index, int number, Iterator iterator) {

    }

    @Override
    public void insert(int index, Iterator iterator) {

    }

    @Override
    public E[] toArray(int index, int number, E[] array) {
        return array;
    }

    @Override
    public E[] removeAndGet(int index, int number, E[] array) {
        return array;
    }

    @Override
    public Iterator iterator(int index) {
        return null;
    }

    @Override
    public Handler handler(int index) {
        return null;
    }

    @Override
    public void add(E element) {

    }

    @Override
    public void add(Iterator iterator) {

    }

    @Override
    public void add(int number, Iterator iterator) {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public E[] toArray(E[] array) {
        return array;
    }

    @Override
    public List copy() {
        return null;
    }

    @Override
    public void clean() {

    }
}
