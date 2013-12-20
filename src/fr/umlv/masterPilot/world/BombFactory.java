package fr.umlv.masterPilot.world;

import fr.umlv.masterPilot.Interface.Bomb;
import fr.umlv.masterPilot.bomb.GenericBomb;

/**
 * Created by emmanuel on 20/12/13.
 */
public class BombFactory {

    private final MasterPilot masterPilot;

    public BombFactory(MasterPilot masterPilot) {

        this.masterPilot = masterPilot;
    }

    public Bomb createBomb(int x, int y, Bomb.BombType type) {
        GenericBomb gBomb = new GenericBomb(this.masterPilot.getWorld(), x, y, type);

        this.masterPilot.addToBombManager(gBomb.getBody(), gBomb);

        return gBomb;
    }
}
