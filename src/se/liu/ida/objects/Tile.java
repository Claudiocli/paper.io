package se.liu.ida.objects;

import java.awt.Color;

import se.liu.ida.entities.Player;

/**
 * A tile in the game area. A tile has an x and y position, a color. It can also
 * have a player as owner and a player as contested owner. A tiles color does
 * depend on owner and contested owner.
 */
public class Tile {

    private Player owner;
    private Player contestedOwner;
    // Coords
    private int x;
    private int y;

    /**
     * Initializes a tile at position (x, y)
     * 
     * @param x x position of the tile
     * @param y y position of the tile
     */
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.owner = null;
        this.contestedOwner = null;
    }

    /**
     * Return a certain color accordingly to its <code>owner</code> and/or
     * <code>contestedOwner</code>. By default it returns <code>Color.WHITE</code>
     * 
     * @see Color
     * @return the color of the tile
     */
    public Color getColor() {
        // If a Tile has an owner and Tile is not being contested,
        // returns owner's color darkened
        if (owner != null && contestedOwner == null) {
            return owner.getColor().darker();
        }
        // If Tile has no owner and is being contested,
        // returns contestedOwner's color with an alpha of 100
        else if (owner == null && contestedOwner != null) {
            return (new Color(contestedOwner.getColor().getRed(), contestedOwner.getColor().getGreen(),
                    contestedOwner.getColor().getBlue(), 100));
        }
        // If Tile has an owner and is being contested by someone,
        // returns contestedOwner's color with an alpha of 100
        else if (owner != null && contestedOwner != owner) {
            return blendColors();
        } else {
            return Color.WHITE;
        }
    }

    /**
     * Blends colors of owner and contested owner
     * 
     * @return the blended color
     */
    private Color blendColors() {
        float blendedRed = ((owner.getColor().getRed() / 255f) * (contestedOwner.getColor().getRed() / 255f));
        float blendedGreen = ((owner.getColor().getGreen() / 255f) * (contestedOwner.getColor().getGreen() / 255f));
        float blendedBlue = ((owner.getColor().getBlue() / 255f) * (contestedOwner.getColor().getBlue() / 255f));

        return (new Color(((blendedRed + 1) / 2), ((blendedGreen + 1) / 2), ((blendedBlue + 1) / 2)));
    }

    /**
     * Get owner of tile
     * 
     * @return Player that is owner of tile
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Sets owner to owner of tile and removes current owner and contested owner
     * 
     * @param owner Player to be set as owner of tile
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * @return Player that is contesting Tile
     */
    public Player getContestedOwner() {
        return contestedOwner;
    }

    /**
     * Sets a player as contestant to Tile
     * 
     * @param contestedOwner Player that is contesting Tile
     */
    public void setContestedOwner(Player contestedOwner) {
        this.contestedOwner = contestedOwner;
    }

    /**
     * Get the tiles x-position
     * 
     * @return The x-position of the tile
     */
    public int getX() {
        return x;
    }

    /**
     * Get the tiles y-position
     * 
     * @return The y-position of the tile
     */
    public int getY() {
        return y;
    }

}