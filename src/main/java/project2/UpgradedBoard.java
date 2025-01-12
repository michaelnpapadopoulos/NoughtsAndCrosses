package project2;

public class UpgradedBoard extends Board implements patternMatcher {

    public UpgradedBoard(int numOfRows, int numOfCols) {
        super(numOfRows, numOfCols);
    }

    public boolean checkRows(int winCond) {
        int symbolsInRow;
        char sampleSquare = ' ';
        char currentSquare;

        for (int row = 0; row < numOfRows; row++) {
            symbolsInRow = 0;
            sampleSquare = ' ';

            for (int col = 0; col < numOfCols; col++) {
                currentSquare = getSquare(row, col);

                if (currentSquare == sampleSquare && sampleSquare != ' ') {
                    symbolsInRow++;
                } else {
                    sampleSquare = currentSquare;
                    if (sampleSquare == ' ') {
                        symbolsInRow = 0;
                    } else {
                        symbolsInRow = 1;
                    }
                }

                if (symbolsInRow == winCond) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean checkCols(int winCond) {
        int symbolsInCol;
        char sampleSquare = ' ';
        char currentSquare;

        for (int col = 0; col < numOfCols; col++) {
            symbolsInCol = 0;
            sampleSquare = ' ';

            for (int row = 0; row < numOfRows; row++) {
                currentSquare = getSquare(row, col);

                if (currentSquare == sampleSquare && sampleSquare != ' ') {
                    symbolsInCol++;
                } else {
                    sampleSquare = currentSquare;
                    if (sampleSquare == ' ') {
                        symbolsInCol = 0;
                    } else {
                        symbolsInCol = 1;
                    }
                }

                if (symbolsInCol == winCond) {
                    return true;
                }
            }
        }

        return false;
    }

    // Checks every diagonal possible (Including "Mini diagonals")
    public boolean checkDiag(int winCond) {
        StringBuffer lineString = new StringBuffer(" ");
        char playerSymbol;

        int currentRow, currentCol;

        for (int row = 0; row < numOfRows; row++) {
            for (int col = 0; col < numOfCols; col++) {
                // Finds a square with a players symbol on it, and then checks all diagonals from that square
                if (!checkSquare(row, col)) { 
                    lineString.delete(0, lineString.length());
                    playerSymbol = getSquare(row, col);

                    currentRow = row;
                    currentCol = col;

                    // Top left to bottom right diagonal
                    while (currentRow-1 >= 0 && currentCol-1 >= 0) { // Navigate to the top left of the diagonal
                        currentRow--;
                        currentCol--;
                    }

                    // Appends whole top left to bottom right diagonal to string
                    while (true) {
                        lineString.append(getSquare(currentRow, currentCol));
                        if (currentRow+1 < numOfRows && currentCol+1 < numOfCols) {
                            currentRow++;
                            currentCol++;
                        } else {
                            break;
                        }
                    }

                    if (winningLine(lineString.toString(), winCond, playerSymbol)) {
                        return true;
                    } else {
                        lineString.delete(0, lineString.length()); // Clear string for next diagonal check
                    }

                    // Reset to initial square to check top right to bottom left diagonal
                    currentRow = row;
                    currentCol = col;

                    while (currentRow-1 >= 0 && currentCol+1 < numOfCols) { // Navigate to top right of the diagonal
                        currentRow--;
                        currentCol++;
                    }

                    while (true) {
                        lineString.append(getSquare(currentRow, currentCol));
                        if (currentRow+1 < numOfRows && currentCol-1 >= 0) {
                            currentRow++;
                            currentCol--;
                        } else {
                            break;
                        }
                    }

                    if (winningLine(lineString.toString(), winCond, playerSymbol)) {
                        return true;
                    } else {
                        lineString.delete(0, lineString.length()); // Clear string for next diagonal check
                    }
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {
        int i = 0;
        do {
            System.out.println(i);
            i++;
        } while (i < 5);  
        
        int y = 0;
        while (y < 5) {
            System.out.println(y);
            y++;
        }
    }
}
