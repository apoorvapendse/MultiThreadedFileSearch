package SearchManager;

import Indexer.DirNode;
import Indexer.FileType;
import Indexer.IndexManager;
import Indexer.Node;
import ThreadPoolManager.ThreadPoolManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SearchFileContent {
    public static Map<Node, String> searchForText(IndexManager im, String key) {
        Queue<DirNode> q = new LinkedList<>();
        Map<Node, String> result = new HashMap<>();
        DirNode root = im.getHead();
        q.offer(root);

        while (!q.isEmpty()) {
            DirNode curr = q.poll();
            for (Node n : curr.getChildren()) {
                if (n.fileType == FileType.FILE) {
                    File currFile = new File(n.absolutePath);
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(currFile));
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (line.contains(key)) {
                                result.put(n, line);
                            }
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    q.offer((DirNode) n);
                }
            }
        }

        return result;
    }

    public static Map<Node, String> searchForTextMultiThreaded(IndexManager im, String key) {
        /*
         * works on the concept of work stealing queue
         * a producer thread explorers the directory structure
         * consumer threads scan the file line by line for strings to match
         */
        int maxThreads = Runtime.getRuntime().availableProcessors();
        ThreadPoolManager tpm = new ThreadPoolManager(maxThreads);
        DirNode root = im.getHead();
        return tpm.startFileContentSearching(root, key);

    }
}
