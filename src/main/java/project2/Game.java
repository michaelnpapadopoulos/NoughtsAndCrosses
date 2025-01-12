package project2;
import java.util.Scanner;

public class Game {

    // Instance variables to store the player and board objects
    protected Player player1;
    protected Player player2;
    protected Board tictactoeBoard;

    // Instance variables to store game loop data, such as current moves and current players
    protected int[] currentMove; // private int array currentMove to store move coordinates
    protected double currentTurn = 0; // tracks the game turns
    protected Player currentPlayer;
    protected char opponentSymbol; // Stores the opposing players symbol for the sake of the computer player
    protected boolean gameRestarted = false; // Tracks whether or not the game has been restarted
    protected int boardRows;
    protected int boardCols;

    /*
     * Scanner object that will be used throughout the game and passed
     * to other objects through their methods. Due to the inability to reopen
     * the System.in stream after closing it, this single object is used for ALL
     * input related processing for the game.
     * Gets closed after the game ends to avoid memory leaks.
     */
    Scanner inputObject = new Scanner(System.in);

    // Basic welcome message method that is called when the game is first started
    protected void welcomeMessage() {
        System.out.println("Hello and good day! Welcome to the greatest Tic Tac Toe Arena in the world! Goodluck!");
    }
    

    //============= Win/draw condition handling =============//
    // Both methods call methods on the Board object and then use their respective return values
    private boolean checkForWin() {
        if (tictactoeBoard.checkRows() || tictactoeBoard.checkCols() || tictactoeBoard.checkDiag()) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean checkForDraw(Board currentBoard) {
        return currentBoard.fullBoard();
    }


    //============= Player info handling =============//
    // Returns a new player object (Either computer or human)
    protected Player getPlayerInfo(int playerNumber, Player opponentPlayer) {
        String playerName;
        String strPlayerSymbol; // Stores the player symbol before it has been converted to char

        char playerSymbol;
        boolean playerIsComputer;

        System.out.printf("==== INITIALIZING PLAYER %d ====\n", playerNumber);
        
        playerIsComputer = isComputerPlayer();

        System.out.printf("Please enter the name of the player\n> ");
        if (opponentPlayer != null) { // Prevents users from entering duplicate names for players
            while (true) { 
                playerName = getUserInput();
                if (playerName.toLowerCase().equals(opponentPlayer.getPlayerName().toLowerCase())) {
                    System.out.printf("That name is taken, please try a different one.\n> ");
                } else {
                    break;
                }
            }
        } else {
            playerName = getUserInput();
        }
        
        
        System.out.printf("Please enter the symbol of the player (This symbol can be any single character... i.e. 'X' or 'O')\n> ");
        while (true) { 
            strPlayerSymbol = getUserInput();

            // Prevents user from entering multiple characters or numbers as their symbol
            if (strPlayerSymbol.length() != 1) {
                System.out.printf("Please enter a single character to represent the player (i.e. 'X' or 'O')\n> ");
            } else {
                if (opponentPlayer != null) { // Prevents user from entering taken symbol
                    if (strPlayerSymbol.charAt(0) == opponentPlayer.getPlayerSymbol() || strPlayerSymbol.charAt(0) == ' ') {
                        System.out.printf("That symbol is taken, please try a different one. (Cant be ' ' whitespace)\n> ");
                    } else {
                        playerSymbol = strPlayerSymbol.charAt(0);
                        break;
                    }
                } else {
                    playerSymbol = strPlayerSymbol.charAt(0);
                    break;
                }
            }
        }

        if (playerIsComputer) {
            return new ComputerPlayer(playerName, playerSymbol);
        } else {
            return new HumanPlayer(playerName, playerSymbol);
        }
    }

    // Gets the users decision on whether or not the player should be a computer
    private boolean isComputerPlayer() {
        System.out.printf("Would you like this player to be a computer? Yes for computer, No for human.\n> ");
        return yesOrNo();
    }


    //============= Input handling =============//
    // Gets string input from user and returns it as it was input
    private String getUserInput() {
        String stringInput;
        stringInput = inputObject.nextLine();
      
        return stringInput;
    }

    // Gets the string input from the user and returns a lowercase version of it
    protected String getNormalizedUserInput() {
        String normString = getUserInput();
        normString = normString.toLowerCase();

        return normString;
    }

    // Returns true if user entered yes, false if no,
    private boolean yesOrNo() {
        while (true) {
            String userDecision = getNormalizedUserInput();

            if (userDecision.equals("yes")) {
                return true;
            } else if (userDecision.equals("no")) {
                return false;
            } else {
                System.out.printf("Please enter yes or no (non-case sensitive)\n> ");
            }
        }
    }
    
    
    //============= Game loop and processes =============//
    public void startGame() {
        if (!gameRestarted) { // Only prints the welcome message once
            welcomeMessage();
        }
        
        // Instantiates both players of the game
        player1 = getPlayerInfo(1, null);
        player2 = getPlayerInfo(2, player1);

        // Instantiates a blank tic-tac-toe board and displays it
        System.out.println("Now that all player information has been collected, the game will begin!");
        tictactoeBoard = new Board(3, 3);
        tictactoeBoard.displayBoard();

        // Gets the boards dimensions for processing input coords in game loop
        int[] boardSize = tictactoeBoard.getBoardSize();
        boardRows = boardSize[0];
        boardCols = boardSize[1];

        System.out.println("Player 1, please make the first move.\nIn order to make a move, type the row and column of the grid that you intend to take. For example: '2,3' would be row 2 column 3");
        
        while (!checkForWin() && !checkForDraw(tictactoeBoard)) { // Continues the game loop whilst the board isnt full and no win
            if (currentTurn % 2 == 0) { // Assumes player 1 is always on an even turn, and player 2 odd
                currentPlayer = player1;
                opponentSymbol = player2.getPlayerSymbol();
            } else {
                currentPlayer = player2;
                opponentSymbol = player1.getPlayerSymbol();
            }

            while (true) {
                System.out.printf("%s's move:\n", currentPlayer.getPlayerName());

                // Gets move coords from the current player
                currentMove = currentPlayer.makeMove(inputObject, tictactoeBoard, opponentSymbol, 3);

                // Prevents squares from getting overwritten or from placing markers somewhere out of the range of the board
                if (currentMove[0] < 0 || currentMove[0] >= boardRows || currentMove[1] < 0 || currentMove[1] >= boardCols) {
                    System.out.println("Cannot place marker outside of the boards parameters, enter a different coordinate.");
                } else {
                    if (tictactoeBoard.checkSquare(currentMove[0], currentMove[1])) {
                        tictactoeBoard.placeMarker(currentMove[0], currentMove[1], currentPlayer.getPlayerSymbol());
                        break;
                    } else {
                        System.out.println("Square taken, try a different square.");
                    }
                }
            }

            currentTurn++; // Increment turn for next loop
            tictactoeBoard.displayBoard();
        }

        if (checkForWin()) {
            System.out.printf("Congratulations %s, you won the game!\n", currentPlayer.getPlayerName());
        } else {
            System.out.println("The game has ended in a draw as there are no more squares to occupy.");
        }

        restartGame();
        inputObject.close(); // Closed to at the end of the game to avoid memory leaks
    }

    protected void restartGame() {
        currentTurn = 0; // Resets turn counter
        boolean restartDecision;

        System.out.printf("Would you like to resart the game? Yes to restart, No to end\n> ");
        restartDecision = yesOrNo();

        if (restartDecision) {
            gameRestarted = true;
            startGame(); // Calls the game loop again
        }
    }
}
