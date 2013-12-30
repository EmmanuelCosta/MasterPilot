package fr.umlv.masterPilot.ship;

import org.jbox2d.dynamics.Body;

/**
 * All MasterPilot Ship Hero like ennemy
 * derive from that interface
 */

public interface SpaceShip extends Move {

    /**
     * normal fire
     */
    public void fire();

    /**
     * this is for launching a bomb
     */
    public void fireBomb();

    /**
     * implement this for shield
     */
    public void shield();

    /**
     * must create a body in jbox2D world
     */
    public void create();

    /**
     * get the jbox 2d Body Object of the spaceShip
     * @return jbox 2d Body Object of the spaceShip
     */
    public Body getBody();

     /**
     * defines here some proccess to do when your spaceShip is destroy
     * like stopping every thread launched by him
     * @return
     */
    public default boolean destroySpaceShip(){
        return false;
    }
}
