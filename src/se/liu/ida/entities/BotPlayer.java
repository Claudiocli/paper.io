package se.liu.ida.entities;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import se.liu.ida.logic.Direction;
import se.liu.ida.objects.Board;

/**
 * BotPlayer adds bot movement on top of abstract class Player. Movement is
 * randomised as long as it is valid moves. A BotPlayer has a random Pokemon
 * name.
 */
public class BotPlayer extends Player {

    /**
     * Constructs a new BotPLayer on a random spot on the game area with specified
     * color with a randomized direction
     * 
     * @param color the color of the player
     */
    public BotPlayer(Color color) {
        super(color);

        ArrayList<String> names = new ArrayList<>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(
                    new InputStreamReader(this.getClass().getResourceAsStream("res/pokemon_names.txt")));
            while (br.ready())
                names.add(br.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        this.name = names.get(this.r.nextInt(names.size()));
    }

    /**
     * Constructs a new BotPLayer on a random spot on the game area with random
     * color
     */
    public BotPlayer() {
        super();

        ArrayList<String> names = new ArrayList<>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(
                    new InputStreamReader(this.getClass().getResourceAsStream("res/pokemon_names.txt")));
            while (br.ready())
                names.add(br.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        this.name = names.get(this.r.nextInt(names.size()));
    }

    /**
     * Each time a bot moves, it will choose a random direction. If the choosen one
     * it's going to bump into a border, it will recalculate a random direction
     */
    @Override
    public void move() {
        super.move();
        double rand = r.nextDouble();
        if (rand < 1d)
            currentDirection = Direction.NORTH;
        if (rand < 0.75d)
            currentDirection = Direction.EAST;
        if (rand < 0.5d)
            currentDirection = Direction.SOUTH;
        if (rand < 0.25d)
            currentDirection = Direction.WEST;
        while ((this.x + this.currentDirection.getX() < 0
                || this.x + this.currentDirection.getX() >= Board.getInstance().getMapWidth())
                || (this.y + this.currentDirection.getY() < 0
                        || this.y + this.currentDirection.getY() >= Board.getInstance().getMapHeight())) {
            if (rand < 1d)
                currentDirection = Direction.NORTH;
            if (rand < 0.75d)
                currentDirection = Direction.EAST;
            if (rand < 0.5d)
                currentDirection = Direction.SOUTH;
            if (rand < 0.25d)
                currentDirection = Direction.WEST;
        }
    }

}