package SearchManager;

import Indexer.DirNode;
import Indexer.FileType;
import Indexer.Node;
import ThreadSafeQueue.ThreadSafeQueue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SearchWithinFiles {
    DirNode root;
    String key;
    List<String> result = new ArrayList<>();
    SearchWithinFiles(DirNode root,String text)
    {
        this.root = root;
        this.key = text;
        this.searchForText();
    }

    private void searchForText()
    {
        Queue<DirNode> q = new LinkedList<>();
        q.offer(root);

        while(q.isEmpty() == false)
        {
            DirNode curr = q.poll();
            for(Node n : curr.getChildren())
            {
                if(n.fileType == FileType.FILE)
                {
                    File currFile = new File(n.absolutePath);
                    try{
                        BufferedReader br = new BufferedReader(new FileReader(currFile));
                        String line;
                        while((line = br.readLine()) != null)
                        {
                            if(line.contains(key))
                            {
                                result.add(n.absolutePath+":"+line);
                            }
                        }
                    }
                    catch(IOException e)
                    {
                        System.out.println(e.getMessage());
                    }
                }
                else
                {
                    q.offer((DirNode) n);
                }
            }



        }

        System.out.println(result);

    }


}
