package fr.umlv.masterPilot.ship;

public interface Move {
	public void right();

	public void left();

	public void up();

	public void down();

    public  default void doMove(){
        throw new UnsupportedOperationException();
    }
}
