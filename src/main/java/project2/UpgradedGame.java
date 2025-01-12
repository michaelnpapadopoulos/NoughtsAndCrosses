package project2;

public class UpgradedGame extends Game {
    private int winCondition;
    private int gridSize;
    private UpgradedBoard tictactoeBoard;

    private void getGameGridSize() {
        System.out.printf("Please enter the size of the game grid. (3-20)\n> ");
        while (true) {
            String userInputSize = getNormalizedUserInput();
        
            try {
                gridSize = Integer.parseInt(userInputSize);
                if (gridSize < 3 || gridSize > 20) {
                    System.out.printf("Please ensure you are entering an integer from 3 to 20.\n> ");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.printf("Please ensure you are entering an integer from 3 to 20.\n> ");
            }

        }
    }

    private void getGameWinCondition() {
        System.out.printf("Please enter the win condition for the game. For example, '3' would indicate that it only takes 3 squares in a line to win.\n(Must be greater than 1 and at most equal the size of the grid.\n> ");
        while (true) {
            String userInputWinCond = getNormalizedUserInput();
        
            try {
                winCondition = Integer.parseInt(userInputWinCond);
                if (winCondition < 2 || winCondition > gridSize) {
                    System.out.printf("Please ensure you are entering an integer from 2 to %d.\n> ",gridSize);
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.printf("Please ensure you are entering an integer from 2 to %d.\n> ",gridSize);
            }

        }
    }

    // Upgraded check for win
    private boolean checkForWin() {
        if (tictactoeBoard.checkRows(winCondition) || tictactoeBoard.checkCols(winCondition) || tictactoeBoard.checkDiag(winCondition)) {
            return true;
        } else {
            return false;
        }
    }


    // Upgraded game loop (Overide old game loop)
    public void startGame() {
        if (!gameRestarted) { // Only prints the welcome message once
            welcomeMessage();
        }

        getGameGridSize();
        getGameWinCondition();
        
        // Instantiates both players of the game
        player1 = getPlayerInfo(1, null);
        player2 = getPlayerInfo(2, player1);

        // Instantiates a blank tic-tac-toe board and displays it
        System.out.println("Now that all player information has been collected, the game will begin!");
        tictactoeBoard = new UpgradedBoard(gridSize, gridSize); // Uses new upgraded board
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
                currentMove = currentPlayer.makeMove(inputObject, tictactoeBoard, opponentSymbol, winCondition);

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
        inputObject.close(); // Closed at the end of the game to avoid memory leaks
    }
    


    public static void main(String[] args) {
        UpgradedGame game = new UpgradedGame();

        game.startGame();
    }
}
