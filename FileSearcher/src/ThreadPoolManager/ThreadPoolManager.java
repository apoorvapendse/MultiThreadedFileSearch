package ThreadPoolManager;

import Indexer.DirNode;
import Indexer.FileNode;
import Indexer.FileType;
import Indexer.Node;
import ThreadSafeQueue.ThreadSafeQueue;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class ThreadPoolManager {
    ThreadSafeQueue<DirNode> q = new ThreadSafeQueue<DirNode>();
    int maxThreads;
    int activeThreads;
    public ThreadPoolManager(int n)
    {
        this.maxThreads = n;
    }

    public void startThreadPool (DirNode root)  {
        q.offer(root);
        ArrayList<Thread> threadList = new ArrayList<>();
        while(threadList.size() <= maxThreads)
        {
            //spawn threads continuously
            Thread t = new Thread(()->{
                DirNode curr = q.poll();
                System.out.println(curr.filename);
                File currDir = new File(curr.absolutePath);
                try{
                    for(File file  : Objects.requireNonNull(currDir.listFiles()))
                    {

                        if(file.isDirectory())
                        {
                            DirNode subdirectory = new DirNode(file.getName(), FileType.DIR, file.getAbsolutePath(),new ArrayList<>());
                            q.offer(subdirectory);

                            curr.addChild(subdirectory);
                        }
                        else{
                            FileNode subfile = new FileNode(file.getName(),FileType.FILE, file.getAbsolutePath());
                            curr.addChild(subfile);
                        }
                    }

                    System.out.println(Thread.currentThread().getName()+" is going off");
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }

            });
            threadList.add(t);
            t.start();


        }

        for(Thread childThread : threadList)
        {
            try{
                childThread.join();

            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }




    }

}
