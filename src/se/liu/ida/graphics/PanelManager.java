package se.liu.ida.graphics;

import java.awt.CardLayout;

import se.liu.ida.logic.GameController;

public class PanelManager {

    /**
     * Sets the card to display
     * @param id The card id
     */
    private void changeCard(String id){
        CardLayout cardLayout = (CardLayout) AppFrame.getInstance().getContentPane().getLayout();
        if(id.equals(GamePanel.CARD_ID)){
            cardLayout.show(AppFrame.getInstance().getContentPane(), GamePanel.CARD_ID);
            GameController.getInstance().unpause();
        }
		if(id.equals(Menu.CARD_ID)){
            cardLayout.show(AppFrame.getInstance().getContentPane(), Menu.CARD_ID);
            GameController.getInstance().pause();
        }
    }
}
