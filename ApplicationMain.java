import java.util.Scanner;

public class ApplicationMain {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        OkeyGame game = new OkeyGame();

        boolean willPlay;
        while (true) {
            System.out.print("Will there be a human Player? (Y/N):");
            String input = sc.next();
            input = input.toUpperCase();
            if (!"YN".contains(input)) {
                System.out.println("Invalid input.");
                continue;
            }
            willPlay = input.equalsIgnoreCase("Y");
            break;
        }
        String playerName = "Mahmut";
        if (willPlay){
            System.out.print("Please enter your name: ");
            playerName = sc.next();
        }

        game.setPlayerName(0, playerName);
        game.setPlayerName(1, "John");
        game.setPlayerName(2, "Jane");
        game.setPlayerName(3, "Ted");

        game.createTiles();
        game.shuffleTiles();
        game.distributeTilesToPlayers();

        // developer mode is used for seeing the computer players hands, to be used for
        // debugging
        System.out.print("Play in developer's mode with other player's tiles visible? (Y/N): ");
        char devMode = sc.next().charAt(0);
        boolean devModeOn = devMode == 'Y';

        boolean firstTurn = true;
        boolean gameContinues = true;
        int playerChoice = -1;

        while (gameContinues) {

            int currentPlayer = game.getCurrentPlayerIndex();
            System.out.println(game.getCurrentPlayerName() + "'s turn.");

            // this is the human player's turn
            if (willPlay && currentPlayer == 0) {
                game.displayCurrentPlayersTiles();
                game.displayDiscardInformation();

                System.out.println("What will you do?");

                if (!firstTurn) {
                    // after the first turn, player may pick from tile stack or last player's
                    // discard
                    System.out.println("1. Pick From Tiles");
                    System.out.println("2. Pick From Discard");

                    System.out.print("Your choice: ");
                    playerChoice = sc.nextInt();

                    while (playerChoice < 1 || playerChoice > 2) {
                        System.out.print("Enter a valid index: ");
                        playerChoice = sc.nextInt();
                    }

                } else {
                    // on first turn the starting player does not pick up new tile
                    System.out.println("1. Discard Tile");

                    System.out.print("Your choice: ");
                    playerChoice = sc.nextInt();

                    while (playerChoice != 1) {
                        System.out.print("Enter a valid index: ");
                        playerChoice = sc.nextInt();
                    }
                }

                

                // after the first turn we can pick up
                if (!firstTurn) {
                    if (playerChoice == 1) {
                        Tile topTile = game.drawAndAddTileToCurrentPlayer();

                        System.out.println("You picked up: " + topTile.toString());
                        firstTurn = false;
                    } else if (playerChoice == 2) {
                        Tile lastDiscardedTile = game.pickUpDiscardAndAddToCurrentPlayer();
                        System.out.println("You picked up: " + lastDiscardedTile.toString());
                    }

                    // display the hand after picking up new tile
                    game.displayCurrentPlayersTiles();
                } else {
                    // after first turn it is no longer the first turn
                    firstTurn = false;
                }

                gameContinues = !game.didGameFinish();

                if (gameContinues) {
                    // if game continues we need to discard a tile using the given index by the
                    // player
                    System.out.println("Which tile you will discard?");
                    System.out.print("Discard the tile in index: ");
                    playerChoice = sc.nextInt();

                    // @author:Eftelya
                    // TODO: make sure the given index is correct, should be 0 <= index <= 14
                    while (playerChoice < 0 || playerChoice > 14) {
                        System.out.println("wrong index! Enter an index between 0-14: ");
                        playerChoice = sc.nextInt();
                    }
                    game.discardTile(playerChoice);
                    game.passTurnToNextPlayer();

                } else {
                    boolean didWin = game.players[game.getCurrentPlayerIndex()].isWinningHand();
                    if (didWin)
                        System.out.println(game.getCurrentPlayerName() + " wins.");
                    else
                        System.out.println("Game has ended. Nobody wins.");
                }
            }
            // this is the computer player's turn
            else {
                if (devModeOn) {
                    game.displayCurrentPlayersTiles();
                }

                // computer picks a tile from tile stack or other player's discard
                game.pickTileForComputer();

                gameContinues = !game.didGameFinish();

                if (gameContinues) {
                    // if game did not end computer should discard
                    game.discardTileForComputer();
                    game.passTurnToNextPlayer();
                } else {
                    // current computer character wins
                    boolean didWin = game.players[game.getCurrentPlayerIndex()].isWinningHand();
                    if (didWin)
                        System.out.println(game.getCurrentPlayerName() + " wins.");
                    else
                        System.out.println("Game has ended. Nobody wins.");
                }
            }
            System.out.println();
        }
        sc.close();
    }
}
