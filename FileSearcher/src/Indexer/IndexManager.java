package Indexer;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

//Responsibility: Create and update index.







public class IndexManager {

    private DirNode head;
    public IndexManager(String rootFolderPath)
    {
        IndexCreator ic = new IndexCreator();
        IndexPrinter indexPrinter = new IndexPrinter();

        head = ic.createIndex(rootFolderPath);
        indexPrinter.printIndex(head);

    }

    public DirNode getHead() {
        return head;
    }
}
