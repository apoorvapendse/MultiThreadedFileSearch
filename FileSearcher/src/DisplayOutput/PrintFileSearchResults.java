package DisplayOutput;

import java.util.List;

public class PrintFileSearchResults {
    public static void printAbsolutePaths(List<String> result) {
        for (int i = result.size() - 1; i >= 0; i--) {
            System.out.println(result.get(i));
        }
    }
}
