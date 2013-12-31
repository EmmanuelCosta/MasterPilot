package fr.umlv.masterPilot.ship.enemy;

import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import java.awt.*;
import java.util.ArrayList;

/**
 * @see fr.umlv.masterPilot.common.UserSpec
 *
 * basic collision behaviour of the triangle space which protect the Squadon Mother Ship
 * @see fr.umlv.masterPilot.ship.enemy.Squadron
 * @author Babala Costa Emmanuel
 */
public class TriangleBehaviour implements UserSpec {
    private final Color color;
    private final ArrayList<Body> bodyJointList;
    private boolean destroy = false;

    public TriangleBehaviour(Color color, ArrayList<Body> bodyJointList) {
        this.color = color;
        this.bodyJointList = bodyJointList;
    }

    @Override
    public void onCollide(Fixture fix2, boolean flag) {

        if (flag == false) {
            if (fix2.getFilterData().categoryBits == (MasterPilotWorld.SHOOT)
                    || fix2.getFilterData().categoryBits == (MasterPilotWorld.SHIELD)) {
                this.destroy = true;
                this.bodyJointList.remove(fix2.getBody());
            }
        }
    }

    @Override
    public boolean isDestroyable() {
        return destroy;
    }

    @Override
    public Color getColor() {
        return this.color;
    }
}
