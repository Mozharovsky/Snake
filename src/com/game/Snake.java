package com.game;

import com.game.Handler.GameHandler;

/**
 * Created by E. Mozharovsky on 19.06.14.
 */
public class Snake {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new GameHandler(0, 0));
    }
}
