package FIleNameMatcher;

import java.util.Arrays;

public class PatternMatcher {
    final int MAX_CHAR = 256;
    int[] badChar;
    final String pattern;

    public PatternMatcher(String pattern) {
        this.pattern = pattern;
        calcLastOccurrence();
    }

    private void calcLastOccurrence() {
        this.badChar = new int[MAX_CHAR];
        Arrays.fill(badChar, -1);
        for (int i = 0; i < pattern.length(); i++) {
            badChar[(int) pattern.charAt(i)] = i;
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

            int lastOccInPattern = badChar[text.charAt(offset + patternPtr)];
            offset += Math.max(1, patternPtr - lastOccInPattern);
        }

        return false;
    }
}
