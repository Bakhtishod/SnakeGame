import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SnakeGame extends JFrame {
    private final int TILE_SIZE = 25;
    private final int GRID_WIDTH = 20;
    private final int GRID_HEIGHT = 20;
    private final CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel;
    private final GameLogic gameLogic;
    private final DatabaseManager databaseManager;
    private final HighScoresManager highScoresManager;
    private Timer gameTimer;
    private ScorePanel scorePanel;
    private Timer timer;
    private int elapsedTime = 0;

    public SnakeGame() {
        gameLogic = new GameLogic(GRID_WIDTH, GRID_HEIGHT, TILE_SIZE);
        databaseManager = new DatabaseManager();
        highScoresManager = new HighScoresManager(databaseManager);
        initUI();
    }

    private void initUI() {
        setTitle("Snake Game");
        setSize(GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE + 80);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(cardLayout);
        mainPanel.add(createStartScreen(), "StartScreen");
        mainPanel.add(new GamePanel(gameLogic), "Game");
        add(mainPanel, BorderLayout.CENTER);

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                gameLogic.handleKeyInput(e);
            }
        });

        setVisible(true);
        cardLayout.show(mainPanel, "StartScreen");
    }


    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");

        JMenuItem restart = new JMenuItem("Restart");
        restart.addActionListener(e -> restartGame());
        menu.add(restart);

        JMenuItem highScores = new JMenuItem("High Scores");
        highScores.addActionListener(e -> highScoresManager.showHighScores(this));
        menu.add(highScores);

        JMenuItem backToMenu = new JMenuItem("Back to Menu");
        backToMenu.addActionListener(e -> backToMenu());
        menu.add(backToMenu);

        menuBar.add(menu);
        return menuBar;
    }
    private JPanel createStartScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Snake Game");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(title, gbc);

        JComboBox<String> levelSelector = new JComboBox<>();
        for (int i = 1; i <= 10; i++) {
            levelSelector.addItem("Level " + i);
        }
        gbc.gridy = 1;
        panel.add(levelSelector, gbc);

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> startGame());
        gbc.gridy = 2;
        panel.add(startButton, gbc);

        JButton highScoresButton = new JButton("High Scores");
        highScoresButton.addActionListener(e -> highScoresManager.showHighScores(this));
        gbc.gridy = 3;
        panel.add(highScoresButton, gbc);

        return panel;
    }

    private void startGame() {
        gameLogic.initGame();
        elapsedTime = 0;

        if (scorePanel == null) {
            scorePanel = new ScorePanel();
            add(scorePanel, BorderLayout.NORTH);
        }
        scorePanel.setVisible(true);
        revalidate();

        setJMenuBar(createMenuBar());
        revalidate();

        gameLogic.setOnGameOverCallback(() -> {
            if (gameTimer != null) {
                gameTimer.stop();
            }
            if (timer != null) {
                timer.stop();
            }
            SwingUtilities.invokeLater(this::showGameOverDialog);
        });

        gameTimer = new Timer(gameLogic.getDelay(), e -> {
            gameLogic.gameLoop();
            mainPanel.repaint();
        });
        gameTimer.start();

        timer = new Timer(1000, e -> {
            elapsedTime++;
            if (scorePanel != null) {
                scorePanel.updateTime(elapsedTime);
                scorePanel.updateScore(gameLogic.getSnakeLength());
            }
        });
        timer.start();

        cardLayout.show(mainPanel, "Game");
    }

    private void showGameOverDialog() {
        String name = JOptionPane.showInputDialog(this, "Game Over! Enter your name:");
        if (name != null && !name.isEmpty()) {
            databaseManager.saveScore(name, gameLogic.getSnakeLength());
        }
        backToMenu();
    }

    private void restartGame() {
        gameLogic.initGame();
    }

    private void backToMenu() {
        if (timer != null) {
            timer.stop();
        }

        if (scorePanel != null) {
            scorePanel.setVisible(false);
        }

        setJMenuBar(null);
        revalidate();

        gameLogic.stopGame();
        cardLayout.show(mainPanel, "StartScreen");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SnakeGame::new);
    }
}
