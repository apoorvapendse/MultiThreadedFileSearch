package SearchManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class SearchManager {
    public SearchManager()
    {
        System.out.println("Constructor");
        String targetFolderPath = Path.of("FileSearcher/search").toAbsolutePath().toString();
        File targetFolder = new File(targetFolderPath);
        File[] files = targetFolder.listFiles();
        for(File file : files)
        {
            System.out.println("Name:"+file.getName()+", isFile:"+file.isFile());
        }


    }

}
