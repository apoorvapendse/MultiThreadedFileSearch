package SearchManager;

import Indexer.IndexManager;

import java.util.List;

// implements all the search algorithms
public class SearchManager {
    public SearchManager()
    {
        System.out.println(" Search Manager Constructor");
    }

    public static List<String> singleThreadedBFS(IndexManager im, String key) {
        return SingleThreadedBFS.singleThreadedBFS(im, key);
    }

    public static void singleThreadedDFS(IndexManager im, String key)
    {
        new SingleThreadDFS(im,key);
    }
}
