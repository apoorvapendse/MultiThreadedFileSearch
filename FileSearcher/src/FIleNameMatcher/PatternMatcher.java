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
