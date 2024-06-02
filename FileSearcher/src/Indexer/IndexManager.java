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
    String filename;
    FileType fileType;
    String absolutePath;

    public FileNode(String filename, FileType fileType, String absolutePath) {
        super(filename,fileType,absolutePath);
    }
}

public class IndexManager {

    private Node head;

    public IndexManager( )
    {

    }

    //parent will always be a folder.
    public void createIndex(String rootFolderPath,DirNode parent)
    {
        File rootFolder = new File(rootFolderPath);
        DirNode currFolder = new DirNode(rootFolder.getName(),FileType.DIR,rootFolder.getAbsolutePath(),new ArrayList<>());
        if(parent == null)//basically this is the root node of our index.
        {
            this.head = currFolder;
        }
        else{
            parent.children.add(currFolder);
        }

        File[] files = rootFolder.listFiles();
        for(File file : files)
        {

            //a file will be a leaf node of the tree
            //a folder may or may not be a leaf node.
            if(file.isFile()==false)
            {
                //this is a directory
                createIndex(file.getAbsolutePath(), currFolder);
            }
            else{
                Node fileInsideParent = new FileNode(file.getName(),FileType.FILE,file.getAbsolutePath());// a file won't have children.
                currFolder.children.add(fileInsideParent);
            }
        }
    }

    public void printIndex()
    {
        if(head==null){
            System.out.println("No index");
            return;
        }

        Queue<DirNode> q = new LinkedList<>(); //we will only enqueue the DirNodes, and their FileType.FILE children will only be printed
        q.offer((DirNode) head);

        while(!q.isEmpty())
        {
            DirNode curr = q.poll();
            System.out.println(curr.filename);
            for(Node child: curr.getChildren())
            {
                if(child.fileType == FileType.DIR)
                {
                    q.offer((DirNode) child);
                }
                else{
                    System.out.print(child.filename+",");
                }
            }
            System.out.println();
        }


    }
}
