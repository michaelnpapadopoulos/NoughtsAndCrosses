package project2;
import java.util.regex.*;

// Re-usable patter matcher (specifically used for the diagonals in UpgradedBoard)
public interface patternMatcher {
    
    default boolean winningLine(String lineString, int winCond, char symbolToFind) {
        // Sets pattern to be the players symbol win condition times in a row
        String winningLinePattern = String.format(".*[%c]{%d}.*", symbolToFind, winCond);
        Pattern patternChecker = Pattern.compile(winningLinePattern);
        Matcher patternMatcher = patternChecker.matcher(lineString.toString());

        if (patternMatcher.find()) {
            return true;
        } else {
            return false;
        }
    }
}
