package com.game.Model;

import com.game.View.*;

import javax.swing.*;

/**
 * The parent class for player and platform ones.
 * Any sub-class inherits methods declared/implemented here
 * and becomes a CollisionListener being registered in the
 * CustomStorage to get a notification when a collision occurred.
 * Sub-classes should be put into a frame or other container as a
 * JPanel, since they extend this class as well.
 *
 * @see javax.swing.JPanel
 * @see com.game.View.CustomStorage
 * @see com.game.View.CollisionListener
 * @see com.game.View.CollisionEvent
 *
 * Created by E. Mozharovsky on 19.06.14.
 */
public abstract class Creature extends JPanel implements CollisionListener {
    /**
     * A flag that indicates if further movement possible or not.
     */
    private boolean movementState = true;

    /**
     * The current movement's vector.
     *
     * @see com.game.Model.Creature.Vector
     */
    private Vector vector;

    /**
     * Register this object in the CustomStorage as a CollisionListener.
     *
     * @see com.game.View.CustomStorage
     * @see com.game.View.CollisionListener
     */
    public Creature() {
        CustomStorage.addCollisionListener(this);
    }

    /**
     * Shift the widget on the screen with the given offset params.
     *
     * @param x The current X-coordinate on the frame.
     * @param y The current Y-coordinate on the frame.
     */
    public abstract void updateCoord(int x, int y);

    /**
     * Set a delay time for painting this widget. A big delay
     * will cause a slow movement, a little one in contrast a fast.
     *
     * @param delay A delay period of time (milliseconds).
     */
    protected void setDelay(long delay) {
        try {
            Thread.sleep(delay);
        } catch(Exception ex) { }
    }

    /**
     * Stop painting the widget on the screen. Should be called
     * when a collision detected.
     *
     * @see com.game.View.CollisionListener
     * @param state A state-flag
     *                          true - continue movement,
     *                          false - stop movement.
     */
    public void setMovementState(boolean state) {
        movementState = state;
    }

    /**
     * An identifier of movement state. Used to check if
     * the widget still moves or not.
     *
     * @return The current widget's movement state:
     *                                 true - moves,
     *                                 false - is being stopped.
     */
    public boolean getMovementState() {
        return movementState;
    }

    /**
     * @return The current movement's vector.
     * @see com.game.Model.Creature.Vector
     */
    public Vector getVector() {
        return vector;
    }

    /**
     * Set the updated movement's vector when the widget changes
     * its direction of movement.
     *
     * @param vector A new (updated) movement's vector.
     * @see com.game.Model.Creature.Vector
     */
    public void setVector(Vector vector) {
        this.vector = vector;
    }

    /**
     * A listener's method which stops the painting of this widget-listener.
     *
     * @param ev A collision event (a simple marker), saying that a collision
     *           was detected.
     */
    @Override
    public final void collisionOccurred(CollisionEvent ev) {
        setMovementState(false);
    }

    /**
     * An enum which declares any possible movement's directions,
     * including a non-existing i.e. non-movement state.
     */
    public enum Vector {
        UP,
        DOWN,
        RIGHT,
        LEFT,
        NONE;
    }
}
