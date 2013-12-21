package fr.umlv.masterPilot.world;

import fr.umlv.masterPilot.bomb.Bomb;
import fr.umlv.masterPilot.bomb.GenericBomb;

/**
 * Created by emmanuel on 20/12/13.
 */
public class BombFactory {

    private final MasterPilotWorld masterPilotWorld;

    public BombFactory(MasterPilotWorld masterPilotWorld) {

        this.masterPilotWorld = masterPilotWorld;
    }

    public Bomb createBomb(int x, int y, Bomb.BombType type) {
        GenericBomb gBomb = new GenericBomb(this.masterPilotWorld.getWorld(), x, y, type);

        this.masterPilotWorld.addToBombManager(gBomb.getBody(), gBomb);

        return gBomb;
    }
}
