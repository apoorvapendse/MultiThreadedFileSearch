package SearchManager;

import Indexer.FileType;
import Indexer.IndexManager;
import Indexer.DirNode;
import Indexer.Node;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Deque;

// implements all the search algorithms
public class SearchManager {
    public SearchManager()
    {
        System.out.println(" Search Manager Constructor");
    }

    public static List<String> singleThreadedBFS(IndexManager indexManager, String key) {
        System.out.println("running single threaded BFS on search term " + key);
        List<String> results = new ArrayList<String>();
        Deque<DirNode> queue = new ArrayDeque<>();
        DirNode head = indexManager.getHead();

        if(head == null) {
            return null;
        }

        queue.offer(head);
        while(!queue.isEmpty()) {
            DirNode curr = queue.poll();
            for(Node child: curr.getChildren()) {
                if(child.fileType == FileType.DIR) {
                    queue.offer((DirNode) child);
                }
                else {
                    if(FileNameMatcher.match(child.filename, key)) {
                        results.add(child.absolutePath);
                    }
                }
            }
        }

        return results;
    }
}
