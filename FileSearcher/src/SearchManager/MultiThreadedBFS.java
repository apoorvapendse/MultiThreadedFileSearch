package SearchManager;

import Indexer.DirNode;
import Indexer.IndexManager;
import ThreadPoolManager.ThreadPoolManager;

import java.util.List;

public class MultiThreadedBFS {
    public static List<String> multiThreadedBFS(IndexManager indexManager, String key, int limit) {
        int maxThreads = Runtime.getRuntime().availableProcessors();
        ThreadPoolManager tpm = new ThreadPoolManager(maxThreads);
        List<String> results;
        DirNode root = indexManager.getHead();
        results = tpm.startBFSSearchingThreads(root, key, limit);
        return results;
    }
}
