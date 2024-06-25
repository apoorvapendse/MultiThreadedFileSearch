package CliManager;

import Indexer.DirNode;
import Indexer.IndexManager;
import Indexer.Node;
import SearchManager.SearchManager;
import Serializer.SerializationManager;

import java.nio.file.Path;
import java.util.*;

public class CliManager {


    static IndexManager im;
    static SerializationManager sm = new SerializationManager();
    public static void parseArgs(String[] args)
    {

        HashSet<String> allowedFlags = new HashSet<>(Arrays.asList(new String[]{"-s", "-i", "-f", "-ignore","-r"}));


        Map<String,String> flagToArg = new HashMap<>();
        boolean expectArg = false;
        String currFlag = "";
        for(String arg :args)
        {
            if(expectArg  && arg.startsWith("-")) //flag after flag;
            {
                throw new IllegalArgumentException(currFlag + " expects an argument");
            }
            else if(!expectArg  && arg.startsWith("-")) //new flag
            {
                if(!allowedFlags.contains(arg))
                {
                    throw new IllegalArgumentException("Flag not recognized:"+arg);
                }
                if(!arg.equals("-i"))//-i doesn't expect arg
                {
                    expectArg = true;
                }
                currFlag = arg;
                flagToArg.put(arg,"");
            }
            else if(expectArg && !arg.startsWith("-")) //arg for current flag
            {
                expectArg = false;//we can again accept flag as the next arguement.
                flagToArg.put(currFlag,arg);
            }
            else if(!expectArg && !arg.startsWith(("`")))
            {
                throw new IllegalArgumentException(currFlag +" does not expect an argument");
            }

        }

        if(expectArg)
        {
            throw new MissingFormatArgumentException(currFlag +"expects an arg");
        }

        if(flagToArg.containsKey("-i") && flagToArg.containsKey("-r"))
        {
            throw new IllegalArgumentException("Cannot use -i and -r flag at the same time, might cause conflicting root directories");
        }
        System.out.println("Printing flags and their respective args");
        for(Map.Entry<String,String> entry : flagToArg.entrySet())
        {
            System.out.println(entry.getKey()+" : "+ entry.getValue());
        }


        executeCommands(flagToArg,System.getProperty("user.dir"));

    }

    public static void executeCommands(Map<String,String> flagToArg,String cwd)
    {
//        System.out.println(cwd);
        if(flagToArg.containsKey("-i"))
        {
            im = new IndexManager(cwd);
            sm.serialize(im.getHead(),"save.txt");

            //execute further flags...
        }
        else if(flagToArg.containsKey("-r"))
        {
            System.out.println(flagToArg.get("-r"));
            im = new IndexManager(flagToArg.get("-r"));
            sm.serialize(im.getHead(),"save.txt");

            //execute further flags...

        }
        else{
            //means the user wants to use the previous index.

            DirNode root = sm.deserialize("save.txt");

            System.out.println(root.filename);
            //execute further flags...
        }
    }

}
