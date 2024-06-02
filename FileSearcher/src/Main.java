import Indexer.IndexManager;
import SearchManager.SearchManager;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
//        SearchManager sm = new SearchManager();
        IndexManager im = new IndexManager();
        im.createIndex(Path.of("FileSearcher/search").toAbsolutePath().toString(),null);
        im.printIndex();
    }

}