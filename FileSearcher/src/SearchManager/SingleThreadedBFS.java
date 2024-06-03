package SearchManager;

import Indexer.DirNode;
import Indexer.FileType;
import Indexer.IndexManager;
import Indexer.Node;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class SingleThreadedBFS {
    public static List<String> singleThreadedBFS(IndexManager indexManager, String key) {
        System.out.println("running single threaded BFS on search term " + key);
        List<String> results = new ArrayList<>();
        Deque<DirNode> queue = new ArrayDeque<>();
        DirNode head = indexManager.getHead();

        if (head == null) {
            return null;
        }

        queue.offer(head);
        while (!queue.isEmpty()) {
            DirNode curr = queue.poll();
            for (Node child : curr.getChildren()) {
                // if the path is a dir add its children to queue
                if (child.fileType == FileType.DIR) {
                    queue.offer((DirNode) child);
                }
                // else if file matches to search key append to results
                else {
                    if (FileNameMatcher.match(child.filename, key)) {
                        results.add(child.absolutePath);
                    }
                }
            }
        }

        return results;
    }
}
