package fr.umlv.masterPilot.world;

import fr.umlv.masterPilot.bomb.Bomb;
import fr.umlv.masterPilot.bomb.GenericBomb;

/**
 * call this for creating a bomb in the jbox2d world and register it in masterPilotWorld
 * Created by emmanuel on 20/12/13.
 */
public class BombFactory {

    private final MasterPilotWorld masterPilotWorld;

    public BombFactory(MasterPilotWorld masterPilotWorld) {

        this.masterPilotWorld = masterPilotWorld;
    }

    /**
     * create a bomb in jbox 2d world and register it in masterPilotWorld
     * @param x
     * @param y
     * @param type
     * @return
     */
    public Bomb createBomb(int x, int y, Bomb.BombType type) {
        GenericBomb gBomb = new GenericBomb(this.masterPilotWorld.getWorld(), x, y, type);

        this.masterPilotWorld.addToBombManager( gBomb);

        return gBomb;
    }
}
