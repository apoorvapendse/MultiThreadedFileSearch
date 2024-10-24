package CliManager;

import Indexer.DirNode;
import Indexer.IndexManager;
import Indexer.Node;  // Node class for content search results
import SearchManager.SearchManager;
import Serializer.SerializationManager;

import javax.swing.JTextArea;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

public class CliManager {

    static IndexManager im;
    static SerializationManager sm = new SerializationManager();
    static HashSet<String> ignoredFilesSet = new HashSet<>();
    static HashSet<String> ignoredDirsSet = new HashSet<>();
    static HashSet<String> ignoredExtSet = new HashSet<>();

    public static void parseArgs(String[] args) {
        /*
         * -s : search for file
         * -i : index cwd
         * -f : search file content
         * -r : index specific directory
         * -igf : ignore file
         * -igd : ignore directory
         * -ige : ignore extension
         */
        HashSet<String> allowedFlags = new HashSet<>(Arrays.asList("-s", "-i", "-f", "-igf", "-igd", "-r", "-ige"));

        Map<String, String> flagToArg = new HashMap<>();
        boolean expectArg = false;
        String currFlag = "";
        for (String arg : args) {
            // flag after flag;
            if (expectArg && arg.startsWith("-")) {
                throw new IllegalArgumentException(currFlag + " expects an argument");
            }
            // new flag
            else if (!expectArg && arg.startsWith("-")) {
                if (!allowedFlags.contains(arg)) {
                    throw new IllegalArgumentException("Flag not recognized: " + arg);
                }
                // -i doesn't expect arg
                if (!arg.equals("-i")) {
                    expectArg = true;
                }
                currFlag = arg;
                flagToArg.put(arg, "");
            }
            // arg for current flag
            else if (expectArg && !arg.startsWith("-")) {
                // we can again accept flag as the next argument.
                expectArg = false;
                flagToArg.put(currFlag, arg);
            } else if (!expectArg && !arg.startsWith(("`"))) {
                throw new IllegalArgumentException(currFlag + " does not expect an argument");
            }
        }

        if (expectArg) {
            throw new IllegalArgumentException(currFlag + " expects an argument");
        }

        if (flagToArg.containsKey("-i") && flagToArg.containsKey("-r")) {
            throw new IllegalArgumentException("Cannot use -i and -r flag at the same time, might cause conflicting root directories");
        }

        System.out.println("Printing flags and their respective args");
        for (Map.Entry<String, String> entry : flagToArg.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        executeCommands(flagToArg, System.getProperty("user.dir"));
    }

    public static void executeCommands(Map<String, String> flagToArg, String cwd) {
        if (flagToArg.containsKey("-i")) {
            if (flagToArg.containsKey("-igf")) {
                String filesString = flagToArg.get("-igf");
                ignoredFilesSet = getIgnoredSet(filesString);
                System.out.println("Ignoring files: " + ignoredFilesSet);
            }
            if (flagToArg.containsKey("-igd")) {
                String dirsString = flagToArg.get("-igd");
                ignoredDirsSet = getIgnoredSet(dirsString);
            }
            if (flagToArg.containsKey("-ige")) {
                String extString = flagToArg.get("-ige");
                ignoredExtSet = getIgnoredSet(extString);
                System.out.println("Ignoring extension: " + ignoredExtSet);
            }

            im = new IndexManager(cwd, ignoredFilesSet, ignoredDirsSet, ignoredExtSet);
            sm.serialize(im.getHead(), "mtfs-index.txt");

            System.out.println("Indexing complete for directory, excluding ignored items.");
            System.out.println("Files indexed: " + im.getFileCount());

            if (flagToArg.containsKey("-s")) {
                String searchArg = flagToArg.get("-s");
                List<String> results;
                if (im.getFileCount() > 10000) {
                    results = SearchManager.multiThreadedBFS(im, searchArg, 10);
                } else {
                    results = SearchManager.singleThreadedBFS(im, searchArg, 10);
                }
                System.out.println("Search results for '" + searchArg + "':");
                for (String result : results.reversed()) {
                    System.out.println(result);
                }
                System.out.println("Search for file '" + searchArg + "' completed.");
            }
        } else if (flagToArg.containsKey("-r")) {
            System.out.println("Indexing specific directory: " + flagToArg.get("-r"));
            im = new IndexManager(flagToArg.get("-r"), ignoredFilesSet, ignoredDirsSet, ignoredExtSet);
            sm.serialize(im.getHead(), "mtfs-index.txt");

            System.out.println("Indexing complete for directory, excluding ignored items.");
            System.out.println("Files indexed: " + im.getFileCount());

            if (flagToArg.containsKey("-s")) {
                String searchArg = flagToArg.get("-s");
                List<String> results;
                if (im.getFileCount() > 10000) {
                    results = SearchManager.multiThreadedBFS(im, searchArg, 10);
                } else {
                    results = SearchManager.singleThreadedBFS(im, searchArg, 10);
                }
                System.out.println("Search results for '" + searchArg + "':");

                for (String result : results.reversed()) {
                    System.out.println(result);
                }
                System.out.println("Search for file '" + searchArg + "' completed.");
            }

            if (flagToArg.containsKey("-f")) {
                String contentSearchArg = flagToArg.get("-f");
                Map<Node, String> contentResultsMap = SearchManager.searchWithinFiles(im, contentSearchArg);
                if (contentResultsMap.isEmpty()) {
                    System.out.println("No content matches found.");
                } else {
                    for (Map.Entry<Node, String> entry : contentResultsMap.entrySet()) {
                        System.out.println("File: " + entry.getKey().filename + " -> Content Match: " + entry.getValue());
                    }
                }
            }
        } else {
            // Assuming this means using the previous index.
            DirNode root = sm.deserialize("mtfs-index.txt");
            System.out.println("Using previous index from: " + root.filename);
            
        }
    }

    private static HashSet<String> getIgnoredSet(String ignoreString) {
        String[] ignoredNames = ignoreString.split(" ");
        HashSet<String> ignoredNodes = new HashSet<>(Arrays.asList(ignoredNames));
        return ignoredNodes;
    }

    public static void redirectSystemOutput(JTextArea textArea) {
        PrintStream printStream = new PrintStream(new JTextAreaOutputStream(textArea));
        System.setOut(printStream);
        System.setErr(printStream);
    }

    // JTextArea Output Stream
    private static class JTextAreaOutputStream extends OutputStream {
        private JTextArea textArea;

        public JTextAreaOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) {
            textArea.append(String.valueOf((char) b));
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }
}
