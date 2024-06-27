package Indexer;

import ThreadPoolManager.ThreadPoolManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

//responsibility: Create index
public class IndexCreator {
    public DirNode createIndex(String rootFolderPath, HashSet<String> ignoredFilesSet, HashSet<String> ignoredDirsSet) {
        File rootFolder = new File(rootFolderPath);
        DirNode root = new DirNode(rootFolder.getName(), FileType.DIR, rootFolder.getAbsolutePath(), new ArrayList<Node>());
        int maxThreads = Runtime.getRuntime().availableProcessors();
        ThreadPoolManager tpm = new ThreadPoolManager(maxThreads);
        tpm.startIndexingThreads(root,ignoredFilesSet,ignoredDirsSet);
        System.out.println("Files indexed: " + tpm.getFileCount());
        return root;
    }

}
