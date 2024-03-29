package fr.umlv.masterPilot.ship.enemy;

import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * this imlplements the basic behaviour of squadron when collision is detect
 * @author Babala Costa Emmanuel
 */
public class SquadronBehaviour extends EnemyBehaviour {
    private final List<Body> bodyJointList;
    private boolean destroyedTriangle =false;

    public SquadronBehaviour(Color color, List<Body> bodyJointList) {
        super( color);
        this.bodyJointList=bodyJointList;
    }

    @Override
    public void onCollide(Fixture fix2, boolean flag) {
        super.onCollide(fix2, flag);
        if(fix2.getFilterData().categoryBits != MasterPilotWorld.PLANET){
            destroyedTriangle=true;
        }
    }

    @Override
    public java.util.List<Body> getJointBody() {
        if(destroyedTriangle ){
            destroyedTriangle=false;
            return this.bodyJointList;
        }
        else{
            return new ArrayList<Body>();
        }
    }

    @Override
    public boolean getSensor() {
        return false;
    }

    @Override
    public boolean hasJointBody() {
        return true;
    }
}
