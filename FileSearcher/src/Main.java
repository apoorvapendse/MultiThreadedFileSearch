import CliManager.CliManager;
import DisplayOutput.PrintFileSearchResults;
import Indexer.IndexManager;
import SearchManager.SearchManager;

import java.nio.file.Paths;
import java.util.List;


import static java.lang.Integer.parseInt;


public class Main {
    public static void main(String[] args) throws Exception {

        CliManager.parseArgs(args);
//        if(args.length < 2 || args.length > 3) {
//            System.out.println("usage: mfs /path/to/search-dir(optional) search-key limit");
//            throw new Exception("Pass proper arguments");
//        }
//        String searchTerm;
//        String indexingPath;
//        int limit;
//
//        /*
//         * when only search term is passed as arg then
//         * current working dir is indexed
//         * else the given path is indexed
//         */
//        if(args.length == 2) {
//            indexingPath = Paths.get(".").toAbsolutePath().normalize().toString();
//            searchTerm = args[0];
//            limit = parseInt(args[1]);
//        } else {
//            indexingPath = args[0];
//            searchTerm = args[1];
//            limit = parseInt(args[2]);
//        }
//        System.out.print("\033[H\033[2J"); // clears console
//
//        // indexing
//        long start = System.currentTimeMillis();
//        IndexManager im  = new IndexManager(indexingPath);
//        long end = System.currentTimeMillis();
//        System.out.println("Indexing done in: " + (end - start) + " ms");
//
//        // mBFS file searching
//        start = System.currentTimeMillis();
//        List<String> res = SearchManager.multiThreadedBFS(im, searchTerm, limit);
//        end = System.currentTimeMillis();
//        PrintFileSearchResults.printAbsolutePaths(res);
//        System.out.println("Search done in: " + (end - start) + " ms");
    }
}