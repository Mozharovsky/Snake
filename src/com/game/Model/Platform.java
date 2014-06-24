package com.game.Model;

import com.game.Util.CollisionCenter;
import com.game.View.CollisionEvent;
import com.game.View.CustomStorage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * This Creature's sub-class is a simple platform controlled
 * by the program. Platforms are non-playable objects which
 * do not depend on player's actions. Each platform has its
 * own Image, Size parameters and Coordinates. They move
 * automatically, calculating the point of movement's end and
 * then change the movement Vector. Inherits everything from
 * Creature parent-class as well as Player one. This class
 * implements an ActionListener to provide a possibility to
 * perform the correct way to calculate movement.
 *
 * @see com.game.Model.Creature
 * @see com.game.Model.Creature.Vector
 *
 * Created by E. Mozharovsky on 19.06.14.
 */
public final class Platform extends Creature implements ActionListener {
    /**
     * The platform's image displaying on the screen.
     * This image fills the whole space of JPanel.
     */
    private final Image   img;

    /**
     * The width of platform's image.
     */
    private final int   WIDTH;

    /**
     * The height of platform's image.
     */
    private final int  HEIGHT;

    /**
     * A timer to execute movement actions.
     */
    private    Timer    timer;

    /**
     * The start minimum for LEFT and UP vectors of movement.
     *
     * @see com.game.Model.Creature.Vector
     */
    private int randomOne = 0;

    /**
     * The start minimum for RIGHT and DOWN vectors of movement.
     *
     * @see com.game.Model.Creature.Vector
     */
    private int randomTwo = 650;

    /**
     * Create a new Platform object with given arguments, initializing
     * platform's image, X and Y offsets.
     *
     * @param img The platform's image.
     * @param x   The initial X-coordinate on the frame.
     * @param y   The initial Y-coordinate on the frame.
     */
    public Platform(final Image img, int x, int y) {
        this.img = img;
        WIDTH = img.getWidth(this);
        HEIGHT = img.getHeight(this);

        setBounds(x, y, WIDTH, HEIGHT);
        setVector(getInitVector()); // init the initial vector randomly

        // set timer on repeating each 20 milliseconds and with
        // a 100 milliseconds delay, then start it
        timer = new Timer(20, this);
        timer.setInitialDelay(100);
        timer.start();
    }

    /**
     * Stop the timer once the movement is no longer processed.
     */
    private void timerStop() {
        timer.stop();
    }

    /**
     * A random generating of the initial movement vector.
     *
     * @return The initial vector of movement.
     */
    private Vector getInitVector() {
        int random = (int)(Math.random() * 8);
        switch (random) {
            case 1: return Vector.UP;
            case 2: return Vector.DOWN;
            case 3: return Vector.RIGHT;
            case 4: return Vector.LEFT;
        }
        return Vector.UP;
    }

    /**
     * Random updating of movement's vector after the previous movement
     * was finished. We check if the current vector is not equals to the
     * one we have got via random selecting, if it is then go next and if
     * it is not update the vector.
     */
    private void generateVector() {
        int random = (int)(Math.random() * 8);

        try {
            switch (random) {
                case 1: case 5:
                    if (getVector().equals(Vector.UP)) {
                        break;
                    }

                    setVector(Vector.UP);
                    break;
                case 2: case 6:
                    if (getVector().equals(Vector.DOWN)) {
                        break;
                    }

                    setVector(Vector.DOWN);
                    break;
                case 3: case 7:
                    if (getVector().equals(Vector.RIGHT)) {
                        break;
                    }

                    setVector(Vector.RIGHT);
                    break;
                case 4: case 8:
                    if (getVector().equals(Vector.LEFT)) {
                        break;
                    }

                    setVector(Vector.LEFT);
                    break;
            }
        } catch (StackOverflowError ex) {
            ex.printStackTrace();
            generateVector(); // try again
        }
    }

    // movement actions

    /**
     * Shift the platform to the right until the randomly
     * generated scope is reached, then randomly change the
     * movement's vector.
     */
    private void moveRight() {
        if(getX() + getWidth() < randomTwo) {
            updateCoord(getX() + 10, getY());
            repaint();
        } else {
            randomTwo = (int)(Math.random() * 650);
            generateVector();
        }
    }

    /**
     * Shift the platform to the left until the randomly
     * generated scope is reached, then randomly change the
     * movement's vector.
     */
    private void moveLeft() {
        if(getX() > randomOne) {
            updateCoord(getX() - 10, getY());
        } else {
            randomOne = (int)(Math.random() * 100);
            generateVector();
        }
    }

    /**
     * Shift the platform to the up until the randomly
     * generated scope is reached, then randomly change the
     * movement's vector.
     */
    private void moveUp() {
        if(getY() > randomOne) {
            updateCoord(getX(), getY() - 10);
        } else {
            randomOne = (int)(Math.random() * 100);
            generateVector();
        }
    }

    /**
     * Shift the platform to the down until the randomly
     * generated scope is reached, then randomly change the
     * movement's vector.
     */
    private void moveDown() {
        if(getY() + getHeight() < randomTwo) {
            updateCoord(getX(), getY() + 10);
        } else {
            randomTwo = (int)(Math.random() * 650);
            generateVector();
        }
    }

    /**
     * Inherit the method from Creature paren-class and check if
     * further movement is possible.
     *
     * @param x The current X-coordinate on the frame.
     * @param y The current Y-coordinate on the frame.
     */
    @Override
    public void updateCoord(int x, int y) {
        if(getMovementState()) {
            setBounds(x, y, WIDTH, HEIGHT);
            repaint();
        }
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
            timerStop();
        }

        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }

    /**
     * Invoke each 20 milliseconds, here we check vector and do movement
     * actions depending on it. All vector changes are being processed inside
     * of movement actions methods.
     *
     * @param ev An ActionEvent object.
     */
    @Override
    public void actionPerformed(ActionEvent ev) {
        switch(getVector()) {
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
            case LEFT:
                moveLeft();
                break;
            case RIGHT:
                moveRight();
                break;
        }
    }
}
