package se.liu.ida.graphics;

import javax.swing.JFrame;

import java.awt.CardLayout;

public class AppFrame extends JFrame {
    private Menu menu;
	private GamePanel gamePanel;

	private static AppFrame instance = null;

	public AppFrame()	{
        setSize(1000, 1000);
		
        menu = new Menu();
		gamePanel = new GamePanel();

        getContentPane().setLayout(new CardLayout());

        getContentPane().add(menu, Menu.CARD_ID);
        getContentPane().add(gamePanel, GamePanel.CARD_ID);
		
		((CardLayout) getContentPane().getLayout()).show(getContentPane(), Menu.CARD_ID);

		pack();
		setResizable(true);
		setVisible(true);
		setTitle("Paper.io");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static AppFrame getInstance()	{
		if (instance == null)
			instance = new AppFrame();
		return instance;
	}
	
}
