package SearchManager;

import Indexer.DirNode;
import Indexer.IndexManager;
import ThreadPoolManager.ThreadPoolManager;

import java.util.List;

public class MultiThreadedBFS {
    public static List<String> multiThreadedBFS(IndexManager indexManager, String key, int limit) {
        DirNode root = indexManager.getHead();

        int maxThreads = Runtime.getRuntime().availableProcessors();
        ThreadPoolManager tpm = new ThreadPoolManager(maxThreads);
        List<String> results = tpm.startBFSSearchingThreads(root, key, limit);

        System.out.println("Files searched: " + tpm.getFileCount());
        return results;
    }
}
