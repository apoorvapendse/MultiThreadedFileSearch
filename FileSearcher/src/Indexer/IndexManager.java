package Indexer;

//Responsibility: Create and update index.
public class IndexManager {

    private DirNode head;

    public IndexManager(String rootFolderPath) {
        IndexCreator ic = new IndexCreator();
        head = ic.createIndex(rootFolderPath);
    }

    public IndexManager(DirNode root) {
        this.head = root;
    }

    public DirNode getHead() {
        return head;
    }

    public void setHead(DirNode root) {
        this.head = root;
    }
}
