package fr.umlv.masterPilot.ship;

/**
 * interface of basic move direction of character in MasterPilotWorld
 */
public interface Move {
    /**
     * must implement the right direction move of a spaceship
     */
	public void right();
    /**
     * must implement the left direction move of a spaceship
     */
	public void left();
    /**
     * must implement the up direction move of a spaceship
     */
	public void up();
    /**
     * must implement the down direction move of a spaceship
     */
	public void down();
	
    /**
     * for complex movement
     */
    public  default void doMove(){
        throw new UnsupportedOperationException();
    }
}
