package Indexer;

import ThreadPoolManager.ThreadPoolManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

//responsibility: Create index
public class IndexCreator {
    private int fileCount = -1;

    public DirNode createIndex(String rootFolderPath, HashSet<String> ignoredFilesSet, HashSet<String> ignoredDirsSet, HashSet<String> ignoredExtSet) {
        File rootFolder = new File(rootFolderPath);
        DirNode root = new DirNode(rootFolder.getName(), FileType.DIR, rootFolder.getAbsolutePath(), new ArrayList<Node>());

        int maxThreads = Runtime.getRuntime().availableProcessors();
        ThreadPoolManager tpm = new ThreadPoolManager(maxThreads);
        tpm.startIndexingThreads(root, ignoredFilesSet, ignoredDirsSet, ignoredExtSet);

        fileCount = tpm.getFileCount();
        System.out.println("Files indexed: " + fileCount);
        return root;
    }

    public int getFileCount() {
        return fileCount;
    }
}
