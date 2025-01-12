package project2;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class HumanPlayer extends Player {

    public HumanPlayer(String playerName, char playerSymbol) {
        super(playerName, playerSymbol);
    }

    // Converts the input of coords into useable integers that have been 0-indexed for processing
    public int[] makeMove(Scanner inputObject, Board currentBoard, char opponentSymbol, int winCond) {
        
        // Loop and try/catch block to ensure that the user inputs coordinates in the correct form
        while (true) {
            try {
                String stringMove = inputObject.nextLine();
                StringTokenizer strTokenizer = new StringTokenizer(stringMove, " ,.");
                String rowCoord = strTokenizer.nextToken();
                String colCoord = strTokenizer.nextToken();

                int[] moveCoords = {Integer.parseInt(rowCoord) - 1, Integer.parseInt(colCoord) - 1};
                return moveCoords;
            } catch (NoSuchElementException | NumberFormatException e) {
                System.out.printf("You did not enter coordinates in the form 'x,y', please try again.\n> ");
            }
        }
    }
}
