package fr.umlv.masterPilot.ship;

import org.jbox2d.dynamics.Body;


public interface SpaceShip extends Move {

    public void fire();

    public void fireBomb();

    public void shield();

    public void create();

    public Body getBody();
}
