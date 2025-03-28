import javax.swing.*;
import java.awt.*;

class GamePanel extends JPanel {
    private final GameLogic gameLogic;

    public GamePanel(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        setPreferredSize(new Dimension(gameLogic.getGridWidth() * gameLogic.getTileSize(),
                gameLogic.getGridHeight() * gameLogic.getTileSize()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameLogic.render(g);
    }
}
