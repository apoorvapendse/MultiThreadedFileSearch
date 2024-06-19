package SearchManager;

import Indexer.IndexManager;
import Indexer.Node;

import java.util.List;
import java.util.Map;

// common class to call all the search algorithms
public class SearchManager {
    public SearchManager() {
        System.out.println(" Search Manager Constructor");
    }

    public static List<String> singleThreadedBFS(IndexManager im, String key, int limit) {
        return SingleThreadedBFS.singleThreadedBFS(im, key, limit);
    }

    public static List<String> multiThreadedBFS(IndexManager im, String key, int limit) {
        return MultiThreadedBFS.multiThreadedBFS(im, key, limit);
    }

    public static void singleThreadedDFS(IndexManager im, String key, int limit) {
        new SingleThreadDFS(im, key, limit);
    }

    public static Map<Node, String> searchWithinFiles(IndexManager im, String key) {
        return SearchFileContent.searchForText(im, key);
    }
}
