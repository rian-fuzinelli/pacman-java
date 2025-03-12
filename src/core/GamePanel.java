package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private int rowCount = 21;
    private int columnCount = 19;
    public static final int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    // Imagens dos elementos
    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    // Mapa de tiles:
    // X = parede, O = pular, P = Pacman, ' ' = comida
    // Fantasmas: b = azul, o = laranja, p = rosa, r = vermelho
    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    private HashSet<Wall> walls;
    private HashSet<Food> foods;
    private HashSet<Ghost> ghosts;
    private Pacman pacman;

    private Timer gameLoop;
    private char[] directions = {'U', 'D', 'L', 'R'};
    private Random random = new Random();
    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;

    public GamePanel() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        // Carrega as imagens
        wallImage = new ImageIcon("res/wall.png").getImage();
        blueGhostImage = new ImageIcon("res/blueGhost.png").getImage();
        orangeGhostImage = new ImageIcon("res/orangeGhost.png").getImage();
        pinkGhostImage = new ImageIcon("res/pinkGhost.png").getImage();
        redGhostImage = new ImageIcon("res/redGhost.png").getImage();

        pacmanUpImage = new ImageIcon("res/pacmanUp.png").getImage();
        pacmanDownImage = new ImageIcon("res/pacmanDown.png").getImage();
        pacmanLeftImage = new ImageIcon("res/pacmanLeft.png").getImage();
        pacmanRightImage = new ImageIcon("res/pacmanRight.png").getImage();

        loadMap();

        // Define direção aleatória para cada fantasma
        for (Ghost ghost : ghosts) {
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }

        gameLoop = new Timer(50, this); // 20 fps (1000/50)
        gameLoop.start();
    }

    public void loadMap() {
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();

        for (int r = 0; r < rowCount; r++) {
            String row = tileMap[r];
            for (int c = 0; c < columnCount; c++) {
                char ch = row.charAt(c);
                int x = c * tileSize;
                int y = r * tileSize;
                switch (ch) {
                    case 'X':
                        walls.add(new Wall(wallImage, x, y, tileSize, tileSize));
                        break;
                    case 'b':
                        ghosts.add(new Ghost(blueGhostImage, x, y, tileSize, tileSize));
                        break;
                    case 'o':
                        ghosts.add(new Ghost(orangeGhostImage, x, y, tileSize, tileSize));
                        break;
                    case 'p':
                        ghosts.add(new Ghost(pinkGhostImage, x, y, tileSize, tileSize));
                        break;
                    case 'r':
                        ghosts.add(new Ghost(redGhostImage, x, y, tileSize, tileSize));
                        break;
                    case 'P':
                        pacman = new Pacman(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case ' ':
                        foods.add(new Food(x + 14, y + 14, 4, 4));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        pacman.draw(g);
        for (Ghost ghost : ghosts) {
            ghost.draw(g);
        }
        for (Wall wall : walls) {
            wall.draw(g);
        }
        for (Food food : foods) {
            food.draw(g);
        }
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.setColor(Color.WHITE);
        if (gameOver) {
            g.drawString("Game Over: " + score, tileSize / 2, tileSize / 2);
        } else {
            g.drawString("Lives: " + lives + " Score: " + score, tileSize / 2, tileSize / 2);
        }
    }

    public void move() {
        // Movimento do Pacman
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;
        for (Wall wall : walls) {
            if (collision(pacman.x, pacman.y, pacman.width, pacman.height,
                    wall.x, wall.y, wall.width, wall.height)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        // Movimento dos fantasmas
        for (Ghost ghost : ghosts) {
            if (collision(ghost.x, ghost.y, ghost.width, ghost.height,
                    pacman.x, pacman.y, pacman.width, pacman.height)) {
                lives--;
                if (lives == 0) {
                    gameOver = true;
                    return;
                }
                resetPositions();
            }
            if (ghost.y == tileSize * 9 && ghost.direction != 'U' && ghost.direction != 'D') {
                ghost.updateDirection('U');
            }
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;
            for (Wall wall : walls) {
                if (collision(ghost.x, ghost.y, ghost.width, ghost.height,
                        wall.x, wall.y, wall.width, wall.height) ||
                        ghost.x <= 0 ||
                        ghost.x + ghost.width >= boardWidth) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }
        }

        // Colisão com a comida
        Food foodEaten = null;
        for (Food food : foods) {
            if (collision(pacman.x, pacman.y, pacman.width, pacman.height,
                    food.x, food.y, food.width, food.height)) {
                foodEaten = food;
                score += 10;
            }
        }
        if (foodEaten != null) {
            foods.remove(foodEaten);
        }

        if (foods.isEmpty()) {
            loadMap();
            resetPositions();
        }
    }

    public boolean collision(int ax, int ay, int aw, int ah,
                             int bx, int by, int bw, int bh) {
        return ax < bx + bw && ax + aw > bx && ay < by + bh && ay + ah > by;
    }

    public void resetPositions() {
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        for (Ghost ghost : ghosts) {
            ghost.reset();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        }
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) {
            pacman.updateDirection('U');
        } else if (key == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D');
        } else if (key == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L');
        } else if (key == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R');
        }
        // Atualiza a imagem do Pacman conforme a direção
        if (pacman.direction == 'U') {
            pacman.image = pacmanUpImage;
        } else if (pacman.direction == 'D') {
            pacman.image = pacmanDownImage;
        } else if (pacman.direction == 'L') {
            pacman.image = pacmanLeftImage;
        } else if (pacman.direction == 'R') {
            pacman.image = pacmanRightImage;
        }
    }
}
