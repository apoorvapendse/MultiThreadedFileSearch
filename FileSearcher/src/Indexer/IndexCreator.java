package Indexer;

import ThreadPoolManager.ThreadPoolManager;

import java.io.File;
import java.util.ArrayList;

//responsibility: Create index
public class IndexCreator {
    public DirNode createIndex(String rootFolderPath) {
        File rootFolder = new File(rootFolderPath);
        DirNode root = new DirNode(rootFolder.getName(), FileType.DIR, rootFolder.getAbsolutePath(), new ArrayList<Node>());
        ThreadPoolManager tpm = new ThreadPoolManager(4);
        tpm.startIndexingThreads(root);
        return root;
    }

}
