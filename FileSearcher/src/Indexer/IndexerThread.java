package Indexer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IndexerThread implements  Runnable{
    DirNode root;

    IndexerThread(DirNode root)
    {
        this.root = root;
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run()
    {
        File currFile = new File(root.absolutePath);
        System.out.println(currFile.getName());
        try
        {
            for(File file : Objects.requireNonNull(currFile.listFiles()))
            {
                if(file.isDirectory())
                {
                    DirNode subdirectory = new DirNode(file.getName(),FileType.DIR,file.getAbsolutePath(),new ArrayList<>());
                    IndexerThread it = new IndexerThread(subdirectory);
                    root.children.add(subdirectory);
                }
                else{
                    root.children.add(new FileNode(file.getName(),FileType.FILE,file.getAbsolutePath()));
                }
            }
        }
        catch(NullPointerException e)
        {
            //Directory is basically empty
        }

    }
}
