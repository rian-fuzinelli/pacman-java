package core;

import java.awt.Graphics;
import java.awt.Image;

public class Pacman {
    public int x, y, width, height;
    public Image image;
    public int startX, startY;
    public char direction = 'R'; // inicia para a direita
    public int velocityX = 0;
    public int velocityY = 0;
    public static final int SPEED = 8;

    public Pacman(Image image, int x, int y, int width, int height) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startX = x;
        this.startY = y;
        updateVelocity();
    }

    public void updateDirection(char newDirection) {
        // Atualiza a direção e a velocidade
        this.direction = newDirection;
        updateVelocity();
        // Move o Pacman de acordo com a velocidade
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
        this.velocityX = 0;
        this.velocityY = 0;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }
}
