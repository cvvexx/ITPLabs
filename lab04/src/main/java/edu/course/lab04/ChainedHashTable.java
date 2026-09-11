package edu.course.lab04;

public class ChainedHashTable<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private static final class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Node<K, V>[] buckets;
    private int size;

    public ChainedHashTable() {
        this(DEFAULT_CAPACITY);
    }

    public ChainedHashTable(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("initialCapacity must be positive");
        }
        buckets = createBuckets(initialCapacity);
    }

    public V put(K key, V value) {
        requireKey(key);
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
        int index = indexFor(key, buckets.length);
        for (Node<K, V> node = buckets[index]; node != null; node = node.next) {
            if (node.key.equals(key)) {
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
        }
        buckets[index] = new Node<>(key, value, buckets[index]);
        size++;
        if (size > buckets.length * LOAD_FACTOR) {
            resize();
        }
        return null;
    }

    public V get(K key) {
        Node<K, V> node = findNode(key);
        return node == null ? null : node.value;
    }

    public V remove(K key) {
        requireKey(key);
        int index = indexFor(key, buckets.length);
        Node<K, V> previous = null;
        for (Node<K, V> node = buckets[index]; node != null; node = node.next) {
            if (node.key.equals(key)) {
                if (previous == null) {
                    buckets[index] = node.next;
                } else {
                    previous.next = node.next;
                }
                size--;
                return node.value;
            }
            previous = node;
        }
        return null;
    }

    public boolean containsKey(K key) {
        return findNode(key) != null;
    }

    public int size() {
        return size;
    }

    int capacity() {
        return buckets.length;
    }

    private Node<K, V> findNode(K key) {
        requireKey(key);
        for (Node<K, V> node = buckets[indexFor(key, buckets.length)]; node != null; node = node.next) {
            if (node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }

    private void resize() {
        Node<K, V>[] newBuckets = createBuckets(buckets.length * 2);
        for (Node<K, V> head : buckets) {
            Node<K, V> node = head;
            while (node != null) {
                Node<K, V> next = node.next;
                int index = indexFor(node.key, newBuckets.length);
                node.next = newBuckets[index];
                newBuckets[index] = node;
                node = next;
            }
        }
        buckets = newBuckets;
    }

    private static int indexFor(Object key, int length) {
        return Math.floorMod(key.hashCode(), length);
    }

    private static void requireKey(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
    }


    private static <K, V> Node<K, V>[] createBuckets(int capacity) {
        return (Node<K, V>[]) new Node[capacity];
    }
}
