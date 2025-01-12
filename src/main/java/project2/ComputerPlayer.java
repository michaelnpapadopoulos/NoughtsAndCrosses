package project2;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.*;

public class ComputerPlayer extends Player implements Wait {

    public ComputerPlayer(String playerName, char playerSymbol) {
        super(playerName, playerSymbol);
    }


    //============= Making moves, checking options for possible moves =============//
    // Will move in the specified way unless non of the 4 sub-strategies are available,
    // in which case will make a random move.
    public int[] makeMove(Scanner inputObject, Board currentBoard, char opponentSymbol, int winCond) {
        System.out.printf("%s is thinking...\n", this.getPlayerName());
        wait(2);

        int[] moveCoords;

        if ((moveCoords = checkForWin(currentBoard, this.getPlayerSymbol(), winCond)) != null) {
            System.out.println("going for win");
            return moveCoords;
        } else if ((moveCoords = blockWin(currentBoard, opponentSymbol, winCond)) != null) {
            System.out.println("blocking win");
            return moveCoords;
        } else if ((moveCoords = checkCenter(currentBoard)) != null) {
            System.out.println("taking center");
            return moveCoords;
        } else if ((moveCoords = checkCorners(currentBoard)) != null) {
            System.out.println("taking corners");
            return moveCoords;
        } else {
            System.out.println("making random move");
            moveCoords = randomCoord(currentBoard);

            while (!currentBoard.checkSquare(moveCoords[0], moveCoords[1])) {
                moveCoords = randomCoord(currentBoard);
            }
        }

        return moveCoords;
    }

    // Checks the four corners of the board, returns the first free corners coordinates
    private int[] checkCorners(Board currentBoard) {
        int[] boardSize = currentBoard.getBoardSize();
        int[] zeroedBoardSize = {boardSize[0]-1, boardSize[1]-1};

        if (currentBoard.checkSquare(0, 0)) {
            return new int[] {0,0};
        } else if (currentBoard.checkSquare(zeroedBoardSize[0], 0)) {
            return new int[] {zeroedBoardSize[0], 0};
        } else if (currentBoard.checkSquare(0, zeroedBoardSize[1])) {
            return new int[] {0, zeroedBoardSize[1]};
        } else if (currentBoard.checkSquare(zeroedBoardSize[0] , zeroedBoardSize[1])) {
            return new int[] {zeroedBoardSize[0] , zeroedBoardSize[1]};
        } else {
            return null;
        }
    }

    // Checks the "center" square of a board, returns its coords if free
    // Doesnt work well when the board itself doesnt have a center square (eg. a 4x4 board)
    private int[] checkCenter(Board currentBoard) {
        int[] boardSize = currentBoard.getBoardSize();
        int numOfRows = boardSize[0];
        int numOfCols = boardSize[1];

        char centerSquare = currentBoard.getSquare(numOfRows/2, numOfCols/2);
        if (centerSquare == ' ') {
            return new int[] {numOfRows/2, numOfCols/2};
        } else {
            return null;
        }
    }

    // Checks for a potential winning line
    private int[] checkForWin(Board currentBoard, char playerSymbol, int winCond) {
        return checkForFullLine(currentBoard, playerSymbol, winCond);
    }

    // Checks to block any potential winning line's
    private int[] blockWin(Board currentBoard, char opponentSymbol, int winCond) {
        return checkForFullLine(currentBoard, opponentSymbol, winCond);
    }

    // Generates a random coordinate within the boards range
    private int[] randomCoord(Board currentBoard) {
        int[] boardSize = currentBoard.getBoardSize();
        Random randomGenerator = new Random();

        int randomRow = randomGenerator.nextInt(boardSize[0]);
        int randomCol = randomGenerator.nextInt(boardSize[1]);

        int[] coords = {randomRow, randomCol};
        return coords;
    }


