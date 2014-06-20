package com.game.Util;

import com.game.Model.Creature;
import com.game.Model.Platform;
import com.game.Model.Player;
import com.game.View.CollisionListener;
import com.game.View.CustomStorage;

/**
 * The helper class which works with founding collisions between objects.
 * Also doing some count work on the player's score points.
 * All methods are static, so there is no need to create an instance of
 * this class, since each sub-Creature instance which wants to be painted
 * again after its movement should check either further movement is possible
 * or not.
 *
 * @see com.game.Model.Creature
 *
 * Created by E. Mozharovsky on 19.06.14.
 */
public abstract class CollisionCenter {
    /**
     * The player's score points which come each time a collision wasn't found.
     */
    private static int score = 0;

    /**
     * @return The points scored.
     */
    public static int getScore() {
        return score;
    }

    /**
     * Remove all previous score points.
     */
    public static void cleanScore() {
        score = 0;
    }

    /**
     * Iterate each CollisionListener element in CustomStorage collection to find
     * a player's object and then do the same with platform's ones (inside of the
     * player's founding branch). Check if player is moving at the moment if yes,
     * then finds any object on the player's trajectory (considering the possible
     * area of collision), and if an object is inside of this area do a check on
     * a collision so if it occurs increment collisionCount integer variable to
     * say that at least one collision was detected (because there is a possibility
     * that more than one collision can be occurred, so we should be sure that we
     * know about this) and assign a status as false, saying that a movement is
     * not possible for the caller's object. If the player is not moving, we check
     * objects which can collide with the player on the possible collision area, so
     * if it occurs we do the same as with player - increment collisionCount integer
     * variable and then change status to false.
     *
     * @see com.game.View.CustomStorage
     * @see com.game.View.CollisionListener
     *
     * @return The status of check:
     *                              true - Collision was not found.
     *                              false - Collision was found therefore
     *                                      any movement impossible.
     */
    public static boolean  isMovementPossible() {
        boolean status = true;

        for(CollisionListener _player : CustomStorage.getCollisionListeners()) {
            if (_player instanceof Player) {
                final Player player = (Player) _player;

                for(CollisionListener _platform : CustomStorage.getCollisionListeners()) {
                    if (_platform instanceof Platform) {
                        final Platform platform = (Platform) _platform;

                        int collisionCount = 0;

                        if (!player.getVector().equals(Creature.Vector.NONE)) {
                            switch (player.getVector()) {
                                case UP:
                                    if (player.getY() <= platform.getY() + platform.getHeight() &
                                            findLocationRelatedTo(player, platform).equals(Location.UPPER) ||
                                            findLocationRelatedTo(player, platform).equals(Location.MULTI)) {
                                        collisionCount++;
                                    }
                                case DOWN:
                                    if (player.getY() + player.getHeight() >= platform.getY() &&
                                            findLocationRelatedTo(player, platform).equals(Location.DOWNER) ||
                                            findLocationRelatedTo(player, platform).equals(Location.MULTI)) {
                                        collisionCount++;
                                    }
                                case RIGHT:
                                    if (player.getX() + player.getWidth() >= platform.getX() &&
                                            findLocationRelatedTo(player, platform).equals(Location.RIGHTER) ||
                                            findLocationRelatedTo(player, platform).equals(Location.MULTI)) {
                                        collisionCount++;
                                    }
                                case LEFT:
                                    if (player.getX() <= platform.getX() + platform.getWidth() &&
                                            findLocationRelatedTo(player, platform).equals(Location.LEFTER) ||
                                            findLocationRelatedTo(player, platform).equals(Location.MULTI)) {
                                        collisionCount++;
                                    }
                            }
                        } else {
                            switch (platform.getVector()) {
                                case UP:
                                    if (platform.getY() <= player.getY() + player.getHeight() &&
                                            findLocationRelatedTo(platform, player).equals(Location.UPPER) ||
                                            findLocationRelatedTo(platform, player).equals(Location.MULTI)) {
                                        collisionCount++;
                                    }
                                    break;
                                case DOWN:
                                    if (platform.getY() + platform.getHeight() >= player.getY() &&
                                            findLocationRelatedTo(platform, player).equals(Location.DOWNER) ||
                                            findLocationRelatedTo(platform, player).equals(Location.MULTI)) {
                                        collisionCount++;
                                    }
                                    break;
                                case RIGHT:
                                    if (platform.getX() + platform.getWidth() >= player.getX() &&
                                            findLocationRelatedTo(platform, player).equals(Location.RIGHTER) ||
                                            findLocationRelatedTo(platform, player).equals(Location.MULTI)) {
                                        collisionCount++;
                                    }
                                    break;
                                case LEFT:
                                    if (platform.getX() <= player.getX() + player.getWidth() &&
                                            findLocationRelatedTo(platform, player).equals(Location.LEFTER) ||
                                            findLocationRelatedTo(platform, player).equals(Location.MULTI)) {
                                        collisionCount++;
                                    }
                                    break;
                            }
                        }

                        if (collisionCount >= 1) {
                            status = false;
                        } else {
                            score++;
                        }
                    }
                }
            }
        }

        return status;
    }

    /**
     * Check coordinates of requester and requested objects and check if the second is inside of the first collision area,
     * if so then return a particular location. But if the movement is being processed then we can get more than one location,
     * to solute this issue we return MILTI location type, which means that the second object is inside of 2 or more locations
     * having a possibility to collide with the first object. If that possibility is not found, we return NONE type.
     *
     * @see com.game.Util.CollisionCenter.Location
     *
     * @param one The requester which wants to know if the two object is inside of its possible collision area.
     * @param two The second object which can or cannot be inside of the first possible collision area.
     * @return A location of the second object relatively to the first if the second is inside of possible
     *         collision area.
     */
    private static Location findLocationRelatedTo(Creature one, Creature two) {
        Location location = Location.NONE;
        int locationCount = 0;

        if(one.getY() + one.getHeight() >= two.getY() &&
                one.getY() <= two.getY() + two.getHeight() &&
                one.getX() + one.getWidth() >= two.getX() + two.getWidth()) {
            location = Location.LEFTER;
            locationCount++;
        }

        if(one.getY() <= two.getY() + two.getHeight() &&
                one.getY() + one.getHeight() >= two.getY() &&
                one.getX() + one.getWidth() <= two.getX() + two.getWidth()) {
            location = Location.RIGHTER;
            locationCount++;
        }

        if(one.getX() + one.getWidth() >= two.getX() &&
                one.getX() <= two.getX() + two.getWidth() &&
                two.getY() <= one.getY()) {
            location = Location.UPPER;
            locationCount++;
        }

        if(one.getX() + one.getWidth() >= two.getX() &&
                one.getX() <= two.getX() + two.getWidth() &&
                two.getY() >= one.getY()) {
            location = Location.DOWNER;
            locationCount++;
        }

        if(locationCount >= 2) {
            location = Location.MULTI;
        }

        return location;
    }

    /**
     * Location types. The MULTI type is used when more than one location was a touched and
     * NONE if there is no location that was touched by some object relatively to other one.
     */
    private enum Location {
        UPPER,
        DOWNER,
        LEFTER,
        RIGHTER,
        MULTI,
        NONE;
    }
}
