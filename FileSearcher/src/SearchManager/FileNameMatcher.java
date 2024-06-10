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

    private int calcLevenshteinDist(String word, String key) {
        int[][] dist = new int[word.length()][key.length()];

        /*
         * initialize the dist array such as
         * 0 1 2 ... key.length() - 1
         * 1
         * 2
         * ..
         * word.length() - 1
         */
        for (int i = 0; i < word.length(); i++) {
            dist[i][0] = i;
        }
        for (int j = 0; j < key.length(); j++) {
            dist[0][j] = j;
        }

        /*
         * algorithm:
         * compare characters of word and key in nested loop
         * if characters are same
         *      dist[i][j] = upper left neighbour of dist
         * else
         *      dist[i][j] = min(upper left, left, upper) neighbour
         */
        for (int i = 1; i <= word.length(); i++) {
            for (int j = 1; j <= key.length(); j++) {
                if (word.charAt(i - 1) == key.charAt(j - 1)) {
                    dist[i][j] = dist[i - 1][j - 1];
                } else {
                    int minNeighbour = Math.min(dist[i - 1][j - 1], Math.min(dist[i - 1][j], dist[i][j - 1]));
                    dist[i][j] = 1 + minNeighbour;
                }
            }
        }

        // Levenshtein distance is the right bottom most corner element of dist
        return dist[word.length() - 1][key.length() - 1];
    }
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