package OrionLuouo.Craft.data.container.map;

import OrionLuouo.Craft.data.Iterator;
import OrionLuouo.Craft.data.container.Map;
import OrionLuouo.Craft.data.container.collection.Set;

import java.util.LinkedList;
import java.util.PrimitiveIterator;

public class DictionaryMap<V> implements Map<CharSequence , V> {
    final Node<V> root;
    int size;

    static class Node<V> {
        char character;
        Node<V> brother, child;
        V value;

        Node() {
        }

        Node(char character) {
            this.character = character;
        }

        Node(char character, Node<V> brother, Node<V> child , V value) {
            this.character = character;
            this.brother = brother;
            this.child = child;
            this.value = value;
        }
    }

    static <V> Node<V> getNode(Node<V> node, char character) {
        if (node.child == null) {
            return null;
        }
        node = node.child;
        while (node.character != character && node != null) {
            node = node.brother;
        }
        return node;
    }

    static <V> Node<V> buildNode(Node<V> node , char character) {
        if (node.child == null) {
            return node.child = new Node<>(character);
        }
        node = node.child;
        while (node.character != character) {
            if (node.brother == null) {
                return node.brother = new Node<>(character);
            }
            node = node.brother;
        }
        return node;
    }

    Node<V> getNode(CharSequence charSequence) {
        PrimitiveIterator.OfInt iterator = charSequence.chars().iterator();
        Node node = root;
        for (int index = 0 , end = charSequence.length() ; index < end ; ) {
            node = getNode(node , charSequence.charAt(index++));
            if (node == null) {
                return null;
            }
        }
        return node;
    }

    Node<V> buildNode(CharSequence charSequence) {
        PrimitiveIterator.OfInt iterator = charSequence.chars().iterator();
        Node node = root;
        for (int index = 0 , end = charSequence.length() ; index < end ; ) {
            node = buildNode(node , charSequence.charAt(index++));
        }
        return node;
    }

    public DictionaryMap() {
        root = new Node<>();
    }

    @Override
    public V get(CharSequence key) {
        Node<V> node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(CharSequence key) {
        Node<V> node = getNode(key);
        return node != null && node.value != null;
    }

    @Override
    public V remove(CharSequence key) {
        Node<V> node = root;
        int index = 0;
        for (int end = key.length() - 1 ; index < end; ) {
            node = getNode(node , key.charAt(index++));
            if (node == null) {
                return null;
            }
        }
        char character = key.charAt(index);
        if (node.child == null || node.child.character > character) {
            return null;
        }
        if (node.child.character == character) {
            Node<V> cache = node.child;
            node.child = node.child.brother;
            return cache.value;
        }
        node = node.child;
        while (node.brother != null) {
            if (node.brother.character == character) {
                Node<V> cache = node.brother;
                node.brother = node.brother.brother;
                return cache.value;
            }
            if (node.brother.character > character) {
                return null;
            }
            node = node.brother;
        }
        return null;
    }

    class ValueIterator implements Iterator<V> {
        Node<V> node = root;
        LinkedList<Node<V>> stack = new LinkedList<>();

        ValueIterator() {
            next();
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public V next() {
            V result = node.value;
            do {
                while (node.child != null) {
                    stack.push(node);
                    node = node.child;
                    if (node.value != null) {
                        return result;
                    }
                }
                while ((node = node.brother) == null) {
                    if (stack.isEmpty()) {
                        return result;
                    }
                    node = stack.pop();
                }
                if (node.value != null) {
                    return result;
                }
            } while (true);
        }
    }


    class EntryIterator implements Iterator<Entry<CharSequence , V>> {
        Node<V> node = root;
        LinkedList<Node<V>> stack = new LinkedList<>();
        StringBuilder builder = new StringBuilder();

        void NodeIterator() {
            next();
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public Entry<CharSequence, V> next() {
            Entry<CharSequence , V> entry = new Entry<>(builder.toString() , node.value);
            do {
                while (node.child != null) {
                    builder.append(node.character);
                    stack.push(node);
                    node = node.child;
                    if (node.value != null) {
                        return entry;
                    }
                }
                while ((node = node.brother) == null) {
                    if (stack.isEmpty()) {
                        return entry;
                    }
                    builder.deleteCharAt(builder.length() - 1);
                    node = stack.pop();
                }
                if (node.value != null) {
                    return entry;
                }
            } while (true);
        }
    }

    @Override
    public Iterator<V> valueIterator() {
        return new ValueIterator();
    }

    @Override
    public void add(Entry<CharSequence, V> element) {
        Node<V> node = buildNode(element.key());
        if (node.value == null) {
            size++;
        }
        node.value = element.value();
    }

    @Override
    public void add(Iterator<Entry<CharSequence, V>> iterator) {
        while (iterator.hasNext()) {
            add(iterator.next());
        }
    }

    @Override
    public void add(int number, Iterator<Entry<CharSequence, V>> iterator) {
        for (int i = 0; i < number && iterator.hasNext() ; i++) {
            add(iterator.next());
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Entry<CharSequence, V>[] toArray(Entry<CharSequence, V>[] array) {
        array = array.length >= size ? array : new Entry[size];
        Iterator<Entry<CharSequence , V>> iterator = new EntryIterator();
        for (int index = 0 ; index < size ; ) {
            array[index++] = iterator.next();
        }
        return array;
    }

    @Override
    public Iterator<Entry<CharSequence, V>> iterator() {
        return new EntryIterator();
    }

    @Override
    public Set<Entry<CharSequence, V>> copy() {
        Node<V> node , destinationNode;
        node = root;

        return null;
    }
}