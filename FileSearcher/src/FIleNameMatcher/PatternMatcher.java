package FIleNameMatcher;

import java.util.HashMap;
import java.util.Map;

public class PatternMatcher {
    Map<Character, Integer> badChar;
    final String pattern;

    public PatternMatcher(String pattern) {
        this.pattern = pattern;
        calcLastOccurrence();
    }

    public boolean KMPSearch(String pat, String txt) {
        int M = pat.length();
        int N = txt.length();

        // create lps[] that will hold the longest
        // prefix suffix values for pattern
        int lps[] = new int[M];
        int j = 0; // index for pat[]

        // Preprocess the pattern (calculate lps[]
        // array)
        computeLPSArray(pat, M, lps);

        int i = 0; // index for txt[]
        while ((N - i) >= (M - j)) {
            if (pat.charAt(j) == txt.charAt(i)) {
                j++;
                i++;
            }
            if (j == M) {
                return true;
//                System.out.println("Found pattern "
//                        + "at index " + (i - j));
//                j = lps[j - 1];
            }

            // mismatch after j matches
            else if (i < N
                    && pat.charAt(j) != txt.charAt(i)) {
                // Do not match lps[0..lps[j-1]] characters,
                // they will match anyway
                if (j != 0)
                    j = lps[j - 1];
                else
                    i = i + 1;
            }
        }
        return false;
    }

    void computeLPSArray(String pat, int M, int[] lps) {
        // length of the previous longest prefix suffix
        int len = 0;
        int i = 1;
        lps[0] = 0; // lps[0] is always 0

        // the loop calculates lps[i] for i = 1 to M-1
        while (i < M) {
            if (pat.charAt(i) == pat.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else // (pat[i] != pat[len])
            {
                // This is tricky. Consider the example.
                // AAACAAAA and i = 7. The idea is similar
                // to search step.
                if (len != 0) {
                    len = lps[len - 1];

                    // Also, note that we do not increment
                    // i here
                } else // if (len == 0)
                {
                    lps[i] = len;
                    i++;
                }
            }
        }
    }

    private void calcLastOccurrence() {
        this.badChar = new HashMap<>();
        for (int i = 0; i < pattern.length(); i++) {
            badChar.put(pattern.charAt(i), i);
        }

    }

    public boolean containsPattern(String text) {
        int patLen = pattern.length(), textLen = text.length();
        int offset = 0;

        while (offset <= textLen - patLen) {
            int patternPtr = patLen - 1;
            while (patternPtr >= 0 && pattern.charAt(patternPtr) == text.charAt(offset + patternPtr)) {
                patternPtr--;
            }

            if (patternPtr < 0) {
                return true;
            }

            int lastOccInPattern = badChar.getOrDefault(text.charAt(offset + patternPtr), -1);
            offset += Math.max(1, patternPtr - lastOccInPattern);
        }

        return false;
    }
}
