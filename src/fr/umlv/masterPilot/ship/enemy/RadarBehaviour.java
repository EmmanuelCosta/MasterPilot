package fr.umlv.masterPilot.ship.enemy;

import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;


/**
 *
 * <p>This is for ennemy ship who need to detect an event in
 * their area of influence
 * </p>
 * some ennemy use this
 * to know if something hostile come in their trajectory
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
