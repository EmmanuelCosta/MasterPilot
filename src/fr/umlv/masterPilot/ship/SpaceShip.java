package fr.umlv.masterPilot.ship;

import org.jbox2d.dynamics.Body;

/**
 * All MasterPilot Ship Hero like ennemy
 * derive from that interface
 */

public interface SpaceShip extends Move {

    public void fire();

    public void fireBomb();

    public void shield();

    public void create();

    public Body getBody();
    
    public void doMove();

    public default boolean destroySpaceShip(){
        return false;
    }
}
