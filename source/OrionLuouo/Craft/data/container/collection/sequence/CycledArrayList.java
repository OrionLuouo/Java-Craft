package OrionLuouo.Craft.data.container.collection.sequence;

import OrionLuouo.Craft.data.Iterator;

public class CycledArrayList<E> implements List<E> {
    public static final int DEFAULT_CAPACITY = 64;

    Object[] array;
    int indexBegin , indexEnd;
    final int initialCapacity;

    public CycledArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public CycledArrayList(final int initialCapacity) {
        this.initialCapacity = initialCapacity;
        array = new Object[initialCapacity];
        indexBegin = 0;
        indexEnd = 0;
    }

    @Override
    public E getFirst() {
        return (E) array[indexBegin];
    }

    @Override
    public E poll() {
        E value = (E) array[indexBegin++];
        if (indexBegin == array.length) {
            indexBegin = 0;
        }
        if (size() < initialCapacity >> 1 && array.length != initialCapacity) {
            Object[] newArray = new Object[array.length >> 1];
            if (indexEnd > indexBegin) {
                indexEnd = indexEnd - indexBegin;
                System.arraycopy(array, indexBegin, newArray, 0, indexEnd);
            }
            else if (indexEnd == indexBegin) {
                indexEnd = 0;
            }
            else {
                System.arraycopy(array, indexBegin, newArray, 0, array.length - indexBegin);
                System.arraycopy(array, 0, newArray, array.length - indexBegin, indexEnd);
                indexEnd = array.length + indexEnd - indexBegin;
            }
            indexBegin = 0;
            array = newArray;
        }
        return value;
    }

    @Override
    public E getLast() {
        return (E) array[indexEnd - 1 < 0  ? array.length - 1 : indexEnd - 1];
    }

    @Override
    public E pop() {
        if (--indexEnd == -1) {
            indexEnd = array.length - 1;
        }
        if (size() < initialCapacity >> 1 && array.length != initialCapacity) {
            Object[] newArray = new Object[array.length >> 1];
            if (indexEnd > indexBegin) {
                indexEnd = indexEnd - indexBegin;
                System.arraycopy(array, indexBegin, newArray, 0, indexEnd);
            }
            else if (indexEnd == indexBegin) {
                indexEnd = 0;
            }
            else {
                System.arraycopy(array, indexBegin, newArray, 0, array.length - indexBegin);
                System.arraycopy(array, 0, newArray, array.length - indexBegin, indexEnd);
                indexEnd = array.length + indexEnd - indexBegin;
            }
            indexBegin = 0;
            array = newArray;
        }
        return (E) array[indexEnd];
    }

    @Override
    public E get(int index) {
        index += indexBegin;
        index = index >= array.length ? 0 : index;
        return (E) array[index];
    }

    @Deprecated
    @Override
    public E remove(int index) {
        return null;
    }

    @Deprecated
    @Override
    public void remove(int index, int number) {

    }

    @Deprecated
    @Override
    public void insert(int index, E element) {

    }

    @Deprecated
    @Override
    public void insert(int index, E[] array) {

    }

    @Deprecated
    @Override
    public void insert(int index, int number, Iterator<E> iterator) {

    }

    @Deprecated
    @Override
    public void insert(int index, Iterator<E> iterator) {

    }

    @Deprecated
    @Override
    public E[] toArray(int index, int number, E[] array) {
        return null;
    }

    @Deprecated
    @Override
    public E[] removeAndGet(int index, int number, E[] array) {
        return null;
    }

    @Override
    public Iterator<E> iterator(int index) {
        return new Iterator<E>() {
            int index = indexBegin;

            @Override
            public boolean hasNext() {
                return index != indexEnd;
            }

            @Override
            public E next() {
                E value = (E) array[index++];
                if (index == array.length) {
                    index = 0;
                }
                return value;
            }
        };
    }


    @Deprecated
    @Override
    public Handler<E> handler(int index) {
        return null;
    }

    @Override
    public void add(E element) {
        array[indexEnd++] = element;
        if (indexEnd == array.length) {
            indexEnd = 0;
        }
        if (indexEnd == indexBegin) {
            Object[] newArray = new Object[array.length << 1];
            System.arraycopy(array , indexBegin , newArray , 0 , array.length - indexBegin);
            if (indexBegin != 0) {
                System.arraycopy(array , 0 , newArray , array.length - indexBegin , indexEnd);
            }
            array = newArray;
            indexBegin = 0;
            indexEnd = 0;
        }
    }

    @Override
    public void add(Iterator<E> iterator) {
        while (iterator.hasNext()) {
            add(iterator.next());
        }
    }

    @Override
    public void add(int number, Iterator<E> iterator) {
        while (iterator.hasNext()) {
            if (--number < 0) {
                break;
            }
            add(iterator.next());
        }
    }

    @Override
    public int size() {
        int size = indexEnd - indexBegin;
        return size < 0 ? array.length + size : size;
    }

    @Deprecated
    @Override
    public E[] toArray(E[] array) {
        return null;
    }

    @Deprecated
    @Override
    public List<E> copy() {
        return null;
    }

    @Override
    public void clean() {
        array = new Object[initialCapacity];
        indexBegin = 0;
        indexEnd = 0;
    }
}
