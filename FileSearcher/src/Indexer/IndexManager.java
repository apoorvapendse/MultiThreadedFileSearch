package Indexer;

//Responsibility: Create and update index.
public class IndexManager {

    private DirNode head;
    public IndexManager(String rootFolderPath)
    {
        IndexCreator ic = new IndexCreator();
//        IndexPrinter indexPrinter = new IndexPrinter();

        head = ic.createIndex(rootFolderPath);
//        indexPrinter.printIndex(head);

    }

    public DirNode getHead() {
        return head;
    }
}
