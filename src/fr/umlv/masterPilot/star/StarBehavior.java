package fr.umlv.masterPilot.star;

import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.dynamics.Fixture;

import java.awt.*;

/**
 * @see fr.umlv.masterPilot.common.UserSpec
 * specify the collision behaviour of the star
 * Created by emmanuel on 29/12/13.
 */
public class StarBehavior implements UserSpec {

    private final Color color;

    public StarBehavior(Color color) {
        this.color = color;
    }

    @Override
    public void onCollide(Fixture fix2, boolean flag) {
        
        /**
         * i have to put hero shield when  it
         * will collide with a star
         */
        if(fix2.getFilterData().categoryBits == MasterPilotWorld.SHIELD){
            fix2.m_isSensor = false;
        }
    }

    @Override
    public Color getColor() {
        return color;
    }
}
