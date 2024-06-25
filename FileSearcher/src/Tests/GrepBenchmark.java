package Tests;

import Indexer.IndexManager;
import Indexer.Node;
import SearchManager.SearchFileContent;
import SearchManager.SearchManager;
import Serializer.SerializationManager;

import java.util.Map;

public class GrepBenchmark {
    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        IndexManager im = new IndexManager("C:");

       Map<Node,String> map =  SearchManager.multiThreadedSearchFileContent(im,"deer");
       for(Map.Entry<Node,String> entry :map.entrySet())
       {
           System.out.println(entry.getKey().filename+":"+entry.getValue());
       }

       long endTime = System.currentTimeMillis() - startTime;
       double diff = ((double) endTime/1000);
       System.out.println("Search finished in :" +diff);
    }
}