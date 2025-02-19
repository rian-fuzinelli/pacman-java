package entities;

import java.awt.*;

public class Block {
    public int x;
    public int y;
    public int width;
    public int height;
    public Image image;

    public int startX;
    public int startY;

    public Block(Image image, int x, int y, int width, int height){
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startX = x;
        this.startY = y;
    }
}
