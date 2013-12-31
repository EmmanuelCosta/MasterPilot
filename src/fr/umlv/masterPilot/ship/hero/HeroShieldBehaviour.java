package fr.umlv.masterPilot.ship.hero;

import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.dynamics.Fixture;

/**
 * @see fr.umlv.masterPilot.common.UserSpec
 * Created by emmanuel on 21/12/13.
 */
public class HeroShieldBehaviour implements UserSpec {
    private final MasterPilotWorld.MODE mode;
    private boolean collide = true;

    /**
     * if mode is MODE CHEAT so automatic shield mode is activate
     * if is HARDCORE YOU MUST ACTIVATE IT MANUALY
     * @param mode {@link fr.umlv.masterPilot.world.MasterPilotWorld.MODE} MODE
     */
    public HeroShieldBehaviour(MasterPilotWorld.MODE mode) {
        this.mode = mode;
    }

    @Override
    public void onCollide(Fixture fix2, boolean flag) {
        
        if (this.mode == MasterPilotWorld.MODE.CHEAT) {
            if (fix2.getFilterData().categoryBits == MasterPilotWorld.TRAIL)
                return;
            
            /**
             * i put the shield in the begining of collision
             * and i retreive it a the end
             */
            if (flag == true) {
                if (fix2.getFilterData().categoryBits != MasterPilotWorld.BOMB ||
                        fix2.getFilterData().categoryBits !=  MasterPilotWorld.MEGABOMB) {
                    collide = false;
                }
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
