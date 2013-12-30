package fr.umlv.masterPilot.ship.enemy;

import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.ship.hero.Hero;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;

/**
 * some ennemies use this
 * to know if something hostile come in their direction
 * so they can performe some action like
 * corrected their direction
 *
 * Created by emmanuel on 26/12/13.
 */
public class RadarBehaviour implements UserSpec {

    private Vec2 collidePosition = new Vec2();


    @Override
    public void onCollide(Fixture fix2, boolean flag) {

        if (flag == true) {
            if (fix2.getFilterData().categoryBits == MasterPilotWorld.HERO ||
                    fix2.getFilterData().categoryBits == MasterPilotWorld.SHOOT
                    || fix2.getFilterData().categoryBits == MasterPilotWorld.BOMB
                    || fix2.getFilterData().categoryBits == MasterPilotWorld.MEGABOMB) {
                collidePosition.set(fix2.getBody().getPosition());



            }
        }
    }

    @Override
    public boolean getSensor() {
        return true;
    }

    public Vec2 getPositionOfCollision() {
        try {
            return new Vec2(this.collidePosition);
        } finally {
            this.collidePosition = new Vec2();
        }

    }


}
