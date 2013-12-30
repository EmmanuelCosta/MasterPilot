package fr.umlv.masterPilot.ship;

/**
 * interface of basic move direction of character in MasterPilotWorld
 */
public interface Move {
	public void right();

	public void left();

	public void up();

	public void down();
    /**
     * for complex movement
     */
    public  default void doMove(){
        throw new UnsupportedOperationException();
    }
}
