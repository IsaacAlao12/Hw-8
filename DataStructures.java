import java.util.*;

public class DataStructuresHW {

    //Part 1: Hash Table with Separate Chaining //

    static class HashTable<K, V> {

        static class Node<K, V> {
            K key;
            V value;
            Node<K, V> next;

            Node(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }

        private LinkedList<Node<K, V>>[] table;
        private int size;
        private int collisionCount;

        @SuppressWarnings("unchecked")
        public HashTable(int capacity) {
            table = new LinkedList[capacity];

            for (int i = 0; i < capacity; i++) {
                table[i] = new LinkedList<>();
            }
        }

        private int hash(K key) {
            return Math.abs(key.hashCode()) % table.length;
        }

        public void put(K key, V value) {
            int index = hash(key);
            LinkedList<Node<K, V>> bucket = table[index];

            //Collision occurs if bucket already has elements
            if (!bucket.isEmpty()) {
                collisionCount++;
            }

            for (Node<K, V> node : bucket) {
                if (node.key.equals(key)) {
                    node.value = value;
                    return;
                }
            }

            bucket.add(new Node<>(key, value));
            size++;
        }

        public V get(K key) {
            int index = hash(key);
            LinkedList<Node<K, V>> bucket = table[index];

            for (Node<K, V> node : bucket) {
                if (node.key.equals(key)) {
                    return node.value;
                }
            }

            return null;
        }

        public void remove(K key) {
            int index = hash(key);
            LinkedList<Node<K, V>> bucket = table[index];

            Iterator<Node<K, V>> iterator = bucket.iterator();

            while (iterator.hasNext()) {
                Node<K, V> node = iterator.next();

                if (node.key.equals(key)) {
                    iterator.remove();
                    size--;
                    return;
                }
            }
        }

        public double getLoadFactor() {
            return (double) size / table.length;
        }

        public int getCollisionCount() {
            return collisionCount;
        }

        public void printTable() {
            for (int i = 0; i < table.length; i++) {
                System.out.print("Bucket " + i + ": ");

                for (Node<K, V> node : table[i]) {
                    System.out.print("(" + node.key + ", " + node.value + ") -> ");
                }

                System.out.println("null");
            }
        }
    }

    //Part 2: Priority Queue Exercises//
    //Find K largest elements using Min Heap
    public static List<Integer> findKLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        for (int num : nums) {
            minHeap.offer(num);

            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        return new ArrayList<>(minHeap);
    }


    //Merge K Sorted Arrays
    static class Element {
        int value;
        int arrayIndex;
        int elementIndex;

        Element(int value, int arrayIndex, int elementIndex) {
            this.value = value;
            this.arrayIndex = arrayIndex;
            this.elementIndex = elementIndex;
        }
    }

    public static List<Integer> mergeKSortedArrays(int[][] arrays) {
        List<Integer> result = new ArrayList<>();

        PriorityQueue<Element> minHeap = new PriorityQueue<>(
                Comparator.comparingInt(e -> e.value)
        );

        //Add first element of each array
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i].length > 0) {
                minHeap.offer(new Element(arrays[i][0], i, 0));
            }
        }

        while (!minHeap.isEmpty()) {
            Element current = minHeap.poll();
            result.add(current.value);

            int nextIndex = current.elementIndex + 1;

            if (nextIndex < arrays[current.arrayIndex].length) {
                minHeap.offer(new Element(
                        arrays[current.arrayIndex][nextIndex],
                        current.arrayIndex,
                        nextIndex
                ));
            }
        }

        return result;
    }

    public static void main(String[] args) {

        //HASH TABLE
        System.out.println("=== HASH TABLE ===");

        HashTable<String, Integer> hashTable = new HashTable<>(5);

        hashTable.put("Apple", 10);
        hashTable.put("Banana", 20);
        hashTable.put("Orange", 30);
        hashTable.put("Grapes", 40);
        hashTable.put("Mango", 50);

        hashTable.printTable();

        System.out.println("\nValue for Banana: " + hashTable.get("Banana"));

        System.out.println("Load Factor: " + hashTable.getLoadFactor());
        System.out.println("Collision Count: " + hashTable.getCollisionCount());

        hashTable.remove("Orange");

        System.out.println("\nAfter removing Orange:");
        hashTable.printTable();


        //PRIORITY QUEUE
        System.out.println("\n=== K LARGEST ELEMENTS ===");

        int[] nums = {10, 4, 7, 20, 15, 3, 25};
        int k = 3;

        List<Integer> largest = findKLargest(nums, k);
        System.out.println(k + " Largest Elements: " + largest);


        System.out.println("\n=== MERGE K SORTED ARRAYS ===");

        int[][] arrays = {
                {1, 4, 7},
                {2, 5, 8},
                {3, 6, 9}
        };

        List<Integer> merged = mergeKSortedArrays(arrays);

        System.out.println("Merged Array: " + merged);
    }
}
