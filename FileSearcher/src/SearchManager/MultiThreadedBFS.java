package SearchManager;

import Indexer.DirNode;
import Indexer.FileType;
import Indexer.IndexManager;
import Indexer.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class MultiThreadedBFS {
    public static List<String> multiThreadedBFS(IndexManager indexManager, String key) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        BlockingQueue<DirNode> queue = new LinkedBlockingQueue<>();
        ArrayList<String> results = new ArrayList<>();
        DirNode head = indexManager.getHead();

        queue.offer(head);

        while(!queue.isEmpty()) {
            DirNode curr = queue.poll();
            executor.execute(new BFSWorker(curr, queue, results, key));
        }
        executor.shutdown();
        return results;
    }
}

class BFSWorker implements Runnable{
    private DirNode node;
    private BlockingQueue<DirNode> queue;
    private ArrayList<String> results;
    private String searchKey;
    BFSWorker(DirNode node, BlockingQueue<DirNode> q, ArrayList<String> results, String searchKey) {
        this.node = node;
        this.queue = q;
        this.results = results;
        this.searchKey = searchKey;
    }

    public void run() {
        if(node == null || node.getChildren() == null) {
            return;
        }
        for (Node child : node.getChildren()) {
            // if the path is a dir add its children to queue
            if (child.fileType == FileType.DIR) {
                queue.offer((DirNode) child);
            }
            // else if file matches to search key append to results
            else {
                if (FileNameMatcher.match(child.filename, searchKey)) {
                    results.add(child.absolutePath);
                }
            }
        }
    }
}