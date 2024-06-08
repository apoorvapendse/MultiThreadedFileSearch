package SearchManager;

import Indexer.DirNode;
import Indexer.FileType;
import Indexer.IndexManager;
import Indexer.Node;
import ThreadPoolManager.ThreadPoolManager;

import java.util.List;
import java.util.concurrent.*;

public class MultiThreadedBFS {
    public static List<String> multiThreadedBFS(IndexManager indexManager, String key) {
        ThreadPoolManager tpm = new ThreadPoolManager(4);
        List<String> results;
        DirNode root = indexManager.getHead();
        results = tpm.startBFSSearchingThreads(root, key);
        return results;
    }
}

class BFSWorker implements Runnable {
//    private DirNode node;
    private BlockingQueue<DirNode> queue;
    private List<String> results;
    private String searchKey;

    BFSWorker(BlockingQueue<DirNode> q, List<String> results, String searchKey) {
        this.queue = q;
        this.results = results;
        this.searchKey = searchKey;
    }

    public void run() {
        DirNode node = queue.poll();
        if (node == null || node.getChildren() == null) {
            return;
        }
//        System.out.println("BFSWorker running on " + node.filename);
        for (Node child : node.getChildren()) {
            // if the path is a dir add its children to queue
            if (child.fileType == FileType.DIR) {
                queue.offer((DirNode) child);
            }
            // else if file matches to search key append to results
            else {
                if (FileNameMatcher.match(child.filename, searchKey)) {
//                    System.out.println(child.absolutePath);
                    results.add(child.absolutePath);
                }
            }
        }
    }
}