package core;

import javax.swing.*;

public class Game {

   public static void main(String[] args) throws Exception{
       int rowCount = 21;
       int columnCount = 19;
       int tileSize = 32;
       int boardWidth = columnCount * tileSize;
       int boardHeight = rowCount * tileSize;

       JFrame frame = new JFrame("Pac Man");
       frame.setSize(boardWidth, boardHeight);
       frame.setLocationRelativeTo(null);
       frame.setResizable(false);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel pacmanGame = new GamePanel();
        frame.add(pacmanGame);
        frame.pack();
        frame.setVisible(true);
   }
}
