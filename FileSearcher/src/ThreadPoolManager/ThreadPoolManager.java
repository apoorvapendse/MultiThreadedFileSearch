package ThreadPoolManager;

import Indexer.DirNode;
import Indexer.FileNode;
import Indexer.FileType;
import Indexer.Node;
import FIleNameMatcher.FileNameMatcher;
import ThreadSafeQueue.ThreadSafeQueue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ThreadPoolManager {
    private final ThreadSafeQueue<DirNode> q;
    private final int maxThreads;

    public ThreadPoolManager(int n) {
        this.maxThreads = n;
        this.q = new ThreadSafeQueue<DirNode>();
    }

    public void startIndexingThreads(DirNode root) {
        q.offer(root);
        List<Thread> threadList = new ArrayList<>();

        for (int i = 0; i < maxThreads; i++) {
            Thread t = new Thread(() -> {
                // Indexing Worker
                while (true) {
                    DirNode curr = q.poll();
                    if (curr == null) {
                        System.out.println(Thread.currentThread().getName() + " has stopped execution");
                        return;
                    }

                    File currDir = new File(curr.absolutePath);
                    try {
                        for (File file : Objects.requireNonNull(currDir.listFiles())) {
                            if (file.isDirectory()) {
                                if(file.getName().equals("node_modules") || file.getName().equals(".git"))continue;
                                DirNode subdirectory = new DirNode(file.getName(), FileType.DIR, file.getAbsolutePath(), new ArrayList<>());
                                curr.addChild(subdirectory);
                                q.offer(subdirectory);
                            } else if (file.isFile()) {
                                FileNode subfile = new FileNode(file.getName(), FileType.FILE, file.getAbsolutePath());
                                curr.addChild(subfile);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            });

            threadList.add(t);
            t.start();
        }
        System.out.println(threadList.size());
        // Wait for all threads to finish
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("All threads have finished execution");
    }

    public List<String> startBFSSearchingThreads(DirNode root, String searchKey, int limit) {
        q.offer(root);
        List<Thread> threadList = new ArrayList<>(maxThreads);
        FileNameMatcher fm = new FileNameMatcher(limit);

        for (int i = 0; i < maxThreads; i++) {
            Thread t = new Thread(() -> {
                // BFS Worker
                while (true) {
                    DirNode curr = q.poll();
                    if (curr == null || curr.getChildren() == null) {
                        System.out.println(Thread.currentThread().getName() + " committed suicide");
                        return;
                    }
                    for (Node child : curr.getChildren()) {
                        // if the path is a dir add its children to queue
                        if (child.fileType == FileType.DIR) {
                            q.offer((DirNode) child);
                        }
                        // else if file matches to search key append to results
                        else {
                            fm.match(child.filename, searchKey, child.absolutePath);
                        }
                    }
                }
            });

            threadList.add(t);
            t.start();
        }

        // Wait for all threads to finish
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("mBFS finished");
        return fm.getMatchedFiles();
    }

    public Map<Node, String> startFileContentSearching(DirNode root, String searchKey) {
        // fileQueue is used for file nodes that will be searched by consumers
        ThreadSafeQueue<FileNode> fileQueue = new ThreadSafeQueue<FileNode>();

        this.startProducerThread(root, fileQueue);
        return startConsumerThread(fileQueue, searchKey);
    }

    private void startProducerThread(DirNode root, ThreadSafeQueue<FileNode> fileQueue) {
        // bfsQueue is used for BFS
        Queue<DirNode> bfsQueue = new LinkedList<>();

        Thread producer = new Thread(() -> {
            bfsQueue.offer(root);
            while (!bfsQueue.isEmpty()) {
                DirNode curr = bfsQueue.poll();
                for (Node child : curr.getChildren()) {
                    // if the path is a dir add its children to queue
                    if (child.fileType == FileType.DIR) {
                        bfsQueue.offer((DirNode) child);
                    }
                    // else if it's a file offer it to this.queue for consumers
                    else {
                        fileQueue.offer((FileNode) child);
                    }
                }
            }
        });
        producer.start();
    }

    private Map<Node, String> startConsumerThread(ThreadSafeQueue<FileNode> fileQueue, String searchKey) {
        List<Thread> threadList = new ArrayList<>(maxThreads - 1);
        Map<Node, String> result = new HashMap<>();

        for (int i = 0; i < this.maxThreads - 1; i++) {
            Thread consumer = new Thread(() -> {
                // search file line by line
                while (true) {
                    FileNode fileNode = fileQueue.poll();
                    if (fileNode == null) {
                        System.out.println(Thread.currentThread().getName() + " has stopped execution");
                        return;
                    }
                    File currFile = new File(fileNode.absolutePath);
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(currFile));
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (line.contains(searchKey)) {
                                result.put(fileNode, line);
                            }
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }

            });
            consumer.start();
            threadList.add(consumer);
        }
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        return result;
    }
}
