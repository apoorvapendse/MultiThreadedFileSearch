package SearchManager;

import Indexer.DirNode;
import Indexer.FileType;
import Indexer.IndexManager;
import Indexer.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SingleThreadDFS {
    SingleThreadDFS(IndexManager indexManager, String key)
    {
        DirNode head = indexManager.getHead();
        List<String> matchingResults = new ArrayList<>();
        performDFS(matchingResults,key,head);
//        System.out.println(matchingResults);

//        Trees do not have cycle, no use tracking visited Nodes
    }

    private void performDFS(List<String> matchingResults,String key,DirNode curr)
    {

        for(Node child : curr.getChildren())
        {
            if(child.fileType== FileType.FILE)
            {
                FileNameMatcher.match(child.filename,key,child.absolutePath);
            }
            else{
                performDFS(matchingResults,key,(DirNode) child);
            }
        }

    }


}
