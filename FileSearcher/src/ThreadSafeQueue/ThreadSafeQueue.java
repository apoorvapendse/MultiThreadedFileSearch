package ThreadSafeQueue;

import java.util.LinkedList;
import java.util.List;

public class ThreadSafeQueue<T> {
    // T will be DirNode in our case.
    private final List<T> arr;
    private final Object lock;
    private final double maxSize = 1e6;

    public ThreadSafeQueue() {
        arr = new LinkedList<>();
        this.lock = new Object();
    }


    public void offer(T obj) {
        synchronized (lock) {
            while (arr.size() == maxSize) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
            arr.add(obj);
            lock.notifyAll();
        }
    }

    public T poll() {
        long endTime = System.currentTimeMillis() + 100;
        synchronized (lock) {
            while (arr.isEmpty()) {
                if (System.currentTimeMillis() >= endTime) return null;
                try {
                    lock.wait(endTime - System.currentTimeMillis());
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
            T item = arr.remove(0);
            lock.notifyAll();
            return item;
        }
    }
}
