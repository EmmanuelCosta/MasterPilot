package fr.umlv.masterPilot.ship.hero;

import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.dynamics.Fixture;

import java.awt.*;

/**
 * @see fr.umlv.masterPilot.common.UserSpec
 * Created by emmanuel on 28/12/13.
 */
public class TrailBehaviour implements UserSpec {

    private Color color;


    public TrailBehaviour(Color color) {
        this.color = color;
    }


    @Override
    public void onCollide(Fixture fix2, boolean flag) {


    }



    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public boolean getSensor() {
        return true;
    }
}
