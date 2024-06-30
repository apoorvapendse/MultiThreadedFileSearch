package Indexer;

import java.util.HashSet;

//Responsibility: Create and update index.
public class IndexManager {

    private DirNode head;
    private int fileCount;

    // use this constructor when index needs to created from scratch with ignored stuff
    public IndexManager(String rootFolderPath, HashSet<String> ignoredFilesSet, HashSet<String> ignoredDirsSet, HashSet<String> ignoredExtSet) {
        IndexCreator ic = new IndexCreator();
        head = ic.createIndex(rootFolderPath, ignoredFilesSet, ignoredDirsSet, ignoredExtSet);
        fileCount = ic.getFileCount();
    }

    // this constructor is used then index is deserialized from cache
    public IndexManager(DirNode root) {
        this.head = root;
    }

    // use this constructor when no files are required to be ignored
    // added for backwards compatibility
    public IndexManager(String rootFolderPath) {
        IndexCreator ic = new IndexCreator();
        head = ic.createIndex(rootFolderPath, new HashSet<>(), new HashSet<>(), new HashSet<>());
        fileCount = ic.getFileCount();
    }

    public DirNode getHead() {
        return head;
    }

    public void setHead(DirNode root) {
        this.head = root;
    }

    public int getFileCount() {
        return fileCount;
    }
}
