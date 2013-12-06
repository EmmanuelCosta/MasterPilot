package fr.umlv.masterPilote.Interface;

public interface Vaisseau extends Move {

	public void fire();

	public void fireBomb(Bomb bomb);

	public void shield();

}
