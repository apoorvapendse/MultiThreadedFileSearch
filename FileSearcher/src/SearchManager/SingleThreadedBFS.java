package SearchManager;

import Indexer.DirNode;
import Indexer.FileType;
import Indexer.IndexManager;
import Indexer.Node;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class SingleThreadedBFS {
    public static List<String> singleThreadedBFS(IndexManager indexManager, String key, int limit) {
        System.out.println("running single threaded BFS on search term " + key);
        Deque<DirNode> queue = new ArrayDeque<>();
        DirNode head = indexManager.getHead();
        FileNameMatcher fm = new FileNameMatcher(limit);

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
                    fm.match(child.filename, key, child.absolutePath);
                }
            }
        }

        List<String> results = fm.getMatchedFiles();
        return results;

    }
}
