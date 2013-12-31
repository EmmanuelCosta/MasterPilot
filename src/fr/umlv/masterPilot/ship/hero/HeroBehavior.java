package fr.umlv.masterPilot.ship.hero;

import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.dynamics.Fixture;

/**
 * @see fr.umlv.masterPilot.common.UserSpec
 * basic behaviour of hero when collision is detected
 * Created by emmanuel on 28/12/13.
 */
public class HeroBehavior implements UserSpec {

    private final MasterPilotWorld.MODE mode ;
    private int heroLive = 10;


    public HeroBehavior(MasterPilotWorld.MODE mode) {
        this.mode = mode;
    }

    @Override
    public void onCollide(Fixture fix2, boolean flag) {
        if(flag == true && this.mode == MasterPilotWorld.MODE.HARDCORE){
            heroLive--;
        }
    }

    public int getHeroLive() {
        return heroLive;
    }
}
