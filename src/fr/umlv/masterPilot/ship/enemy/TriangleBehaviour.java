package fr.umlv.masterPilot.ship.enemy;

import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by emmanuel on 22/12/13.
 */
public class TriangleBehaviour implements UserSpec {

    private final SpaceShip enemySpaceShip;
    private final Color color;
    private final ArrayList<Body> bodyJointList;
    private boolean destroy = false;

    public TriangleBehaviour(SpaceShip enemySpaceShip, Color color, ArrayList<Body> bodyJointList) {
        this.enemySpaceShip = enemySpaceShip;
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
