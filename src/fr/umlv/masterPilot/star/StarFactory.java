package fr.umlv.masterPilot.star;

import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.ship.enemy.*;
import fr.umlv.masterPilot.ship.hero.Hero;
import fr.umlv.masterPilot.world.MasterPilotWorld;

/**
 * Use this to create star in jbox world
 * it will register it into the StarManager of the MasterPiloteWorld
 * Created by emmanuel on 28/12/13.
 */
public class StarFactory {


    private final MasterPilotWorld masterPilotWorld;

    public StarFactory(MasterPilotWorld masterPilotWorld) {

        this.masterPilotWorld = masterPilotWorld;
    }

    public Star createStar(float x_axis, float y_axis,int density,int radius){
        Star star = new Star(this.masterPilotWorld.getWorld(),x_axis,y_axis,density,radius);
        star.create();
        this.masterPilotWorld.addToStarManager(star.getBody(), star);
        return star;
    }

}