    //============= line checking/looking for possible full lines =============//
    // WORKS ON ANY BOARD SIZE AND ANY SIZE WIN CONDITION
    // Checks if there are any possible lines to make
    private int[] checkForFullLine(Board currentBoard, char symbolToCheck, int winCond) {
        // Storing general board information for processing
        int[] boardSize = currentBoard.getBoardSize();
        int numOfRows = boardSize[0];
        int numOfCols = boardSize[1];

        int possibleWin = winCond-1; // Number of characters in a line for a possible win
        
        // Represents the index position of the free square that will complete a line.
        // Is -1 if there is no square to complete a line
        int foundLine; 
        
        // StringBuffer object to hold each row as a string
        StringBuffer lineString = new StringBuffer(" ");

        int currentRow, currentCol; // Used for keeping track of the squares for the diagonal checks
        // Stores the sub diagonals coords
        ArrayList<int[]> subDiagCoords = new ArrayList<>();

        /*
         * Each following for loop appends a row, column, or diagonal to a StringBuffer object.
         * The string that represents that line is then passed into a patternCheck() method that will
         * return the position of the free square to fullfill the line, or -1 if there is no line to fullfill
         */
        // Checks horizontal lines
        for (int row = 0; row < numOfRows; row++) {
            lineString.delete(0, lineString.length()); // Clear string for each new row

            for (int col = 0; col < numOfCols; col++) {
                lineString.append(currentBoard.getSquare(row, col));
            }

            foundLine = patternCheck(lineString.toString(), symbolToCheck, possibleWin);
            if (foundLine != -1) {
                return new int[] {row, foundLine};
            }

        }

        // Checks vertical lines
        for (int col = 0; col < numOfCols; col++) {
            lineString.delete(0, lineString.length()); // Clear string for each new col

            for (int row = 0; row < numOfRows; row++) {
                lineString.append(currentBoard.getSquare(row, col));
            }

            foundLine = patternCheck(lineString.toString(), symbolToCheck, possibleWin);
            if (foundLine != -1) {
                return new int[] {foundLine, col};
            }
            
        }

        // Checks diagonal lines
        for (int row = 0; row < numOfRows; row++) {
            for (int col = 0; col < numOfCols; col++) {
                // Finds a square with the players symbol on it, and then checks all diagonals from that square
                if (currentBoard.getSquare(row, col) == symbolToCheck) { 
                    subDiagCoords.clear();
                    lineString.delete(0, lineString.length());
    
                    currentRow = row;
                    currentCol = col;

                    // Top left to bottom right diagonal
                    while (currentRow-1 >= 0 && currentCol-1 >= 0) { // Navigate to the top left of the diagonal
                        currentRow--;
                        currentCol--;
                    }

                    // Appends whole top left to bottom right diagonal to string
                    while (true) {
                        lineString.append(currentBoard.getSquare(currentRow, currentCol));
                        subDiagCoords.add(new int[] {currentRow, currentCol});
                        if (currentRow+1 < numOfRows && currentCol+1 < numOfCols) {
                            currentRow++;
                            currentCol++;
                        } else {
                            break;
                        }
                    }

                    foundLine = patternCheck(lineString.toString(), symbolToCheck, possibleWin);
                    if (foundLine != -1) {
                        return subDiagCoords.get(foundLine);
                    } else {
                        subDiagCoords.clear(); // Clears for next diagonal
                        // Reset to square at center of the diagonal
                        currentRow = row;
                        currentCol = col;
                        lineString.delete(0, lineString.length()); // clear diagonal string
                    }

                    // Appends whole top right to bottom left diagonal to string
                    while (currentRow-1 >= 0 && currentCol+1 < numOfCols) { // Navigate to top right of the diagonal
                        currentRow--;
                        currentCol++;
                    }

                    while (true) {
                        lineString.append(currentBoard.getSquare(currentRow, currentCol));
                        subDiagCoords.add(new int[] {currentRow, currentCol});
                        if (currentRow+1 < numOfRows && currentCol-1 >= 0) {
                            currentRow++;
                            currentCol--;
                        } else {
                            break;
                        }
                    }

                    foundLine = patternCheck(lineString.toString(), symbolToCheck, possibleWin);
                    if (foundLine != -1) {
                        return subDiagCoords.get(foundLine);
                    }
                }
            }
        }

        return null; // Returns null if there is no coordinate to fill a line
    }

    // Checks a string line for any winning pattern, if found, returns position of the winning space (Uses regex)
    private int patternCheck(String lineString, char symbolToCheck, int possibleWin) {
        // Dynamic regex patterns to look for a given character in a specified sequence within a string
        String forwardsPattern = String.format("[%c]{%d}[ ]{1}", symbolToCheck, possibleWin); // Pattern with the winning square to the right of the row of symbols
        String backwardsPattern = String.format("[ ]{1}[%c]{%d}", symbolToCheck, possibleWin);  // Pattern with the winning square to the left of the row of symbols
        String gapPattern = String.format("[%c]{1,%d}[ ]{1}[%c]{1,%d}", symbolToCheck, possibleWin-1, symbolToCheck, possibleWin-1); // Pattern with the winning square somewhere inbetween symbols

        // Define regex objects
        Pattern patternChecker;
        Matcher patternMatcher;

        // Checks for forward direction pattern
        patternChecker = Pattern.compile(forwardsPattern);
        patternMatcher = patternChecker.matcher(lineString.toString());
        if (patternMatcher.find()) {
            return patternMatcher.start()+possibleWin;
        }

        // Checks for backwards direction pattern
        patternChecker = Pattern.compile(backwardsPattern);
        patternMatcher = patternChecker.matcher(lineString.toString());
        if (patternMatcher.find()) {
            return patternMatcher.start();
        }

        // Checks for pattern with a gap
        patternChecker = Pattern.compile(gapPattern);
        patternMatcher = patternChecker.matcher(lineString.toString());
        // Will only treat pattern as a winning pattern if there are the correct number of symbols before and after the gap (Does this by called validGap() below)
        if (patternMatcher.find() && validGap(lineString.toString(), symbolToCheck, patternMatcher.start(), possibleWin+1)) {
            return lineString.toString().indexOf(' ', patternMatcher.start());
        }

        return -1;
    }

    /*
     * Checks a string from a given index value until the end of the match. 
     * If there are the correct number of symbols before and after gap, returns true.
     * If there arent enough symbols before and after the gap, then the gap does not represent
     * a possible line completing move, and returns false.
     */
    private boolean validGap(String lineString, char symbolToFind,int indexOfGapMatch, int winCond) {
        int possibleWin = winCond-1; // Symbols in line required right before a win
        int numberOfSymbolsInLine = 0;

        if (indexOfGapMatch < 0 || indexOfGapMatch+winCond > lineString.length()) {
            return false; // Returns false if the substring containing the gap is shorter than the wincond
        }

        for (int currentChar = indexOfGapMatch; currentChar < indexOfGapMatch+winCond; currentChar++) {
            if (lineString.charAt(currentChar) == symbolToFind) {
                numberOfSymbolsInLine++;
            }
        }

        if (numberOfSymbolsInLine == possibleWin) {
            return true;
        } else {
            return false;
        }
    }
}
