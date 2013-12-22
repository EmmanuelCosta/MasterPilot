package fr.umlv.masterPilot.ship.enemy;

import fr.umlv.masterPilot.ship.SpaceShip;
import org.jbox2d.dynamics.Body;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by emmanuel on 22/12/13.
 */
public class SquadronBehaviour extends EnemyBehaviour {
    private final List<Body> bodyJointList;

    public SquadronBehaviour(SpaceShip enemySpaceShip, Color color, List<Body> bodyJointList) {
        super(enemySpaceShip, color);
        this.bodyJointList=bodyJointList;
    }



    @Override
    public java.util.List<Body> getJointBody() {
        return this.bodyJointList;
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
