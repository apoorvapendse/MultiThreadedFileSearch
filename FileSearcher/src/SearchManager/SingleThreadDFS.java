package SearchManager;

import FIleNameMatcher.FileNameMatcher;
import Indexer.DirNode;
import Indexer.FileType;
import Indexer.IndexManager;
import Indexer.Node;

public class SingleThreadDFS {
    SingleThreadDFS(IndexManager indexManager, String key, int limit) {
        DirNode head = indexManager.getHead();
        FileNameMatcher fm = new FileNameMatcher(limit);

        performDFS(key, head, fm);
        System.out.println(fm.getMatchedFiles());
//        Trees do not have cycle, no use tracking visited Nodes
    }

    private void performDFS(String key, DirNode curr, FileNameMatcher fm) {

        for (Node child : curr.getChildren()) {
            if (child.fileType == FileType.FILE) {
                fm.match(child.filename, key, child.absolutePath);
            } else {
                performDFS(key, (DirNode) child, fm);
            }
        }

    }
}
