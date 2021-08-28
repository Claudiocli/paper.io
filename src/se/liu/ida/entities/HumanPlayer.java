package se.liu.ida.entities;

import java.awt.Color;

/**
 * A HumanPlayer is a player controlled by a person. HumanPlayer adds reactions to key presses on top of abstract class
 * Player. HumanPlayer stores which key speed direction should be updated regarding to in the next tick as well.
 */
public class HumanPlayer extends Player {

    /**
     * Constructs a HumanPlayer on a random spot on the game area with specified color
     * @param color the color of the player
     * @param name the name of player
     */
    public HumanPlayer(Color color, String name) {
        super(color);
        this.name = name;
    }
    /**
     * Constructs a HumanPlayer on a random spot on the game area
     * @param name the name of player
     */
    public HumanPlayer(String name) {
        super();
        this.name = name;
    }

}
