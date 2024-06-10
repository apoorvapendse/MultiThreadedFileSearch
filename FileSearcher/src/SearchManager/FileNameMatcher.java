package SearchManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileNameMatcher {

    static class FileMatchNode {
        String name;
        int matchedNums;

        FileMatchNode(String n, int count) {
            this.name = n;
            this.matchedNums = count;
        }
    }

    // Use ConcurrentHashMap for thread-safe operations
    static Map<String, Integer> matchedFileChars = new ConcurrentHashMap<>();

    public static void match(String filename, String key, String absolutePath) {
        // Initialize the count if not already present
        matchedFileChars.putIfAbsent(absolutePath, 0);

        for (char c : key.toCharArray()) {
            if (filename.contains(String.valueOf(c))) {
                // Use compute to safely update the value in a thread-safe manner
                matchedFileChars.compute(absolutePath, (k, v) -> v + 1);
            }
        }
    }

    public static List<String> getMatchedFilePaths(int minimumMatchingCharCount) {
        // Converting map to a list
        List<FileMatchNode> list = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : matchedFileChars.entrySet()) {
            list.add(new FileMatchNode(entry.getKey(), entry.getValue()));
        }

        // sort the list based on matched chars
        Collections.sort(list, (a, b) -> b.matchedNums - a.matchedNums);

        // Collecting results based on minimumMatchingCharCount
        List<String> results = new ArrayList<>();
        for (FileMatchNode n : list) {
            if (n.matchedNums > minimumMatchingCharCount) {
                results.add(n.name);
            }
        }
        return results;
    }

}