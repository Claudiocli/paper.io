package se.liu.ida.entities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import se.liu.ida.logic.Direction;
import se.liu.ida.objects.Board;
import se.liu.ida.objects.Tile;

/**
 * An abstract class for a general player in the game. Human player and bot
 * player differs a bit but their common logic is specified here. It keeps track
 * of players position, speed, color, owned and contested tiles and name. Two
 * players can also be compared that compares number of owned tiles of the
 * player.
 */
public abstract class Player implements Comparable<Player> {
    // Current coords
    protected int y;
    protected int x;
    // Color of the player
    protected Color color;
    // The name of this Player
    protected String name;
    protected Boolean isAlive;
    // Array to store each tile owned by the player
    protected ArrayList<Tile> tilesOwned;
    // Array to store each contested tile by the player (aka the player's tail)
    protected ArrayList<Tile> tilesContested;
    // Enum that indicate the current direction
    protected Direction currentDirection;
    // Util random object
    protected Random r;

    /**
     * Initializes a player on a random spot on the game area with specified color
     * 
     * @param color the color of the player
     */
    protected Player(Color color) {
        this.isAlive = true;
        this.tilesOwned = new ArrayList<>();
        this.tilesContested = new ArrayList<>();
        this.r = new Random();

        this.color = color;

        var width = Board.getInstance().getMapWidth();
        var height = Board.getInstance().getMapHeight();
        while ((this.x - 3 < 0 || this.x + 3 >= width) || (this.y - 3 < 0 || this.y + 3 >= height)) {
            this.x = r.nextInt(width);
            this.y = r.nextInt(height);
        }

        var rand = r.nextDouble();
        if (rand < 1d)
            currentDirection = Direction.NORTH;
        if (rand < 0.75d)
            currentDirection = Direction.EAST;
        if (rand < 0.5d)
            currentDirection = Direction.SOUTH;
        if (rand < 0.25d)
            currentDirection = Direction.WEST;
    }

    protected Player() {
        this.isAlive = true;
        this.tilesOwned = new ArrayList<>();
        this.tilesContested = new ArrayList<>();
        this.r = new Random();

        this.color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));

        var width = Board.getInstance().getMapWidth();
        var height = Board.getInstance().getMapHeight();

        var isTooClose = false;

        for (var i = this.x - 1; i < this.x + 1; i++)
            for (var j = this.y - 1; j < this.y + 1; j++)
                if (Board.getInstance().getTileAt(i, j).getOwner() != null)
                    isTooClose = true;
        

        while ((this.x - 3 < 0 || this.x + 3 >= width) || (this.y - 3 < 0 || this.y + 3 >= height) || isTooClose) {
            this.x = r.nextInt(width);
            this.y = r.nextInt(height);
        }

        var rand = r.nextDouble();
        if (rand < 1d)
            currentDirection = Direction.NORTH;
        if (rand < 0.75d)
            currentDirection = Direction.EAST;
        if (rand < 0.5d)
            currentDirection = Direction.SOUTH;
        if (rand < 0.25d)
            currentDirection = Direction.WEST;
    }

    /**
     * Move the player in the currentDirection
     */
    public void move() {
        this.x += this.currentDirection.getX();
        this.y += this.currentDirection.getY();
    }
    
    public void changeDirection(Direction d)    {this.currentDirection = d;}

    /**
     * Logic for when player gets killed. Turns all associated tiles to neutral
     */
    public void die() {
        isAlive = false;
        // FIXME: really bad use of clone! Don't use clone if you can!
        ArrayList<Tile> ownedTilesCopy = (ArrayList<Tile>) tilesOwned.clone();
        ArrayList<Tile> contestedTilesCopy = (ArrayList<Tile>) tilesContested.clone();
        for (int i = 0; i < ownedTilesCopy.size(); i++) {
            ownedTilesCopy.get(i).setOwner(null);
        }

        for (int i = 0; i < contestedTilesCopy.size(); i++) {
            contestedTilesCopy.get(i).setContestedOwner(null);
        }
        tilesOwned.clear();
        tilesContested.clear();

    }

    /**
     * Add tile to players list of owned tiles
     * 
     * @param t Tile to be added to players owned list
     */
    public void addTileToOwned(Tile t) {
        tilesOwned.add(t);
        t.setOwner(this);
        t.setContestedOwner(null);
    }

    /**
     * Get as a percentage how much of the total game area a player owns
     * 
     * @return percentage of how much of the total game area a player owns
     */
    public double getPercentOwned() {
        return 100 * getTilesOwned().size()
                / (double) (Board.getInstance().getMapHeight() * Board.getInstance().getMapWidth());
    }

    /**
     * Add tile to players list of contested tiles
     * 
     * @param t Tile to be added to players contested list
     */
    public void contestTile(Tile t) {
        tilesContested.add(t);
        t.setContestedOwner(this);
    }

    /**
     * Sets contested tiles to owned by player
     */
    public void contestToOwned() {
        for (Tile t : tilesContested) {
            addTileToOwned(t);
        }
        tilesContested.clear();
    }

    /**
     * The x position in the tile system
     * 
     * @return x position in the tile system
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * The y position in the tile system
     * 
     * @return y position in the tile system
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return color of the player
     */
    public Color getColor() {
        return color;
    }

    /**
     * Get name of player
     * 
     * @return Name of player
     */
    public String getName() {
        return name;
    }

    /**
     * Get the status of player
     * 
     * @return true if the player is alive, false otherwise
     */
    public Boolean isAlive() {
        return isAlive;
    }

    /**
     * Get tiles owned by player
     * 
     * @return Tiles owned by player
     */
    public ArrayList<Tile> getTilesOwned() {
        return tilesOwned;
    }

    /**
     * Get tiles contested by player
     * 
     * @return Tiles contested by player
     */
    public List<Tile> getTilesContested() {
        return tilesContested;
    }

    /**
     * Compares two players by the number of tiles owned.
     * 
     * @param player Player to compare this to
     * @return 1 if this owns more tiles than player, -1 if player owns more tiles
     *         than this or 0 otherwise
     */
    public int compareTo(Player player) {
        return Integer.compare(player.getTilesOwned().size(), tilesOwned.size());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof Player))
            return false;
        Player other = (Player) obj;
        return (this.color == other.color) && (this.name.equals(other.name)) && (this.x == other.x)
                && (this.y == other.y) && (this.tilesContested.equals(other.tilesContested))
                && (this.tilesOwned.equals(other.tilesOwned)) && (this.isAlive == other.isAlive);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() + this.color.hashCode();
    }
}