package fr.umlv.masterPilot.ship.enemy;

import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emmanuel on 22/12/13.
 */
public class SquadronBehaviour extends EnemyBehaviour {
    private final List<Body> bodyJointList;
    private boolean destroyedTriangle =false;

    public SquadronBehaviour(SpaceShip enemySpaceShip, Color color, List<Body> bodyJointList) {
        super(enemySpaceShip, color);
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
