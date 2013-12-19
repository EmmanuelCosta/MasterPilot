package fr.umlv.masterPilot.Interface;

import org.jbox2d.dynamics.Body;

public interface SpaceShip extends Move {

    public void fire();

    public void fireBomb(Bomb.BombType bomb);

    public void shield();

    public void create();

    public Body getBody();

}
