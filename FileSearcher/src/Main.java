import Indexer.IndexManager;
import SearchManager.SearchManager;

import java.nio.file.Path;
import java.util.List;


public class Main {
    public static void main(String[] args) throws Exception {
        if(args.length < 1) {
            throw new Exception("Pass proper arguments");
        }

        String searchTerm = args[0];
        IndexManager im = new IndexManager(Path.of("FileSearcher/search").toAbsolutePath().toString());

//        List<String> res = SearchManager.singleThreadedBFS(im, searchTerm);
//        System.out.println(res);
        Thread.sleep(500);
        System.out.println("Single Threaded DFS");
        SearchManager.singleThreadedDFS(im,"hello.txt");

//        List<String> res = SearchManager.multiThreadedBFS(im, "hello.txt");
//        System.out.println(res);
    }

}