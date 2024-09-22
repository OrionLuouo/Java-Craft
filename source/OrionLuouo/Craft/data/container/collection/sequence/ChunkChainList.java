package OrionLuouo.Craft.data.container.collection.sequence;

import OrionLuouo.Craft.data.Iterator;

import java.lang.reflect.Array;

public class ChunkChainList<E> implements List<E> {
    public static final int DEFAULT_CHUNK_SIZE = 16;

    static class Chunk {
        Object[] array;
        Chunk fore , next;

        Chunk() {}

        Chunk(int size , Chunk fore) {
            array = new Object[size];
            this.fore = fore;
        }
    }

    Chunk root , tail;
    int chunkSize , size , headIndex , tailIndex;

    {
        headIndex = 0;
    }

    public ChunkChainList() {
        this(DEFAULT_CHUNK_SIZE);
    }

    public ChunkChainList(int chunkSize) {
        root = new Chunk(0 , root);
        root.fore = root;
        size = 0;
        tailIndex = 0;
        this.chunkSize = chunkSize < 0 ? DEFAULT_CHUNK_SIZE : chunkSize;
        root.next = tail = new Chunk(chunkSize , root);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E[] toArray(E[] array) {
        array = array.length < size ? (E[]) Array.newInstance(array.getClass().getComponentType() , size) : array;
        Chunk chunk = root.next;
        if (chunk != tail) {
            int index = 0;
            System.arraycopy(chunk.array , headIndex , array , 0 , index = chunk.array.length - headIndex);
            while ((chunk = chunk.next) != tail) {
                System.arraycopy(chunk.array , 0 , array , index , chunk.array.length);
            }
            System.arraycopy(chunk.array , 0 , array , index , tailIndex);
        }
        else {
            System.arraycopy(chunk.array , headIndex , array , 0 , size);
        }
        return array;
    }

    @Override
    public void add(E element) {
        tail.array[tailIndex++] = element;
        if (tailIndex == tail.array.length) {
            tail = tail.next = new Chunk(chunkSize , tail);
            tailIndex = 0;
        }
        size++;
    }

    @Override
    public void add(int number, Iterator<E> iterator) {
        final int numberCopy = number;
        if (number < tail.array.length - tailIndex) {
            while (number-- > 0) {
                tail.array[tailIndex++] = iterator.next();
            }
        }
        else {
            number -= tail.array.length - tailIndex;
            while (tailIndex != tail.array.length) {
                tail.array[tailIndex++] = iterator.next();
            }
            while ((number -= chunkSize) >= 0) {
                tail = tail.next = new Chunk(chunkSize , tail);
                tailIndex = 0;
                while (tailIndex != chunkSize) {
                    tail.array[tailIndex++] = iterator.next();
                }
            }
            tail = tail.next = new Chunk(chunkSize , tail);
            number += chunkSize;
            tailIndex = 0;
            while (tailIndex != number) {
                tail.array[tailIndex++] = iterator.next();
            }
        }
        size += numberCopy;
    }

    @Override
    public void add(Iterator<E> iterator) {
        while (iterator.hasNext()) {
            add(iterator.next());
        }
    }

    @Override
    public E get(int index) {
        Chunk chunk = root.next;
        index += headIndex;
        while (index >= chunk.array.length) {
            index -= chunk.array.length;
            chunk = chunk.next;
        }
        return (E) chunk.array[index];
    }

    @Override
    public E remove(int index) {
        Chunk chunk = root.next;
        index += headIndex;
        while (index >= chunk.array.length) {
            index -= chunk.array.length;
            chunk = chunk.next;
        }
        E value = (E) chunk.array[index];
        int begin = root.next == chunk ? headIndex : 0
                ,end = (tail == chunk ? tailIndex : chunk.array.length) - 1;
        if (begin == end) {
            if (chunk == tail) {
                tail = chunk == root.next ? chunk : chunk.fore;
            }
            else {
                chunk.fore.next = chunk.next;
                chunk.next.fore = chunk.fore;
            }
            tail = chunk == tail ? chunk.fore : tail;
            headIndex = chunk == root.next ? 0 : headIndex;
            tailIndex = chunk == tail ? chunk.fore.array.length : tailIndex;
        }
        else if (index == begin) {
            Object[] array = new Object[end - begin];
            System.arraycopy(chunk.array , begin + 1 , array , 0 , array.length);
            chunk.array = array;
            if (chunk == root.next) {
                headIndex++;
            }
        }
        else if (index == end) {
            if (chunk == tail) {
                tailIndex--;
            }
            else {
                Object[] array = new Object[end - begin];
                System.arraycopy(chunk.array , begin , array , 0 , array.length);
                chunk.array = array;
            }
        }
        else {
            Object[] array = new Object[end - begin];
            System.arraycopy(chunk.array , begin , array , 0 , index - begin);
            System.arraycopy(chunk.array , index + 1 , array , index - begin , end - index);
            chunk.array = array;
        }
        size--;
        return value;
    }

    @Override
    public void remove(int index, int number) {
        Chunk chunk = root.next;
        index += headIndex;
        while (index >= chunk.array.length) {
            index -= chunk.array.length;
            chunk = chunk.next;
        }
        int endIndex = index + number;
        Chunk endChunk = chunk;
        while (endIndex >= endChunk.array.length) {
            endChunk = endChunk.next;
            endIndex -= endChunk.array.length;
        }
        if (chunk == endChunk) {
            int begin = chunk == root.next ? headIndex : 0
                    ,end = endChunk == tail ? tailIndex : endChunk.array.length;
            Object[] newArray = new Object[end - begin - endIndex + index];
            int newArrayIndex = index - begin + 1;
            System.arraycopy(chunk.array , begin , newArray , 0 , newArrayIndex);
            System.arraycopy(chunk.array , endIndex , newArray , newArrayIndex , newArray.length - newArrayIndex);
            chunk.array = newArray;
            if (chunk == tail) {
                tailIndex = 0;
                chunk.next = tail = new Chunk(chunkSize , chunk);
            }
            headIndex = chunk == root.next ? 0 : headIndex;
        }
        else {
            endChunk.fore = chunk.fore;
            chunk.fore.next = endChunk;
            int begin = chunk == root.next ? headIndex : 0
                    ,end = endChunk == tail ? tailIndex : endChunk.array.length;
            int foreLength = index - begin
                    ,backLength = end - endIndex;
            Object[] newArray = new Object[foreLength + backLength];
            System.arraycopy(chunk.array , begin , newArray , 0 , foreLength);
            System.arraycopy(endChunk.array , endIndex , newArray , foreLength , backLength);
            endChunk.array = newArray;
            headIndex = chunk == root.next ? 0 : headIndex;
            if (endChunk == tail) {
                tailIndex = 0;
                endChunk.next = tail = new Chunk(chunkSize , tail);
            }
        }
        size -= number;
    }

    @Override
    public void insert(int index, E element) {
        Chunk chunk = root.next;
        index += headIndex;
        while (index >= chunk.array.length) {
            index -= chunk.array.length;
            chunk = chunk.next;
        }
        if (chunk.array.length >= chunkSize << 1 && chunk != tail && chunk != root.next) {
            int foreLength = chunk.array.length >> 1;
            Chunk afterChunk = new Chunk(chunk.array.length - foreLength + 1 , chunk);
            afterChunk.next = chunk.next;
            chunk.next.fore = afterChunk;
            chunk.next = afterChunk;
            Object[] array = new Object[foreLength];
            System.arraycopy(chunk.array , 0 , array , 0 , foreLength);
            System.arraycopy(chunk.array , foreLength , afterChunk.array , 1 , afterChunk.array.length - 1);
            afterChunk.array[0] = element;
            chunk.array = array;
        }
        else {
            Object[] array = new Object[chunk.array.length + 1];
            System.arraycopy(chunk.array , 0 , array , 0 , index);
            System.arraycopy(chunk.array , index , array , index + 1 , chunk.array.length - index);
            array[index] = element;
            chunk.array = array;
            if (chunk == tail) {
                tailIndex++;
            }
        }
        size++;
    }

    @Override
    public void insert(int index, E[] array) {
        Chunk chunk = root.next;
        index += headIndex;
        while (index >= chunk.array.length) {
            index -= chunk.array.length;
            chunk = chunk.next;
        }
        if (array.length < chunkSize) {
            Object[] newArray = new Object[chunk.array.length + 1];
            System.arraycopy(chunk.array , 0 , newArray , 0 , index);
            System.arraycopy(chunk.array , index , newArray , index + array.length , chunk.array.length - index);
            System.arraycopy(array , 0 , newArray , index , array.length);
            chunk.array = newArray;
            if (chunk == tail) {
                tailIndex += array.length;
            }
        }
        else {
            Chunk endChunk = null;
            if (index == 0) {
                chunk = (endChunk = chunk).fore;
            }
            else if (index == chunk.array.length - 1) {
                endChunk = chunk.next;
            }
            else {
                endChunk = new Chunk();
                int length;
                if (chunk == tail) {
                    endChunk.array = new Object[chunkSize];
                    if ((length = tailIndex - index) == chunkSize) {
                        endChunk.next = tail = new Chunk(chunkSize , endChunk);
                        tailIndex = 0;
                    }
                }
                else {
                    endChunk.array = new Object[chunk.array.length - index];
                    length = chunk.array.length - index;
                }
                System.arraycopy(chunk.array , index , endChunk.array , 0 , length);
                Object[] newArray;
                if (chunk == root.next) {
                    newArray = new Object[index - headIndex];
                    System.arraycopy(chunk.array , headIndex , newArray , 0 , newArray.length);
                    headIndex = 0;
                }
                else {
                    newArray = new Object[index];
                    System.arraycopy(chunk.array , 0 , newArray , 0 , newArray.length);
                }
                chunk.array = newArray;
            }
            int number = array.length;
            index = 0;
            while (number > chunkSize) {
                chunk = chunk.next = new Chunk(chunkSize , chunk);
                System.arraycopy(array , index , chunk.array , 0 , chunkSize);
                index += chunkSize;
                number -= chunkSize;
            }
            if (number != 0) {
                chunk = chunk.next = new Chunk(number , chunk);
                System.arraycopy(array , index , chunk.array , 0 , number);
            }
            if (endChunk == null) {
                chunk.next = tail = new Chunk(chunkSize , chunk);
                tailIndex = 0;
            }
            chunk.next = endChunk;
            endChunk.fore = chunk;
        }
        size += array.length;
    }

    @Override
    public void insert(int index, int number, Iterator<E> iterator) {
        final int numberCopy = number;
        Chunk chunk = root.next;
        index += headIndex;
        while (index >= chunk.array.length) {
            index -= chunk.array.length;
            chunk = chunk.next;
        }
        if (number < chunkSize) {
            Object[] newArray = new Object[chunk.array.length + 1];
            System.arraycopy(chunk.array , 0 , newArray , 0 , index);
            System.arraycopy(chunk.array , index , newArray , index + number , chunk.array.length - index);
            Iterator.Util.fillArray(newArray , index , iterator , number);
            chunk.array = newArray;
            if (chunk == tail) {
                tailIndex += number;
            }
        }
        else {
            Chunk endChunk = null;
            if (index == 0) {
                chunk = (endChunk = chunk).fore;
            }
            else if (index == chunk.array.length - 1) {
                endChunk = chunk.next;
            }
            else {
                endChunk = new Chunk();
                int length;
                if (chunk == tail) {
                    endChunk.array = new Object[chunkSize];
                    if ((length = tailIndex - index) == chunkSize) {
                        endChunk.next = tail = new Chunk(chunkSize , endChunk);
                        tailIndex = 0;
                    }
                }
                else {
                    endChunk.array = new Object[chunk.array.length - index];
                    length = chunk.array.length - index;
                }
                System.arraycopy(chunk.array , index , endChunk.array , 0 , length);
                Object[] newArray;
                if (chunk == root.next) {
                    newArray = new Object[index - headIndex];
                    System.arraycopy(chunk.array , headIndex , newArray , 0 , newArray.length);
                    headIndex = 0;
                }
                else {
                    newArray = new Object[index];
                    System.arraycopy(chunk.array , 0 , newArray , 0 , newArray.length);
                }
                chunk.array = newArray;
            }
            index = 0;
            while (number > chunkSize) {
                chunk = chunk.next = new Chunk(chunkSize , chunk);
                Iterator.Util.fillArray(chunk.array , 0 , iterator , chunkSize);
                index += chunkSize;
                number -= chunkSize;
            }
            if (number != 0) {
                chunk = chunk.next = new Chunk(number , chunk);
                Iterator.Util.fillArray(chunk.array , 0 , iterator , number);
            }
            if (endChunk == null) {
                chunk.next = tail = new Chunk(chunkSize , chunk);
                tailIndex = 0;
            }
            chunk.next = endChunk;
            endChunk.fore = chunk;
        }
        size += numberCopy;
    }

    @Override
    public void insert(int index , Iterator<E> iterator) {
        Chunk chunk = root.next;
        index += headIndex;
        while (index >= chunk.array.length) {
            index -= chunk.array.length;
            chunk = chunk.next;
        }
        Chunk endChunk = null;
        if (index == 0) {
            chunk = (endChunk = chunk).fore;
        }
        else if (index == chunk.array.length - 1) {
            endChunk = chunk.next;
        }
        else {
            endChunk = new Chunk();
            int length;
            if (chunk == tail) {
                endChunk.array = new Object[chunkSize];
                if ((length = tailIndex - index) == chunkSize) {
                    endChunk.next = tail = new Chunk(chunkSize , endChunk);
                    tailIndex = 0;
                }
            } else {
                endChunk.array = new Object[chunk.array.length - index];
                length = chunk.array.length - index;
            }
            System.arraycopy(chunk.array , index , endChunk.array , 0 , length);
            Object[] newArray;
            if (chunk == root.next) {
                newArray = new Object[index - headIndex];
                System.arraycopy(chunk.array , headIndex , newArray , 0 , newArray.length);
                headIndex = 0;
            }
            else {
                newArray = new Object[index];
                System.arraycopy(chunk.array , 0 , newArray , 0 , newArray.length);
            }
            chunk.array = newArray;
        }
        index = chunk.array.length;
        while (iterator.hasNext()) {
            if (index == chunk.array.length) {
                chunk = chunk.next = new Chunk(chunkSize , chunk);
                index = 0;
            }
            chunk.array[index++] = iterator.next();
            size++;
        }
        if (index != chunk.array.length) {
            Object[] array = new Object[index];
            System.arraycopy(chunk.array , 0 , array , 0 , index);
            chunk.array = array;
        }
        if (endChunk == null) {
            chunk.next = tail = new Chunk(chunkSize , chunk);
            tailIndex = 0;
        }
        chunk.next = endChunk;
        endChunk.fore = chunk;
    }

    @Override
    public E[] toArray(int index, int number, E[] array) {
        array = array.length < number ? (E[]) Array.newInstance(array.getClass().getComponentType() , number) : array;
        Chunk chunk = root.next;
        index += headIndex;
        while (index >= chunk.array.length) {
            index -= chunk.array.length;
            chunk = chunk.next;
        }
        int arrayIndex = 0;
        if (index >= chunk.array.length) {
            arrayIndex = chunk.array.length - index;
            System.arraycopy(chunk.array , index , array , 0 , arrayIndex);
        }
        else {
            System.arraycopy(chunk.array , index , array , 0 , number);
        }
        int endIndex = index + number;
        Chunk endChunk = chunk;
        while (endIndex >= endChunk.array.length) {
            System.arraycopy(chunk.array , 0 , array , arrayIndex , chunk.array.length);
            arrayIndex += chunk.array.length;
            endChunk = endChunk.next;
            endIndex -= endChunk.array.length;
        }
        if (chunk != endChunk) {
            System.arraycopy(endChunk.array , 0 , array , arrayIndex , endIndex);
        }
        return array;
    }

    @Override
    public E[] removeAndGet(int index, int number, E[] array) {
        array = array.length < number ? (E[]) Array.newInstance(array.getClass().getComponentType() , number) : array;
        Chunk chunk = root.next;
        index += headIndex;
        while (index >= chunk.array.length) {
            index -= chunk.array.length;
            chunk = chunk.next;
        }
        int arrayIndex = 0;
        if (index >= chunk.array.length) {
            arrayIndex = chunk.array.length - index;
            System.arraycopy(chunk.array , index , array , 0 , arrayIndex);
        }
        else {
            System.arraycopy(chunk.array , index , array , 0 , number);
        }
        int endIndex = index + number;
        Chunk endChunk = chunk;
        while (endIndex >= endChunk.array.length) {
            System.arraycopy(chunk.array , 0 , array , arrayIndex , chunk.array.length);
            arrayIndex += chunk.array.length;
            endChunk = endChunk.next;
            endIndex -= endChunk.array.length;
        }
        if (chunk == endChunk) {
            int begin = chunk == root.next ? headIndex : 0
                    ,end = endChunk == tail ? tailIndex : endChunk.array.length;
            Object[] newArray = new Object[end - begin - endIndex + index];
            int newArrayIndex = index - begin + 1;
            System.arraycopy(chunk.array , begin , newArray , 0 , newArrayIndex);
            System.arraycopy(chunk.array , endIndex , newArray , newArrayIndex , newArray.length - newArrayIndex);
            chunk.array = newArray;
            if (chunk == tail) {
                tailIndex = 0;
                chunk.next = tail = new Chunk(chunkSize , chunk);
            }
            headIndex = chunk == root.next ? 0 : headIndex;
        }
        else {
            System.arraycopy(endChunk.array , 0 , array , arrayIndex , endIndex);
            endChunk.fore = chunk.fore;
            chunk.fore.next = endChunk;
            int begin = chunk == root.next ? headIndex : 0
                    ,end = endChunk == tail ? tailIndex : endChunk.array.length;
            int foreLength = index - begin
                    ,backLength = end - endIndex;
            Object[] newArray = new Object[foreLength + backLength];
            System.arraycopy(chunk.array , begin , newArray , 0 , foreLength);
            System.arraycopy(endChunk.array , endIndex , newArray , foreLength , backLength);
            endChunk.array = newArray;
            headIndex = chunk == root.next ? 0 : headIndex;
            if (endChunk == tail) {
                tailIndex = 0;
                endChunk.next = tail = new Chunk(chunkSize , tail);
            }
        }
        size -= number;
        return array;
    }

    @Override
    public Iterator<E> iterator(int index) {
        return new ChunkChainListIterator(index);
    }

    @Override
    public Handler<E> handler(int index) {
        return new ChunkChainListHandler(index);
    }

    @Override
    public E getFirst() {
        return (E) root.next.array[headIndex];
    }

    @Override
    public E poll() {
        E value = (E) root.next.array[headIndex++];
        if (root.next == tail) {
            if (headIndex == tailIndex) {
                tailIndex = headIndex = 0;
            }
        }
        else {
            if (headIndex == root.next.array.length) {
                headIndex = 0;
                root.next = root.next.next;
                root.next.fore = root;
            }
        }
        size--;
        return value;
    }

    @Override
    public E getLast() {
        return (E) (tailIndex == 0 ? tail.fore.array[tail.fore.array.length - 1] : tail.array[tailIndex - 1]);
    }

    @Override
    public E pop() {
        E value = (E) tail.array[--tailIndex];
        if (tailIndex == 0 && root.next != tail) {
            tail = tail.fore;
            tailIndex = tail.array.length;
        }
        size--;
        return value;
    }

    @Override
    public ChunkChainList<E> copy() {
        ChunkChainList<E> list = new ChunkChainList<>(chunkSize);
        Chunk chunk = root.next
                ,destination = list.root;

        while (chunk != null) {
            destination = destination.next = new Chunk(chunk.array.length , destination);
            System.arraycopy(chunk , 0 , destination , 0 , chunk.array.length);
            chunk = chunk.next;
        }
        tail = destination;
        list.size = size;
        list.headIndex = headIndex;
        list.tailIndex = tailIndex;
        return list;
    }

    class ChunkChainListIterator implements Iterator<E> {
        Chunk chunk;
        int index;

        ChunkChainListIterator(int index) {
            index--;
            chunk = root.next;
            index += headIndex;
            while (index >= chunk.array.length) {
                chunk = chunk.next;
                index -= chunk.array.length;
            }
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return chunk != tail || index != tailIndex;
        }

        @Override
        public E next() {
            E value = (E) chunk.array[index++];
            if (index == chunk.array.length) {
                index = 0;
                chunk = chunk.next;
            }
            return value;
        }
    }

    class ChunkChainListHandler extends ChunkChainListIterator implements Handler<E> {
        ChunkChainListHandler(int index) {
            super(index);
        }

        @Override
        public boolean hasFore() {
            return chunk != root.next || index != headIndex;
        }

        @Override
        public E fore() {
            E value = (E) chunk.array[--index];
            if (index == chunk.array.length) {
                chunk = chunk.next;
                index = chunk.array.length;
            }
            return value;
        }
    }
}
