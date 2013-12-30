package fr.umlv.masterPilot.ship.enemy;

import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

import java.awt.*;

/**
 * define how ennemy shield body have to react when collision is detect
 * see also @see UserSpec
 * Created by emmanuel on 21/12/13.
 */
public class EnemyBehaviour implements UserSpec {

    private boolean destroy = false;
    private final SpaceShip enemySpaceShip ;
    private final Color color;
    public EnemyBehaviour(SpaceShip enemySpaceShip,Color color) {
        this.enemySpaceShip = enemySpaceShip;
        this.color=color;
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
