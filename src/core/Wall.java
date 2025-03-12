package core;

import java.awt.Graphics;
import java.awt.Image;

public class Wall {
    public int x, y, width, height;
    public Image image;

    public Wall(Image image, int x, int y, int width, int height) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }
}
