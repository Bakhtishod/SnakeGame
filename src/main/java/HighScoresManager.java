import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HighScoresManager {
    private final DatabaseManager databaseManager;

    public HighScoresManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void showHighScores(JFrame parent) {
        try (ResultSet rs = databaseManager.getHighScores()) {
            StringBuilder scores = new StringBuilder("High Scores:\n");
            while (rs.next()) {
                scores.append(rs.getString("name")).append(": ").append(rs.getInt("score")).append("\n");
            }
            JOptionPane.showMessageDialog(parent, scores.toString(), "High Scores", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
