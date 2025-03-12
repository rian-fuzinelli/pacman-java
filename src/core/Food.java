package core;

import java.awt.Color;
import java.awt.Graphics;

public class Food {
    public int x, y, width, height;

    public Food(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }
}
