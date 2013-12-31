package fr.umlv.masterPilot.ship.enemy;

import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.dynamics.Fixture;

/**
 * Created by emmanuel on 22/12/13.
 */
public class EnemyShieldBehaviour implements UserSpec {
    private int nbShield;
    private boolean collide = true;

    public EnemyShieldBehaviour(int nbShield) {
        this.nbShield = nbShield;
    }

    @Override
    public void onCollide(Fixture fix2, boolean flag) {

        /**
         * i put the shield in the begining of collision
         * and i retreive it a the end
         */
        if (fix2.getFilterData().categoryBits != MasterPilotWorld.ENEMY
                || fix2.getFilterData().categoryBits != MasterPilotWorld.RADAR) {
            if (flag == true && nbShield > 0) {
                collide = false;
                nbShield--;
            } else if (flag == false) {
                collide = true;
            }
        }
    }

    /**
     * according to jbox sensor if is set to false
     * so collision must append
     *
     * @return
     */
    @Override
    public boolean getSensor() {
        return collide;
    }
}
