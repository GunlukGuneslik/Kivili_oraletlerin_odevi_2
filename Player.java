public class Player {
    String playerName;
    Tile[] playerTiles;
    int numberOfTiles;

    public Player(String name) {
        setName(name);
        playerTiles = new Tile[15]; // there are at most 15 tiles a player owns at any time
        numberOfTiles = 0; // currently this player owns 0 tiles, will pick tiles at the beggining of the game
    }

    /*
     * @author zeynep
     * TODO: removes and returns the tile in given index
     */
    public Tile getAndRemoveTile(int index) {
        Tile tile = playerTiles[index];
        for (int i = index; i < playerTiles.length-1; i++) {
            playerTiles[i] = playerTiles[i+1];
        }
        playerTiles[playerTiles.length - 1] = null;
        return tile;
    }

    /*
     * @author Cagla Gunes
     * TODO: adds the given tile to the playerTiles in order
     * should also update numberOfTiles accordingly.
     * make sure playerTiles are not more than 15 at any time
     */
    public void addTile(Tile t) {
        if (numberOfTiles <= 15) {
            int index = 0;
            while (index < numberOfTiles && numberOfTiles && playerTiles[index].compareTo(t) < 0) {
                index++;
            }

            for (int i = numberOfTiles; i > index; i--) {
                playerTiles[i] = playerTiles[i - 1];
            }

            playerTiles[index] = t;
            numberOfTiles++;
        }  
    }

    /*
     * TODO: checks if this player's hand satisfies the winning condition
     * to win this player should have 3 chains of length 4, extra tiles
     * does not disturb the winning condition
     * @return
     */
    public boolean isWinningHand() {
        int chainCount=0;
        int tileCount=1;
        for(int i=1; i<this.numberOfTiles; i++)
        {
           Tile FormerTile=this.playerTiles[i-1];
           Tile tile=this.playerTiles[i];
           if(tile.canFormChainWith(FormerTile))
           {
                tileCount++;
                if(tileCount==4)
                {
                    chainCount++;
                    tileCount=1;
                }
           }
           else if(!tile.canFormChainWith(FormerTile) && tile.getValue()!=FormerTile.getValue())
           {
            tileCount=1;
           }
        }
        return chainCount==3;
    }

    /**
    * This method finds the last tile' position!
    */
    public int findPositionOfTile(Tile t) {
        int tilePosition = -1;
        for (int i = 0; i < numberOfTiles; i++) {
            if(playerTiles[i].compareTo(t) == 0) {
                tilePosition = i;
            }
        }
        return tilePosition;
    }

    public void displayTiles() {
        System.out.println(playerName + "'s Tiles:");
        for (int i = 0; i < numberOfTiles; i++) {
            System.out.print(playerTiles[i].toString() + " ");
        }
        System.out.println();
    }

    public Tile[] getTiles() {
        return playerTiles;
    }

    public void setName(String name) {
        playerName = name;
    }

    public String getName() {
        return playerName;
    }
}
