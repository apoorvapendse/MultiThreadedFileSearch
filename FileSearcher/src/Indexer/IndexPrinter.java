package Indexer;

import java.util.LinkedList;
import java.util.Queue;

// prints all the filenames in BFS manner
public class IndexPrinter {
    public void printIndex(DirNode head) {
        if (head == null) {
            System.out.println("No index");
            return;
        }

        Queue<DirNode> q = new LinkedList<>(); //we will only enqueue the DirNodes, and their FileType.FILE children will only be printed
        q.offer(head);

        while (!q.isEmpty()) {
            DirNode curr = q.poll();
            System.out.println(curr.filename);
            for (Node child : curr.getChildren()) {
                if (child.fileType == FileType.DIR) {
                    q.offer((DirNode) child);
                } else {
                    System.out.print(child.filename + ",");
                }
            }
            System.out.println();
        }


    }
}
