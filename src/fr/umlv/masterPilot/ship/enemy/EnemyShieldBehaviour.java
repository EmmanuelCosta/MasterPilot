package fr.umlv.masterPilot.ship.enemy;

import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.dynamics.Fixture;

/**
 * this is class which defines the action of shield ennemy
 * when collision is detect
 * give it to the spaceship ennemy that have been design to use shield
 * Created by emmanuel on 22/12/13.
 */
public class EnemyShieldBehaviour implements UserSpec {
    private int nbShield;
    private boolean collide = true;

    /**
     * create an instance of that class with the number of attempt shield
     * @param nbShield
     */
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


    @Override
    public boolean getSensor() {
        return collide;
    }
}
