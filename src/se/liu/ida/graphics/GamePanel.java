package se.liu.ida.graphics;

import javax.swing.JPanel;

import se.liu.ida.objects.Board;

public class GamePanel extends JPanel{
	public static final String CARD_ID = "game panel";
    private static final int SCALE_SIZE = 20;

	private Board board;

	public GamePanel()	{
		this.board = Board.getInstance();
	}

}
