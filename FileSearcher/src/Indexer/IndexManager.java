package Indexer;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

//Responsibility: Create and update index.

 class Node
{
    String filename;
    FileType fileType;
    String absolutePath;

    public Node(String filename, FileType fileType, String absolutePath) {
        this.filename = filename;
        this.fileType = fileType;
        this.absolutePath = absolutePath;
    }
}


 class DirNode extends Node{
    List<Node> children;
    public DirNode(String filename, FileType fileType, String absolutePath, List<Node> children) {
        super(filename,fileType,absolutePath);
        this.children = children;
    }
    public List<Node> getChildren() {
        return children;
    }
}

class FileNode extends Node{
    public FileNode(String filename, FileType fileType, String absolutePath) {
        super(filename,fileType,absolutePath);
    }
}
public class IndexManager {

    private DirNode head;
    public IndexManager(String rootFolderPath)
    {
        IndexCreator ic = new IndexCreator();
        IndexPrinter indexPrinter = new IndexPrinter();

        head = ic.createIndex(rootFolderPath);
        indexPrinter.printIndex(head);

    }

}
