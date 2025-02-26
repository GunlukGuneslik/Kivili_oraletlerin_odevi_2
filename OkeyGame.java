import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class OkeyGame {

    Player[] players;
    Tile[] tiles;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    public OkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[112];
        int currentTile = 0;

        // two copies of each color-value combination, no jokers
        for (int i = 1; i <= 7; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[currentTile++] = new Tile(i,'Y');
                tiles[currentTile++] = new Tile(i,'B');
                tiles[currentTile++] = new Tile(i,'R');
                tiles[currentTile++] = new Tile(i,'K');
            }
        }
    }

    /*
     * @author Cagla Gunes
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles
     * this method assumes the tiles are already shuffled
     */
    public void distributeTilesToPlayers() {
        for (int i = 0; i < players.length; i++) {
            Player currentPlayer = players[i];
            for (int a = 0; a < 14; a++) {
                Tile pickedTile = getTopTile();
                currentPlayer.addTile(pickedTile);
            }

            if (i == 0) {
                Tile pickedTile = getTopTile();
                currentPlayer.addTile(pickedTile);
            }
        }
    }

    /*
     * @author zeynep
     * TODO: get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public Tile getLastDiscardedTile() {
        return lastDiscardedTile;
    }

    /*
     * @ author Cagla Gunes
     * TODO: get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top tile)
     * it should return the toString method of the tile so that we can print what we picked
     * My implementation notes:
     * If there is no tile left in the stack it returns to null.
     * After getting the top tile from tiles array; method makes the top tile's place null.
     */
    public Tile getTopTile() {
        int index = findIndexOfArraysLastElement(tiles);
        if (index >= 0) {
            Tile pickedTile = tiles[index];
            tiles[index] = 0;
            return pickedTile;
        }
        return null;
    }

    /*
    * @author Cagla Gunes
    * Finds the last element's index in an partially filled array.
    * this method assumes that array is filled by starting from index 0 and there is no gap (null element) between consecutive elements.
    * If the first element is null it returns -1 indicates array is filled with null elements.
    *   If array is completely filled it returns -2.
    */
    private static int findIndexOfArraysLastElement(Arrays arr[]) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) {
                return i - 1;
            }
        }
        return -2;
    }

    /*
     * @author zeynep
     * TODO: should randomly shuffle the tiles array before game starts
     */
    public void shuffleTiles() {
        Collections.shuffle(Arrays.asList(tiles));
    }

    /*
     * @author Kerem   
     * TODO: check if game still continues, should return true if current player
     * finished the game, use isWinningHand() method of Player to decide
     */
    public boolean didGameFinish() {
        return players[currentPlayerIndex].isWinningHand();
    }

    /*
     * @author Kerem 
     * TODO: Pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * You should consider if the discarded tile is useful for the computer in
     * the current status. Print whether computer picks from tiles or discarded ones.
     */
    public void pickTileForComputer() {
        
        boolean canChain= false;
        boolean alreadyExists= false;
        for(int i=0; i<players[currentPlayerIndex].numberOfTiles; i++)
        {
            if(getLastDiscardedTile().canFormChainWith(players[currentPlayerIndex].getTiles()[i]))
            {
                canChain=true;
            }
            if(getLastDiscardedTile().compareTo(players[currentPlayerIndex].getTiles()[i])==0)
            {
                alreadyExists=true;
            }
        }
        if(!alreadyExists && canChain)
        {
            players[currentPlayerIndex].addTile(getLastDiscardedTile());
            System.out.println("Player "+players[currentPlayerIndex].getName()+" picked the last discarded tile.");
        }
        else
        {
            players[currentPlayerIndex].addTile(getTopTile());
            System.out.println("Player "+players[currentPlayerIndex].getName()+" picked a tile from tiles.");
        }
    }

   /*
     * @author: Eftelya
     * TODO: Current computer player will discard the least useful tile.
     * this method should print what tile is discarded since it should be
     * known by other players. You may first discard duplicates and then
     * the single tiles and tiles that contribute to the smallest chains.
     */
    public void discardTileForComputer() {
        Player curPlayer = players[currentPlayerIndex];
        for(int i = 1 ; i< curPlayer.getTiles().length; i++)
        {
            if(curPlayer.getTiles()[i].compareTo(curPlayer.getTiles()[i-1])== 0)
            {
                lastDiscardedTile =curPlayer.getAndRemoveTile(i-1);
                displayDiscardInformation();
            }
        }

        if(lastDiscardedTile== null)
        {
         boolean isSingle= false;
            for(int i = 0 ; i< curPlayer.getTiles().length-1; i++)
            {
              if(curPlayer.getTiles()[i].getValue() != curPlayer.getTiles()[i+1].getValue())
                {
                    isSingle= true;
                }

              if(isSingle)
                {
                    lastDiscardedTile =curPlayer.getAndRemoveTile(i);
                    displayDiscardInformation();
                }
            }
            
        }

    }

    /*
     * @author zeynep
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */
    public void discardTile(int tileIndex) {
        Player currentPlayer = players[currentPlayerIndex];
        lastDiscardedTile = currentPlayer.getAndRemoveTile(tileIndex);
    }

    public void displayDiscardInformation() {
        if(lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

      public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if(index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }

}
