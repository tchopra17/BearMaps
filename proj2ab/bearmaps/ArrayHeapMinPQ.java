package bearmaps;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    HashMap<T, Location> h;
    private ArrayList<Node> minHeap;
    private int size;

    public ArrayHeapMinPQ() {
        minHeap = new ArrayList<>();
        Node entry = new Node(null, 0);
        h = new HashMap<>();
        minHeap.add(entry);
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException();
        }
        Node added = new Node(item, priority);
        minHeap.add(added);
        size++;
        Location l = new Location(priority, size());
        h.put(item, l);
        checkPlacement(size(), priority, added);
    }

    @Override
    public boolean contains(T item) {
        return h.containsKey(item);
    }

    @Override
    public T getSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        return minHeap.get(1).item;
    }

    @Override
    public T removeSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException();
        } else if (size() > 1) {
            Node removed = minHeap.get(1);
            minHeap.set(1, minHeap.remove(size()));
            size--;
            h.remove(removed.item);
            checkPlacement(1, minHeap.get(1).priority, minHeap.get(1));
            return removed.item;
        } else {
            Node removed = minHeap.get(1);
            h.remove(removed.item);
            size--;
            return minHeap.remove(1).item;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void changePriority(T item, double priority) {
        int place = h.get(item).place;
        h.remove(item);
        Location l = new Location(priority, place);
        h.put(item, l);
        minHeap.get(place).priority = priority;
        checkPlacement(place, priority, minHeap.get(place));
    }

    private void checkPlacement(int place, double priority, Node toCheck) {
        Node parent = minHeap.get(place / 2);
        if (parent.priority > priority) {
            swim(parent, place, priority, toCheck);
        }
        if (size() > (place * 2) + 1) {
            Node left = minHeap.get((place * 2));
            Node right = minHeap.get((place * 2) + 1);
            if (left.priority <= right.priority) {
                sink(place, place * 2, priority, toCheck);
            } else {
                sink(place, (place * 2) + 1, priority, toCheck);
            }
        } else if (size() >= (place * 2)) {
            sink(place, (place * 2), priority, toCheck);
        }
    }

    private void sink(int place, int childPlace, double priority, Node toSink) {
        Node child = minHeap.get(childPlace);
        if (priority > child.priority) {
            Node temp = child;
            h.remove(temp.item);
            h.remove(toSink.item);
            minHeap.set(childPlace, toSink);
            Location l = new Location(priority, childPlace);
            h.put(toSink.item, l);
            minHeap.set(place, temp);
            Location l2 = new Location(temp.priority, place);
            h.put(temp.item, l2);
            checkPlacement(childPlace, priority, toSink);
        }
    }

    private void swim(Node parent, int place, double priority, Node toSwim) {
        Node temp = parent;
        h.remove(parent.item);
        h.remove(toSwim.item);
        minHeap.set(place / 2, toSwim);
        Location l = new Location(priority, place / 2);
        h.put(toSwim.item, l);
        minHeap.set(place, temp);
        Location l2 = new Location(temp.priority, place);
        h.put(temp.item, l2);
        checkPlacement(place / 2, priority, toSwim);
    }

    private class Location {
        double priority;
        int place;

        Location(double priority, int place) {
            this.place = place;
            this.priority = priority;
        }
    }

    private class Node {
        T item;
        double priority;

        Node(T i, double p) {
            item = i;
            priority = p;
        }
    }

    public static void main(String[] args) {

    }
}
