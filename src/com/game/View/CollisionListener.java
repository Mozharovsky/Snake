package com.game.View;

/**
 * The listener interface for receiving collision events.
 * Each object implements this listener is registered in
 * CustomStorage to be capable of being informed once a
 * collision event is generated and therefore sent to
 * all collision listeners, doing something about this.
 *
 * @see com.game.View.CustomStorage
 * @see com.game.View.CollisionEvent
 *
 * Created by E. Mozharovsky on 19.06.14.
 */
public interface CollisionListener {
    /**
     * Invoke when a collision between some elements in game found by the
     * CollisionCenter, thus each listener should stop any movement.
     *
     * @param ev A collision event (a simple marker), saying that a collision
     *           detected.
     */
    void collisionOccurred(CollisionEvent ev);
}
