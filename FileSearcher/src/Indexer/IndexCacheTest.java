//package Indexer;
//
//import Serializer.SerializationManager;
//
//import java.nio.file.Path;
//
//public class IndexCacheTest {
//    private static void createCache(String path) {
//        long indexStartTime = System.currentTimeMillis();
//        IndexManager im = new IndexManager(Path.of(path).toAbsolutePath().toString());
//        double indexingDiff = System.currentTimeMillis() - indexStartTime;
//        double indexingSecoonds = ((double)indexingDiff/1000);
//
//        System.out.println("Indexing done in "+indexingSecoonds);
//
//
//        long serializationStartTime = System.currentTimeMillis();
//
//
//        SerializationManager sm = new SerializationManager();
//        sm.serialize(im.getHead(), "save.txt");
//
//        long diff = System.currentTimeMillis() - serializationStartTime;
//        double diffInSeconds = ((double)diff/1000);
//        System.out.println("Serialization done in:"+diffInSeconds);
//    }
//    public static void main(String[] args) {
//        String indexPath = "C:/";
//        createCache(indexPath);
//
//
//        SerializationManager sm = new SerializationManager();
//        long startTime = System.currentTimeMillis();
//        DirNode root = sm.deserialize("save.txt");
//        long difference = System.currentTimeMillis() - startTime;
//        double diff = ((double) difference / 1000);
//        System.out.println("deserialization done in " + diff + " seconds");
//
////        IndexManager im = new IndexManager(root.absolutePath);
//    }
//}