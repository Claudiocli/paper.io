package se.liu.ida.objects;

import javax.swing.*;

import se.liu.ida.entities.BotPlayer;
import se.liu.ida.entities.HumanPlayer;
import se.liu.ida.entities.Player;
import se.liu.ida.graphics.Painter;
import se.liu.ida.logic.Direction;
import se.liu.ida.logic.GameController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;

/**
 * The board class is the class responsible for main game logic. This class
 * initializes the tile grid, players and keeps track of them. Board is also
 * specifies key bindings, fills enclosed areas and keeps track of a timer to
 * tick through game logic. Board draws the live scoreboard but uses one or two
 * Painter:s to draw the game area and players on it.
 */
public class Board extends JPanel {
    private static final String MOVE_P1_UP_KEY = "p1 north";
    private static final String MOVE_P1_DOWN_KEY = "p1 south";
    private static final String MOVE_P1_LEFT_KEY = "p1 west";
    private static final String MOVE_P1_RIGHT_KEY = "p1 east";

    private static final String MOVE_P2_UP_KEY = "p2 north";
    private static final String MOVE_P2_DOWN_KEY = "p2 south";
    private static final String MOVE_P2_LEFT_KEY = "p2 west";
    private static final String MOVE_P2_RIGHT_KEY = "p2 east";

    private final int mapHeight;
    private final int mapWidth;
    private transient Tile[][] gameArea;

    private boolean multiplayer;

    private int botNumber;

    private transient ArrayList<Player> players;

    private transient Player p1;
    private transient Player p2;

    private Random r;

    private static Board instance = null;

    public static Board createInstance(String p1name, int mapWidth, int mapHeight, int botNumber) {
        instance = new Board(p1name, mapWidth, mapHeight, botNumber);
        return instance;
    }

    public static Board createInstance(String p1name, String p2name, int mapWidth, int mapHeight, int botNumber) {
        instance = new Board(p1name, p2name, mapWidth, mapHeight, botNumber);
        return instance;
    }

    public static Board getInstance() {
        if (instance == null)
            throw new NullPointerException("You have to create the board first!");
        return instance;
    }

    public int getMapWidth() {
        return this.mapWidth;
    }

    public int getMapHeight() {
        return this.mapHeight;
    }

    /**
     * Creates board for singleplayer
     * 
     * @param p1name    name of player
     * @param mapWidth  width of game area
     * @param mapHeight height of game area
     * @param gameSpeed game speed between 1 and 5, 5 being the fastest
     * @param botNumber number of bots to have in game
     */
    private Board(String p1name, int mapWidth, int mapHeight, int botNumber) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.botNumber = botNumber;
        this.multiplayer = false;
        this.players = new ArrayList<>();
        this.r = new Random();

        this.p1 = new HumanPlayer(p1name);
        this.p2 = null;

        players.add(this.p1);

