package FIleNameMatcher;

import java.util.*;

public class FileNameMatcher {

    static class FileMatchNode {
        String filename;
        String absPath;
        int relevancyIndex;

        FileMatchNode(String filename, String absPath, int relevancyIndex) {
            this.filename = filename;
            this.absPath = absPath;
            this.relevancyIndex = relevancyIndex;
        }

        @Override
        public String toString() {
            return absPath;
        }
    }

    private final BoundedPriorityQueue<FileMatchNode> matchedFiles;

    public FileNameMatcher(int maxMatches) {
        matchedFiles = new BoundedPriorityQueue<FileMatchNode>(maxMatches, (f1, f2) -> {
            if (f1.relevancyIndex != f2.relevancyIndex) {
                return Integer.compare(f1.relevancyIndex, f2.relevancyIndex);
            } else {
                return Integer.compare(f1.filename.length(), f2.filename.length());
            }
        });
    }

    private int calcLevenshteinDist(String word, String key) {
        int[][] dist = new int[word.length() + 1][key.length() + 1];

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
                    int minNeighbor = Math.min(dist[i - 1][j - 1], Math.min(dist[i - 1][j], dist[i][j - 1]));
                    dist[i][j] = 1 + minNeighbor;
                }
            }
        }
        // Levenshtein distance is the right bottom most corner element of dist
        return dist[word.length() - 1][key.length() - 1];
    }

    public void match(String filename, String key, String absPath) {
        /*
         * matching algorithm:
         * 1. calculate Levenshtein Distance (LD)
         * 2. get filename.length
         * 3. calculate relevancyIndex = filename.length - LD
         * 4. if relevancyIndex > 0
         *      add the file to matchedFiles array
         * 5. sort matchedFiles array on two parameters
         *      i. relevancyIndex
         *      ii. filename.length
         */

        // preprocessing
        int lastIndex = filename.lastIndexOf('.');
        if (lastIndex != -1) {
            filename = filename.substring(0, lastIndex);
        }
        filename = filename.toLowerCase();
        key = key.toLowerCase();
        if (filename.isEmpty()) {
//            System.out.println("filename without ext was empty for path: " + absPath);
            return;
        }

        int LD = calcLevenshteinDist(filename, key);

        if (LD < filename.length() * 3 / 4F) {
            matchedFiles.offer(new FileMatchNode(filename, absPath, LD));
        }
    }

    public List<String> getMatchedFiles() {
        List<String> matchedFileStrings = new ArrayList<>();
        for (FileMatchNode matchedNode : matchedFiles) {
            matchedFileStrings.add(matchedNode.toString());
        }

        return matchedFileStrings;
    }

}