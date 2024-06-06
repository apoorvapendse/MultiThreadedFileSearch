package ThreadSafeQueue;

import java.util.LinkedList;
import java.util.List;

public class ThreadSafeQueue<T> {
    // T will be DirNode in our case.
    private final List<T> arr;
    private final Object lock;
    private final double maxSize = 1e5;

    public ThreadSafeQueue() {
        arr = new LinkedList<>();
        this.lock = new Object();
    }

    public void offer(T obj,int timeoutLimit) {
        long endTime = System.currentTimeMillis() + timeoutLimit;

        synchronized (lock) {
            while (arr.size() == maxSize) {
                if(endTime - System.currentTimeMillis()<=0)return ;
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

    public T poll(int timeoutLimit) {
        long endTime = System.currentTimeMillis() + timeoutLimit;
        synchronized (lock) {
            while (arr.isEmpty()) {
                try {
                    if(endTime - System.currentTimeMillis()<=0)return null;
                    lock.wait();
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
