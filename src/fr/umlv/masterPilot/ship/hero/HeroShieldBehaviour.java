package fr.umlv.masterPilot.ship.hero;

import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.dynamics.Fixture;

/**
 * Created by emmanuel on 21/12/13.
 */
public class HeroShieldBehaviour implements UserSpec {
    private boolean collide = true;

    @Override
    public void onCollide(Fixture fix2, boolean flag) {

        if(fix2.getFilterData().categoryBits == MasterPilotWorld.TRAIL)
            return;
/**
 * i put the shield in the begining of collision
 * and i retreive it a the end
 */
        if (flag == true) {

            if (fix2.getFilterData().categoryBits != (MasterPilotWorld.BOMB | MasterPilotWorld.MEGABOMB) ) {


                collide = false;
            }


        } else if (flag == false) {

            collide = true;

        }

    }

    /**
     * according to jbox sensor if is set to false
     * so collision must append
     * @return
     */
    @Override
    public boolean getSensor() {
        return collide;
    }

}
