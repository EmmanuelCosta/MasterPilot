package fr.umlv.masterPilot.Interface;

public interface SpaceShip extends Move {

    public void fire();

    public void fireBomb(Bomb.BombType bomb);

    public void shield();

}