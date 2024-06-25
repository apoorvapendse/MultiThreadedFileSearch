package CliManager;

import Indexer.IndexManager;

import java.util.*;

public class CliManager {



    public static void parseArgs(String[] args)
    {

        HashSet<String> allowedFlags = new HashSet<>(Arrays.asList(new String[]{"-s", "-i", "-f", "-ignore"}));


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

        System.out.println("Printing flags and their respective args");
        for(Map.Entry<String,String> entry : flagToArg.entrySet())
        {
            System.out.println(entry.getKey()+" : "+ entry.getValue());
        }






    }
}
