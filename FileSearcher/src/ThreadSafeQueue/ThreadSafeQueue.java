package ThreadSafeQueue;


import java.util.LinkedList;
import java.util.List;

public class ThreadSafeQueue<T>  {
    //T will be DirNode in our case.
    private List<T> arr;
    private final Object lock1;
    private final Object lock2;
    private double maxSize = 1e5;
    public ThreadSafeQueue()
    {
        arr = new LinkedList<>();
        this.lock1 = new Object();
        this.lock2 = new Object();
    }

    //synchronized keyword uses this as the lock.
    public void offer(T obj) {
        synchronized (lock1) {
            while (arr.size() == maxSize) {
                try {
                    lock1.wait();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
            synchronized (lock2) {
                arr.add(obj);
                lock2.notifyAll();
            }
        }
    }

    public T poll() {
        synchronized (lock2) {
            while (arr.isEmpty()) {
                try {
                    lock2.wait();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
            T item;
            synchronized (lock1) {
                item = arr.remove(0);
                lock1.notifyAll();
            }
            return item;
        }
    }


}
