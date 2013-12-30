package fr.umlv.masterPilot.star;

import fr.umlv.masterPilot.world.MasterPilotWorld;
import java.awt.*;

/**
 * Use this to create star in jbox2d world
 * it will register it into the StarManager of the MasterPiloteWorld
 * Created by emmanuel on 28/12/13.
 */
public class StarFactory {
    private final MasterPilotWorld masterPilotWorld;

    public StarFactory(MasterPilotWorld masterPilotWorld) {
        this.masterPilotWorld = masterPilotWorld;
    }

    public Star createStar(float x_axis, float y_axis,int density,int radius,Color color){
        Star star = new Star(this.masterPilotWorld.getWorld(),x_axis,y_axis,density,radius,color);
        star.create();
        this.masterPilotWorld.addToStarManager(star.getBody(), star);
        return star;
    }

    public Star createStar(float x_axis, float y_axis,Color color){
        Star star = new Star(this.masterPilotWorld.getWorld(),x_axis,y_axis,color);
        star.create();
        this.masterPilotWorld.addToStarManager(star.getBody(), star);
        return star;
    }

}
