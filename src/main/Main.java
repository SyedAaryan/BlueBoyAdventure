package main;

import debugger.Debugger;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Main {

    public static JFrame window;

    public static void main(String[] args) {


        Debugger debugger = new Debugger();

        window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Adventure Game ");

//        window.setUndecorated(true);


        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        // The window will be displayed at the center of the screen
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setUpGame();
        gamePanel.startGameThread();
    }

}
