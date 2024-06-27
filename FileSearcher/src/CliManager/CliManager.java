package CliManager;

import Indexer.DirNode;
import Indexer.IndexManager;
import SearchManager.SearchManager;
import Serializer.SerializationManager;

import java.util.*;

public class CliManager {


    static IndexManager im;
    static SerializationManager sm = new SerializationManager();
    static HashSet<String> ignoredFilesSet = new HashSet<>();
    static HashSet<String> ignoredDirsSet = new HashSet<>();


    public static void parseArgs(String[] args) {

        HashSet<String> allowedFlags = new HashSet<>(Arrays.asList("-s", "-i", "-f", "-igf", "-igd", "-r"));

        Map<String, String> flagToArg = new HashMap<>();
        boolean expectArg = false;
        String currFlag = "";
        for (String arg : args) {
            if (expectArg && arg.startsWith("-")) //flag after flag;
            {
                throw new IllegalArgumentException(currFlag + " expects an argument");
            } else if (!expectArg && arg.startsWith("-")) //new flag
            {
                if (!allowedFlags.contains(arg)) {
                    throw new IllegalArgumentException("Flag not recognized:" + arg);
                }
                if (!arg.equals("-i"))//-i doesn't expect arg
                {
                    expectArg = true;
                }
                currFlag = arg;
                flagToArg.put(arg, "");
            } else if (expectArg && !arg.startsWith("-")) //arg for current flag
            {
                expectArg = false;//we can again accept flag as the next argument.
                flagToArg.put(currFlag, arg);
            } else if (!expectArg && !arg.startsWith(("`"))) {
                throw new IllegalArgumentException(currFlag + " does not expect an argument");
            }
        }

        if (expectArg) {
            throw new MissingFormatArgumentException(currFlag + "expects an arg");
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

            if(flagToArg.containsKey("-igf"))
            {
                String filesString = flagToArg.get("-igf");
                ignoredFilesSet = getIgnoredSet(filesString);
            }
            if(flagToArg.containsKey("-igd"))
            {
                String dirsString = flagToArg.get("-igd");
                ignoredDirsSet= getIgnoredSet(dirsString);
            }

            im = new IndexManager(cwd,ignoredFilesSet,ignoredDirsSet);
            sm.serialize(im.getHead(), "mtfs-index.txt");

            if(flagToArg.containsKey("-s"))
            {
                String searchArg = flagToArg.get("-s");
                List<String> results = SearchManager.multiThreadedBFS(im,searchArg,10);
                for(String result:results)
                {
                    System.out.println(result);
                }
            }

            //execute further flags...
        } else if (flagToArg.containsKey("-r")) {
            System.out.println(flagToArg.get("-r"));
            im = new IndexManager(flagToArg.get("-r"),ignoredFilesSet,ignoredDirsSet);
            sm.serialize(im.getHead(), "mtfs-index.txt");

            //execute further flags...

        } else {
            //means the user wants to use the previous index.

            DirNode root = sm.deserialize("mtfs-index.txt");

            System.out.println(root.filename);
            //execute further flags...
        }
    }

    private static HashSet<String> getIgnoredSet(String ignoreString)
    {
        String[] ignoredNames = ignoreString.split(" ");
        HashSet<String> ignoredNodes = new HashSet<>(Arrays.asList(ignoredNames));
        return ignoredNodes;
    }


}
