package OrionLuouo.Craft.data.container.collection.sequence;

import OrionLuouo.Craft.data.Iterator;

public class SuperChainList<E> implements List<E> {
    static class Node<E> {
        Node next;
        E value;
    }

    static class GuardNode<E> {
        GuardNode fore , next;
        int leapingNodeCount;
        Node<E> node;

        GuardNode(Node<E> node) {
            this.node = node;
        }
    }

    public static final int LEAP_STEP = 16;

    int leapStep , size;
    final Node<E> root;
    final GuardNode<E> guard;

    public SuperChainList() {
        this(LEAP_STEP);
    }

    public SuperChainList(int leapStep) {
        this.leapStep = leapStep;
        root = new Node<E>();
        guard = new GuardNode<E>(root);
    }

    @Override
    public E getFirst() {
        return null;
    }

    @Override
    public E poll() {
        return null;
    }

    @Override
    public E getLast() {
        return null;
    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public E remove(int index) {
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
