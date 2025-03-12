package core;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

public class Ghost {
    public int x, y, width, height;
    public Image image;
    public int startX, startY;
    public char direction = 'U';
    public int velocityX = 0;
    public int velocityY = 0;
    public static final int SPEED = 8;

    private Random random = new Random();

    public Ghost(Image image, int x, int y, int width, int height) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startX = x;
        this.startY = y;
        // Direção inicial aleatória
        char[] directions = {'U', 'D', 'L', 'R'};
        this.direction = directions[random.nextInt(4)];
        updateVelocity();
    }

    public void updateDirection(char newDirection) {
        this.direction = newDirection;
        updateVelocity();
        this.x += this.velocityX;
        this.y += this.velocityY;
    }

    public void updateVelocity() {
        if (direction == 'U') {
            velocityX = 0;
            velocityY = -SPEED;
        } else if (direction == 'D') {
            velocityX = 0;
            velocityY = SPEED;
        } else if (direction == 'L') {
            velocityX = -SPEED;
            velocityY = 0;
        } else if (direction == 'R') {
            velocityX = SPEED;
            velocityY = 0;
        }
    }

    public void reset() {
        this.x = startX;
        this.y = startY;
        // Sorteia uma nova direção
        char[] directions = {'U', 'D', 'L', 'R'};
        this.direction = directions[random.nextInt(4)];
        updateVelocity();
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }
}