        initBoard();
    }

    /**
     * Creates board for multiplayer
     * 
     * @param p1name    name of player 1
     * @param p2name    name of player 2
     * @param mapWidth  width of game area
     * @param mapHeight height of game area
     * @param gameSpeed game speed between 1 and 5, 5 being the fastest
     * @param botNumber number of bots to have in game
     */
    private Board(String p1name, String p2name, int mapWidth, int mapHeight, int botNumber) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.botNumber = botNumber;
        this.multiplayer = true;
        this.players = new ArrayList<>();
        this.r = new Random();

        this.p1 = new HumanPlayer(p1name);
        this.p2 = new HumanPlayer(p2name);

        initBoard();
    }

    /**
     * Initializes necessary variables, timer, players etc required for the board
     */
    private void initBoard() {
        this.gameArea = new Tile[this.mapWidth][this.mapHeight];
        for (int i = 0; i < this.mapWidth; i++) {
            for (int j = 0; j < this.mapHeight; j++) {
                gameArea[i][j] = new Tile(i, j);
            }
        }

        specifyKeyActions();

        setBackground(Color.BLACK);

        // Adds new bots and give them a color either from colorList or randomized
        for (int i = 0; i < botNumber; i++)
            players.add(new BotPlayer());

        // Gives each player a starting area
        for (int i = 0; i < players.size(); i++)
            startingArea(players.get(i));

        // Starts a timer to tick the game logic
        final int INITIAL_DELAY = 0;
        final int PERIOD_INTERVAL = 1000 / 60;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(), INITIAL_DELAY, PERIOD_INTERVAL);
    }

    /**
     * Specifies necessary key bindings and key actions for game
     */
    private void specifyKeyActions() {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        if (!multiplayer) {
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), MOVE_P1_UP_KEY);
            am.put(MOVE_P1_UP_KEY, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    p1.changeDirection(Direction.NORTH);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), MOVE_P1_DOWN_KEY);
            am.put(MOVE_P1_DOWN_KEY, new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {
                    p1.changeDirection(Direction.SOUTH);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), MOVE_P1_LEFT_KEY);
            am.put(MOVE_P1_LEFT_KEY, new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {
                    p1.changeDirection(Direction.WEST);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), MOVE_P1_RIGHT_KEY);
            am.put(MOVE_P1_RIGHT_KEY, new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {
                    p1.changeDirection(Direction.EAST);
                }
            });
        } else {
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), MOVE_P1_UP_KEY);
            am.put(MOVE_P1_UP_KEY, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    p1.changeDirection(Direction.NORTH);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), MOVE_P1_DOWN_KEY);
            am.put(MOVE_P1_DOWN_KEY, new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {
                    p1.changeDirection(Direction.SOUTH);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), MOVE_P1_LEFT_KEY);
            am.put(MOVE_P1_LEFT_KEY, new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {
                    p1.changeDirection(Direction.WEST);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), MOVE_P1_RIGHT_KEY);
            am.put(MOVE_P1_RIGHT_KEY, new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {
                    p1.changeDirection(Direction.EAST);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), MOVE_P2_UP_KEY);
            am.put(MOVE_P2_UP_KEY, new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {
                    p2.changeDirection(Direction.NORTH);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), MOVE_P2_DOWN_KEY);
            am.put(MOVE_P2_DOWN_KEY, new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {
                    p2.changeDirection(Direction.SOUTH);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), MOVE_P2_LEFT_KEY);
            am.put(MOVE_P2_LEFT_KEY, new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {
                    p2.changeDirection(Direction.WEST);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), MOVE_P2_RIGHT_KEY);
            am.put(MOVE_P2_RIGHT_KEY, new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {
                    p2.changeDirection(Direction.EAST);
                }
            });
        }

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pause");
        am.put("pause", new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                GameController.getInstance().pause();
            }
        });
    }

    /**
     * Marks all tiles in the starting area of a player to owned by player
     * 
     * @param player player to generate starting area for
     */
    private void startingArea(Player player) {
        for (int i = player.getX() - 1; i <= player.getX() + 1; i++) {
            for (int j = player.getY() - 1; j <= player.getY() + 1; j++) {
                player.addTileToOwned(this.gameArea[i][j]);
            }
        }
    }

    public Tile getTileAt(int i, int j) {
        return this.gameArea[i][j];
    }

    /**
     * Overrides paintComponent and is called whenever everything should be drawn on
     * the screen
     * 
     * @param g Graphics element used to draw elements on screen
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < painters.size(); i++) {
            // Set clipping area for painter
            g.setClip(getWidth(), 0, getWidth(), getHeight());

            // Move graphics to top-left of clipping area
            g.translate(getWidth(), 0);

            // Painter paints area
            painters.get(i).draw(g);

            // Move graphics back to top-left of window
            g.translate(-getWidth(), 0);
        }
        try {
            drawScoreboard(g);

        } catch (IndexOutOfBoundsException ignored) {
        }
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * Draws the live scoreboard up in the rightmost corner
     * 
     * @param g Graphics object received as argument in paintComponent method
     */
    private void drawScoreboard(Graphics g) {
        g.setFont(new Font("Monospaced", Font.PLAIN, 16));
        FontMetrics fontMetrics = g.getFontMetrics();
        int fontHeight = fontMetrics.getHeight();
        int barWidth;
        int barHeight = fontHeight + 4;

        Player player;
        String string;
        Color color;

        double highestPercentOwned = players.get(0).getPercentOwned();
        Collections.sort(players);
        for (int i = 0; i < Integer.min(5, players.size()); i++) {
            player = players.get(i);
            string = String.format("%.2f%% - %s", player.getPercentOwned(), player.getName());
            color = player.getColor();

            barWidth = (int) ((player.getPercentOwned() / highestPercentOwned) * (getWidth() / 4));
            g.setColor(player.getColor());
            g.fillRect(getWidth() - barWidth, barHeight * i, barWidth, barHeight);
            // If color is perceived as dark set the font color to white, else black
            if (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue() < 127) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.BLACK);
            }
            g.drawString(string, 2 + getWidth() - barWidth, barHeight * i + fontHeight);
        }
    }

    /**
     * Method responsible for main logic of the game. Checks collisions and if
     * enclosures should be filled.
     */
    private void tick() {
        Player player;
        tilePlayerMap.clear();
        for (int i = 0; i < players.size(); i++) {
            player = players.get(i);
            player.move();
            // Kill player if player moves outside game area
            if (player.getX() < 0 || player.getX() >= mapWidth || player.getY() < 0 || player.getY() >= mapHeight) {
                player.die();
            } else {
                Tile tile = getTile(player.getX(), player.getY());
                player.checkCollision(tile);
                player.setCurrentTile(tile);
                findCollision(player, tile);

                // If player is outside their owned territory
                if (tile.getOwner() != player && player.isAlive()) {
                    player.contestTile(tile);
                    // If player arrives back to an owned tile
                } else if (player.getTilesContested().size() > 0) {
                    player.contestToOwned();
                    fillEnclosure(player);
                }
            }
            // If BotPlayer is killed, add it to deadBots list
            if (player instanceof BotPlayer && !player.isAlive()) {
                deadBots.add(player);
            }
        }
        respawnBots();

        boolean allKilled = true;
        for (HumanPlayer humanPlayer : humanPlayers) {
            humanPlayer.updateD();
            // Sets painter to stop drawing if humanPlayer is dead
            player_painter.get(humanPlayer).setDraw(humanPlayer.isAlive());
            allKilled = allKilled && !humanPlayer.isAlive();
        }
        if (allKilled) {
            endGame();
        }

        // Remove dead players
        players.removeIf(p -> !p.isAlive());
    }

    /**
     * Method to end game and tell this to PaperIO class
     */
    private void endGame() {
        JOptionPane.showMessageDialog(this, "You lost, game over", "GAME OVER", JOptionPane.PLAIN_MESSAGE);
        actionListener.actionPerformed(new ActionEvent(this, 0, "End Game"));
    }

    /**
     * Method that respawns dead bots after a set interval
     */
    private void respawnBots() {
        for (int i = 0; i < deadBots.size(); i++) {
            if (deadBots.get(i).isAlive()) {
                Player player = new BotPlayer(gameArea.length, gameArea[0].length,
                        new Color((int) (Math.random() * 0x1000000)));
                startingArea(player);
                players.add(player);
                deadBots.remove(deadBots.get(i));
            }
        }
    }

    /**
     * Method that detects player-to-player head on collision
     * 
     * @param player Player you want to check collision for
     * @param tile   Tile that Player currently is on
     */
    private void findCollision(Player player, Tile tile) {
        // If corresponding tile is found in tilePlayerMap
        if (tilePlayerMap.containsKey(tile)) {

            // Iterate through all entries in tilePlayerMap, if the Tile in entry matches
            // Tile in input,
            // compare sizes between players and destroy one of them. The player with the
            // largest tiles contested
            // survives. If both players have the same amount of tiles contested, the player
            // with the most tiles
            // owned survives. If both players have the same amount of tiles contested and
            // tiles owned,
            // the first player added to Players list dies.
            for (Map.Entry<Tile, Player> entry : tilePlayerMap.entrySet()) {
                if (entry.getKey() == tile) {
                    if (entry.getValue().getTilesContested().size() > player.getTilesContested().size()) {
                        entry.getValue().die();
                    } else if (entry.getValue().getTilesContested().size() < player.getTilesContested().size()) {
                        player.die();
                    } else if (entry.getValue().getTilesContested().size() == player.getTilesContested().size()) {
                        if (entry.getValue().getTilesOwned().size() > player.getTilesOwned().size()) {
                            entry.getValue().die();
                        } else {
                            player.die();
                        }
                    }
                }
            }
        } else { // If no corresponding tile is found, add tile and player to tilePlayerMap
            tilePlayerMap.put(tile, player);
        }
        // Remove dead players
        players.removeIf(p -> !p.isAlive());
    }

    /**
     * Controls tick counter of game which is needed to make game smooth.
     */
    private void updateTick() {
        tickCounter++;
        tickCounter %= tickReset;
    }

    /**
     * After a player has traveled out to enclose an area the area needs to be
     * filled. This method depends on that the Player.contestedToOwned() method has
     * been called. The method works by doing a depth first search from each tile
     * adjacent to a tile owned by the player sent as parameter. If the DFS
     * algorithm finds a boundary we know it is not enclosed and should not be
     * filled. The boundary is the smallest rectangle surrounding all owned tiles by
     * the player to minimize cost of method. If the DFS can't find the boundary or
     * if the one the DFS starts on we know it should be filled.
     * 
     * @param player The player whose enclosure to be filled
     */
    private void fillEnclosure(Player player) {
        // Set boundary
        int maxX = 0;
        int minX = gameArea[0].length;
        int maxY = 0;
        int minY = gameArea.length;
        for (Tile t : player.getTilesOwned()) {
            if (t.getX() > maxX)
                maxX = t.getX();
            if (t.getX() < minX)
                minX = t.getX();
            if (t.getY() > maxY)
                maxY = t.getY();
            if (t.getY() < minY)
                minY = t.getY();
        }

        // Necessary collections for DFS to work
        ArrayList<Tile> outside = new ArrayList<>();
        ArrayList<Tile> inside = new ArrayList<>();
        ArrayList<Tile> visited = new ArrayList<>();
        HashSet<Tile> toCheck = new HashSet<>();

        // Add all adjacent tiles
        int y;
        int x;
        for (Tile t : player.getTilesOwned()) {
            y = t.getY();
            x = t.getX();
            if (y - 1 >= 0)
                toCheck.add(gameArea[y - 1][x]);
            if (y + 1 < gameArea.length)
                toCheck.add(gameArea[y + 1][x]);
            if (x - 1 >= 0)
                toCheck.add(gameArea[y][x - 1]);
            if (x + 1 < gameArea[y].length)
                toCheck.add(gameArea[y][x + 1]);
        }

        // Loop over all tiles to do DFS from
        for (Tile t : toCheck) {
            if (!inside.contains(t)) {
                Stack<Tile> stack = new Stack<>();
                boolean cont = true;
                Tile v;
                visited.clear();

                // DFS algorithm
                stack.push(t);
                while ((!stack.empty()) && cont) {
                    v = stack.pop();
                    if (!visited.contains(v) && (v.getOwner() != player)) {
                        y = v.getY();
                        x = v.getX();
                        if (outside.contains(v) // If already declared as outside
                                || x < minX || x > maxX || y < minY || y > maxY // If outside of boundary
                                || x == gameArea[0].length - 1 || x == 0 || y == 0 || y == gameArea.length - 1) { // If
                                                                                                                  // it
                                                                                                                  // is
                                                                                                                  // a
                                                                                                                  // edge
                                                                                                                  // tile
                            cont = false;
                        } else {
                            visited.add(v);
                            if (y - 1 >= 0)
                                stack.push(gameArea[y - 1][x]);
                            if (y + 1 < gameArea.length)
                                stack.push(gameArea[y + 1][x]);
                            if (x - 1 >= 0)
                                stack.push(gameArea[y][x - 1]);
                            if (x + 1 < gameArea[y].length)
                                stack.push(gameArea[y][x + 1]);
                        }
                    }
                }
                if (cont) { // If DFS don't find boundary
                    inside.addAll(visited);
                } else {
                    outside.addAll(visited);
                }
            }
        }

        // Set all enclosed tiles to be owned by player
        for (Tile t : inside) {
            player.addTileToOwned(t);
        }
    }

    /**
     * ScheduleTask is responsible for receiving and responding to timer calls
     */
    private class ScheduleTask extends TimerTask {

        /**
         * Gets called by timer at specified interval. Calls tick at specified rate and
         * repaint each time
         */
        @Override
        public void run() {
            if (!paused) {
                updateTick();
                if (tickCounter == 0) {
                    tick();
                }
                repaint();
            }
        }
    }
}