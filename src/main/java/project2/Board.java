package project2;
import java.util.ArrayList;

public class Board {

    private ArrayList<ArrayList<Character>> TTTBoard = new ArrayList<>(); // Array list that stores board
    // Stores the number of cols and rows for processing
    protected int numOfRows;
    protected int numOfCols;

    
    //============= Board initialization =============//
    public Board(int numOfRows, int numOfCols) {
        this.numOfRows = numOfRows;
        this.numOfCols = numOfCols;

        for (int row = 0; row < numOfRows; row++) {
            TTTBoard.add(new ArrayList<Character>());
        }

        for (ArrayList<Character> Row: TTTBoard) {
            for (int column = 0; column < numOfCols; column++) {
                Row.add(' ');
            }
        }
    }


    //============= Square checkers/getters/setters =============//
    public void placeMarker(int row, int col, char marker) {
        TTTBoard.get(row).set(col, marker);
    }

    
    // Returns true if square is free and exists and false if square is taken or is out of bounds
    public boolean checkSquare(int row, int col) {
        if (row < 0 || row >= numOfRows || col < 0 || col >= numOfCols) { // Returns false if out of bounds
            return false;
        }
        if (TTTBoard.get(row).get(col) == ' ') { // Returns true if in bounds and empty
            return true;
        } else {
            return false;
        }
    }
    
    // Returns the character at the given square
    public char getSquare(int row, int col) {
        return TTTBoard.get(row).get(col);
    }

    // Returns integer array containing number of rows and columns
    public int[] getBoardSize() {
        int[] boardSize = {numOfRows, numOfCols};
        return boardSize;
    }

    //============= Row/Col/Diag/board checkers =============//
    public boolean checkRows() {
        char sampleSquare; // Square to compare others in row to
        int col = 0;
        int symbolsInRow = 0;

        for (int row = 0; row < numOfRows; row++) {
            sampleSquare = getSquare(row, col); // Sets sample square to first square in row
            
            while (col < numOfCols) {
                if (sampleSquare == ' ') { // Automatically breaks if sample square is empty
                    break;
                }


                if (getSquare(row, col) == sampleSquare) {
                    symbolsInRow++;
                }

                col++;
            }

            col = 0; // Resets to the first column for next row

            // If the row is full of symbols, return true
            if (symbolsInRow == numOfCols) {
                return true;
            } else {
                symbolsInRow = 0;
            }
        }

        return false;
    }


    public boolean checkCols() { // Same general logic as checkRows
        char sampleSquare;
        int row = 0;
        int symbolsInCol = 0;


        for (int col = 0; col < numOfCols; col++) {
            sampleSquare = getSquare(row, col);

            while (row < numOfRows) {
                if (sampleSquare == ' ') {
                    break;
                }

                if (getSquare(row, col) == sampleSquare) {
                    symbolsInCol++;
                }

                row++;
            }

            row = 0;

            if (symbolsInCol == numOfCols) {
                return true;
            } else {
                symbolsInCol = 0;
            }
        }


        return false;
    }

    public boolean checkDiag() {
        // First checks the center square is it is part of both diagonals.
        char centerSquare = getSquare(numOfRows/2, numOfCols/2);
        if (centerSquare == ' ') { // If the center square isnt taken, immediately returns false
            return false;
        }

        int currentRow = 0;
        int symbolsInDiag = 0;
        
        // Similar logic to checkCols and checkRows for here on out
        // Top left to bottom right diagonal
        for (int col = 0; col < numOfCols; col++) {
            if (getSquare(col, col) == centerSquare) {
                symbolsInDiag++;
            }
        }

        if (symbolsInDiag == numOfCols) {
            return true;
        }

        symbolsInDiag = 0; // Reset diagonal symbol counter for other diagonal direction

        // Top right to bottom left diagonal
        for (int col = numOfCols-1; col >= 0; col--) {
            if (getSquare(currentRow, col) == centerSquare) {
                symbolsInDiag++;
            }

            currentRow++;
        }

        if (symbolsInDiag == numOfCols) {
            return true;
        }

        return false;
    }

    // Checks if the board has any open squares left
    public boolean fullBoard() {
        for (int row = 0; row < numOfRows; row++) {
            for (int col = 0; col < numOfCols; col++) {
                if (this.checkSquare(row, col)) {
                    return false;
                }
            }
        }

        return true;
    }


    //============= Board Display =============//
    // Dynamically prints the tic-tac-toe board to stdout (Can properly print any size board)
    public void displayBoard() {

        for (int row = 0; row < numOfRows; row++) {

            for (int col = 0; col < numOfCols; col++) {
                if (col == numOfRows-1) {
                    System.out.printf(" %c\n", TTTBoard.get(row).get(col));
                } else {
                    System.out.printf(" %c |", TTTBoard.get(row).get(col));
                }
            }

            if (!(row == TTTBoard.size()-1)) {
                for (int spacerIndex = 1; spacerIndex < numOfCols*4; spacerIndex++) {
                    if (spacerIndex == numOfCols*4-1) {
                        System.out.printf("-\n");
                    } else {
                        System.out.printf("-");
                    }
                }
            } 
        }
    }
}
