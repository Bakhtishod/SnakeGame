import java.sql.*;

public class DatabaseManager {
    private final String DB_URL = "jdbc:mysql://localhost:3306/SnakeGameDB";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "bakhtishoddev2004";

    public DatabaseManager() {
        setupDatabase();
    }

    private void setupDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            String createTableQuery = """
                    CREATE TABLE IF NOT EXISTS scores (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        score INT NOT NULL
                    )
                    """;
            stmt.executeUpdate(createTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveScore(String name, int score) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO scores (name, score) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setInt(2, score);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getHighScores() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        String query = "SELECT name, score FROM scores ORDER BY score DESC LIMIT 10";
        return conn.createStatement().executeQuery(query);
    }
}
