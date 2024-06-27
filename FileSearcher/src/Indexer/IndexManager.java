package Indexer;

import java.util.HashSet;

//Responsibility: Create and update index.
public class IndexManager {

    private DirNode head;

    public IndexManager(String rootFolderPath, HashSet<String> ignoredFilesSet, HashSet<String> ignoredDirsSet) {
        IndexCreator ic = new IndexCreator();
        head = ic.createIndex(rootFolderPath,ignoredFilesSet,ignoredDirsSet);
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
