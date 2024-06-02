package Indexer;

public class Node
{
    public String filename;
    public FileType fileType;
    public String absolutePath;

    public Node(String filename, FileType fileType, String absolutePath) {
        this.filename = filename;
        this.fileType = fileType;
        this.absolutePath = absolutePath;
    }
}
