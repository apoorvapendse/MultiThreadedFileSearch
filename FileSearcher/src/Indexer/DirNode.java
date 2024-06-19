package Indexer;

import java.io.Serializable;
import java.util.List;

public class DirNode extends Node implements Serializable {
    private List<Node> children;

    public DirNode(String filename, FileType fileType, String absolutePath, List<Node> children) {
        super(filename, fileType, absolutePath);
        this.children = children;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void addChild(Node n) {
        this.children.add(n);
    }

}

