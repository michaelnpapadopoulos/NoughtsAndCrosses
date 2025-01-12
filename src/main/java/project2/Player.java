package project2;
import java.util.Scanner;

public abstract class Player {
    private String playerName;
    private char playerSymbol;

    public Player(String playerName, char playerSymbol) {
        this.playerName = playerName;
        this.playerSymbol = playerSymbol;
    }

    public String getPlayerName() {
        return playerName;
    }
    
    public char getPlayerSymbol() {
        return playerSymbol;
    }

    public abstract int[] makeMove(Scanner inputObject, Board currentBoard, char opponentSymbol, int winCond);
}
