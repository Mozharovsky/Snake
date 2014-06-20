package com.game.View;

import java.util.ArrayList;

/**
 * A simple storage for all kind of CollisionListener objects,
 * which registers each object implementing CollisionListener interface,
 * providing ways to get a copy of this collection with listeners also
 * clear it when it is necessary, and which sends a created CollisionEvent
 * to all collision listeners in the collection.
 *
 * @see com.game.View.CollisionEvent
 * @see com.game.View.CollisionListener
 *
 * Created by E. Mozharovsky on 19.06.14.
 */
public abstract class CustomStorage {
    /**
     * A collection which stores every registered CollisionListener.
     */
    private static ArrayList<CollisionListener> collisionListeners = new ArrayList<>();

    /**
     * Register an object which implements CollisionListener interface,
     * so when a collision is found all listeners will be noticed.
     *
     * @param listener An object implementing CollisionListener interface.
     * @return An adding status (true - success, false - fail to register).
     */
    public static boolean addCollisionListener(CollisionListener listener) {
        if(listener.equals(null))
            return false;

        collisionListeners.add(listener);
        return true;
    }

    /**
     * Send a CollisionEvent to all registered listeners.
     *
     * @param ev An object of CollisionEvent (a simple marker).
     */
    public static void broadcastCollisionEvent(CollisionEvent ev) {
        for(int i = 0; i < collisionListeners.size(); i++) {
            collisionListeners.get(i).collisionOccurred(ev);
        }
    }

    /**
     * @return A collection with registered collision listeners.
     */
    public static ArrayList<CollisionListener> getCollisionListeners() {
        return collisionListeners;
    }

    /**
     * Clear the collection with listeners (when the game should be shut down/restarted/closed).
     */
    public static void clear() {
        collisionListeners.clear();
    }
}
