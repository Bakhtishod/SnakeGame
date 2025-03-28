import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;


public class GameLogic {
    private final int gridWidth;
    private final int gridHeight;
    private final int tileSize;
    private final int delay = 150;

    private ArrayList<Point> snake;
    private Point food;
    private ArrayList<Point> rocks;
    private String direction = "UP";
    private boolean running = true;
    private Runnable onGameOverCallback;


    public GameLogic(int gridWidth, int gridHeight, int tileSize) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.tileSize = tileSize;
    }

    public void initGame() {
        snake = new ArrayList<>();
        snake.add(new Point(gridWidth / 2, gridHeight / 2));
        snake.add(new Point(gridWidth / 2, gridHeight / 2 + 1));

        direction = "UP";
        running = true;
        rocks = generateRocks();
        spawnFood();
    }

    public void stopGame() {
        running = false;
    }

    public void handleKeyInput(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                if (!direction.equals("DOWN")) direction = "UP";
            }
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                if (!direction.equals("UP")) direction = "DOWN";
            }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                if (!direction.equals("RIGHT")) direction = "LEFT";
            }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                if (!direction.equals("LEFT")) direction = "RIGHT";
            }
        }
    }

    public void setOnGameOverCallback(Runnable callback) {
        this.onGameOverCallback = callback;
    }

    private void onGameOver() {
        if (onGameOverCallback != null) {
            onGameOverCallback.run();
        }
    }


    private ArrayList<Point> generateRocks() {
        ArrayList<Point> rocks = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            Point rock;
            do {
                rock = new Point(random.nextInt(gridWidth), random.nextInt(gridHeight));
            } while (rocks.contains(rock));
            rocks.add(rock);
        }
        return rocks;
    }

    private void spawnFood() {
        Random random = new Random();
        do {
            food = new Point(random.nextInt(gridWidth), random.nextInt(gridHeight));
        } while (snake.contains(food) || rocks.contains(food));
    }

    public void gameLoop() {
        if (!running) {
            return;
        }

        Point head = snake.get(0);
        Point newHead = switch (direction) {
            case "UP" -> new Point(head.x, head.y - 1);
            case "DOWN" -> new Point(head.x, head.y + 1);
            case "LEFT" -> new Point(head.x - 1, head.y);
            case "RIGHT" -> new Point(head.x + 1, head.y);
            default -> head;
        };

        if (newHead.x < 0 || newHead.x >= gridWidth || newHead.y < 0 || newHead.y >= gridHeight
                || snake.contains(newHead) || rocks.contains(newHead)) {
            running = false;
            onGameOver();
            return;
        }

        snake.add(0, newHead);

        if (newHead.equals(food)) {
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }


    public void render(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                g.drawRect(x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }

        g.setColor(Color.RED);
        g.fillOval(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

        g.setColor(Color.GRAY);
        for (Point rock : rocks) {
            g.fillRect(rock.x * tileSize, rock.y * tileSize, tileSize, tileSize);
        }

        g.setColor(Color.GREEN);
        for (Point part : snake) {
            g.fillRect(part.x * tileSize, part.y * tileSize, tileSize, tileSize);
        }
    }

    public int getSnakeLength() {
        return snake.size() - 2;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getDelay() {
        return delay;
    }
}
