package Indexer;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;

//responsibility: Create index
public class IndexCreator {
    public DirNode createIndex(String rootFolderPath)
    {
        File rootFolder = new File(rootFolderPath);
        DirNode root = new DirNode(rootFolder.getName(),FileType.DIR ,rootFolder.getAbsolutePath(),new ArrayList<Node>());
        IndexerThread it = new IndexerThread(root);
        return root;
    }

    public void createIndexRecursively(File currFolder, DirNode currFolderNode)
    {
        File[] files = currFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    DirNode dirNode = new DirNode(file.getName(),FileType.DIR, file.getAbsolutePath(),new ArrayList<>());
                    currFolderNode.children.add(dirNode);
                    createIndexRecursively(file, dirNode);
                } else {
                    Node fileNode = new FileNode(file.getName(), FileType.FILE,file.getAbsolutePath());
                    currFolderNode.children.add(fileNode);
                }
            }
        }
    }


}
