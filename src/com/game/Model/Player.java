package com.game.Model;

import com.game.Util.CollisionCenter;
import com.game.View.CollisionEvent;
import com.game.View.CustomStorage;

import javax.swing.*;
import java.awt.*;

/**
 * The sub-class of Creature  which is going to be
 * controlled by the player and his actions. This is
 * just a simple JPanel which draws an image loaded
 * in the static initializer (as well as other parameters)
 * each time paintComponent() method is been invoked. There
 * should be the only Player in game. Inherits everything
 * from Creature and JPanel.
 *
 * @see com.game.Model.Creature
 * @see javax.swing.JPanel
 *
 * Created by E. Mozharovsky on 19.06.14.
 */
public final class Player extends Creature {
    // constants loading in the static initializer

    /**
     * The player's image displaying on the screen.
     * This image fills the whole space of JPanel.
     */
    private static final Image img;

    /**
     * The width of player's image.
     */
    private static final int WIDTH;

    /**
     * The height of player's image.
     */
    private static final int HEIGHT;

    /**
     * Initializing of constants (we do not need to use specific ones, since
     * there is the only player in game).
     */
    static {
        img = new ImageIcon("resources/Cube.png").getImage();
        WIDTH  = img.getWidth(null);
        HEIGHT = img.getHeight(null);
    }

    /**
     * Create a new Player object with initialized constants and
     * the specific coordinates on the frame. The initial vector
     * is NONE.
     *
     * @see com.game.Model.Creature
     * @see com.game.Model.Creature.Vector
     */
    public Player() {
        super();

        setVector(Vector.NONE);
        setBounds(50, 50, WIDTH, HEIGHT);
    }

    /**
     * Draw an image on the updated panel (with new coordinates).
     * Generate a new CollisionEvent and ask CollisionCenter to broadcast
     * it to all CollisionListener objects if a collision was detected.
     *
     * @see com.game.Util.CollisionCenter
     * @see com.game.View.CollisionEvent
     * @see com.game.View.CollisionListener
     *
     * @param g The Graphics object we use to paint this widget on.
     */
    @Override
    protected final void paintComponent(Graphics g) {
        if(!CollisionCenter.isMovementPossible()) {
            CustomStorage.broadcastCollisionEvent(new CollisionEvent() {
            });
        }

        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }

    /**
     * Inherit the method from Creature paren-class and check if
     * further movement is possible. Also provides a possibility
     * to make the movement slower (originally, it was designed to
     * change the speed of painting all widgets).
     *
     * @param x The current X-coordinate on the frame.
     * @param y The current Y-coordinate on the frame.
     */
    @Override
    public void updateCoord(int x, int y) {
        if(canMove()) {
            setBounds(x, y, WIDTH, HEIGHT);
            setDelay(0); // if we want to make a movement slower
            repaint();   // update the model on the screen
        }
    }
}
