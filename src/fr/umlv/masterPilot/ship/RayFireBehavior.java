package fr.umlv.masterPilot.ship;

import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.dynamics.Fixture;

import java.awt.*;

/**
 * @see fr.umlv.masterPilot.common.UserSpec
 *
 * behaviour collision of RayFire
 * @author Babala Costa Emmanuel
 */
public class RayFireBehavior implements UserSpec {
    private boolean destroy=false;
    private final Color color;

    public RayFireBehavior(Color color) {
        this.color = color;
    }

    @Override
    public void onCollide(Fixture fix2, boolean flag) {
        if(flag == false && fix2.getFilterData().categoryBits != MasterPilotWorld.RADAR){
            this.destroy=true;
        }
    }

    @Override
    public boolean isDestroyable() {
        return destroy;
    }

    @Override
    public Color getColor() {
        return color;
    }
}
