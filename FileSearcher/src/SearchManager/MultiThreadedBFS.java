package SearchManager;

import Indexer.DirNode;
import Indexer.FileType;
import Indexer.IndexManager;
import Indexer.Node;

import  java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MultiThreadedBFS {
    public static List<String> multiThreadedBFS(IndexManager indexManager, String key) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
        BlockingQueue<DirNode> queue = new LinkedBlockingQueue<>();
//        ArrayList<String> results = new ArrayList<>();
        List<String> results = Collections.synchronizedList(new ArrayList<>());
        DirNode head = indexManager.getHead();

        queue.offer(head);

        do {
            if (!queue.isEmpty()) {
                executor.execute(new BFSWorker(queue, results, key));
            }
            System.out.println("");
        } while (executor.getActiveCount() > 0 || !queue.isEmpty());
        executor.shutdown();
        try {
            System.out.println(executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS));
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }
}

class BFSWorker implements Runnable {
    private DirNode node;
    private BlockingQueue<DirNode> queue;
    private List<String> results;
    private String searchKey;

    BFSWorker(BlockingQueue<DirNode> q, List<String> results, String searchKey) {
        this.queue = q;
        this.node = queue.poll();
        this.results = results;
        this.searchKey = searchKey;
    }

    public void run() {
        if (node == null || node.getChildren() == null) {
            return;
        }
        System.out.println("BFSWorker running on " + node.filename);
        for (Node child : node.getChildren()) {
            // if the path is a dir add its children to queue
            if (child.fileType == FileType.DIR) {
                queue.offer((DirNode) child);
            }
            // else if file matches to search key append to results
            else {
                if (FileNameMatcher.match(child.filename, searchKey)) {
                    System.out.println(child.absolutePath);
                    results.add(child.absolutePath);
                }
            }
        }
    }
}