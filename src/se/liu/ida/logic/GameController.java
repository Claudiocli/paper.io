package se.liu.ida.logic;

import se.liu.ida.objects.Board;

public class GameController {

	private Board board;
	
	private boolean paused;

	private static GameController instance = null;

	private GameController()	{
		this.paused = false;
	}

	public void createBoard(String p1Name, int mapWidth, int mapHeight, int gameSpeed, int botNumber)	{
		// TODO: set the gamespeed
		this.board = Board.createInstance(p1Name, mapWidth, mapHeight, botNumber);
	}
	public void createBoard(String p1Name, String p2Name, int mapWidth, int mapHeight, int gameSpeed, int botNumber)	{
		// TODO: set the gamespeed
		this.board = Board.createInstance(p1Name, p2Name, mapWidth, mapHeight, botNumber);
	}

	public void start()	{

	}
	public void stop()	{

	}
	public void pause()	{
		this.paused = true;
	}
	public void unpause()	{
		this.paused = false;
	}

	public Boolean isPaused()	{return this.paused;}

	public static GameController getInstance()	{
		if (instance == null)
			instance = new GameController();
		return instance;
	}

}
