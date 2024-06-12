package FIleNameMatcher;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

public class BoundedPriorityQueue<T> implements Iterable<T> {
    PriorityQueue<T> minHeap;
    int maxSize;

    BoundedPriorityQueue(int maxSize, Comparator<T> comp) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("size must be greater than 0");
        }
        this.maxSize = maxSize;
        // Comparator is reversed as least relevant element is kept at the start of the queue
        minHeap = new PriorityQueue<>(maxSize, comp.reversed());
    }

    public synchronized void offer(T element) {
        // if size is less than maxSize then remove the least relevant element
        if (size() == maxSize) {
            // compare element with the least relevant element in the queue
            // to check if its worth queuing it
            T leastRelevantElem = minHeap.peek();
            if (minHeap.comparator().compare(element, leastRelevantElem) < 0) {
                return;
            }
            minHeap.poll();
        }
        minHeap.offer(element);
    }

    public int size() {
        return minHeap.size();
    }

    public boolean isEmpty() {
        return minHeap.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return minHeap.iterator();
    }
}
