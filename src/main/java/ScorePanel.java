import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {
    private JLabel scoreLabel;
    private JLabel timeLabel;

    public ScorePanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 30));
        setBackground(Color.LIGHT_GRAY);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scoreLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(scoreLabel, BorderLayout.WEST);

        timeLabel = new JLabel("Time: 0s");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(timeLabel, BorderLayout.EAST);
    }

    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    public void updateTime(int time) {
        timeLabel.setText("Time: " + time + "s");
    }
}
