package fr.umlv.masterPilot.ship.enemy;

import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.dynamics.Fixture;

import java.awt.*;

/**
 * Created by emmanuel on 22/12/13.
 */
public class TriangleBehaviour implements UserSpec {

    private final SpaceShip enemySpaceShip;
    private final Color color;
    private boolean destroy = false;

    public TriangleBehaviour(SpaceShip enemySpaceShip, Color color) {
        this.enemySpaceShip = enemySpaceShip;
        this.color = color;
    }

    @Override
    public void onCollide(Fixture fix2, boolean flag) {

        if (flag == false) {
            if (fix2.getFilterData().categoryBits == (MasterPilotWorld.SHOOT)
                    || fix2.getFilterData().categoryBits == (MasterPilotWorld.SHIELD)) {

                this.destroy = true;

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
