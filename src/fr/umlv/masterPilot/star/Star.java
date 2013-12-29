package fr.umlv.masterPilot.star;

import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.*;

import java.awt.*;

/**
 * created a start in the world
 * Created by emmanuel
 */
public class Star {
    private final int radius;
    private final int maskBit;
    private final int category;
    private final World world;
    private final float x_axis;
    private final float y_axis;
    private final int density ;
    private Color color;
    private Body body;

    /**
     * created a star at the secify coordinate
     *
     * @param world
     * @param x_axis
     * @param y_axis
     */
    public Star(World world, float x_axis, float y_axis) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.color = Color.RED;
        this.category = MasterPilotWorld.PLANET;
        this.maskBit = MasterPilotWorld.ENEMY | MasterPilotWorld.HERO
                | MasterPilotWorld.SHOOT | MasterPilotWorld.SHIELD
                | MasterPilotWorld.MEGABOMB | MasterPilotWorld.BOMB;
        this.density = 150;
        this.radius=50;
    }

    public Star(World world, float x_axis, float y_axis,Color color) {
        this(world,x_axis,y_axis);
        this.color=color;
    }

    public Star(World world, float x_axis, float y_axis,int density,int radius,Color color) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.color = color;
        this.category = MasterPilotWorld.PLANET;
        this.maskBit = MasterPilotWorld.ENEMY | MasterPilotWorld.HERO
                | MasterPilotWorld.SHOOT | MasterPilotWorld.SHIELD
                | MasterPilotWorld.MEGABOMB | MasterPilotWorld.BOMB;
        this.density = density;
        this.radius=radius;
    }

    /**
     *
     */
    public void create() {

        CircleShape cs = new CircleShape();

        cs.m_radius = radius;

        BodyDef bd = new BodyDef();

        bd.position.set(x_axis, y_axis);
        bd.type = BodyType.STATIC;
        bd.allowSleep = true;
        bd.userData = this.getClass();


        // Create a fi xture for ball
        FixtureDef fd = new FixtureDef();
        // applique toi a cs
        fd.shape = cs;
        fd.density = this.density;
        fd.friction = 50f;
        fd.restitution = 0.5f;


        /**
         * we set the color of
         * the body
         */
        fd.userData = new StarBehaviour(this.color);

        fd.filter.categoryBits = this.category;

        fd.filter.maskBits = this.maskBit;


        // body
        Body body = this.world.createBody(bd);
        body.createFixture(fd);

        this.body = body;

    }

    public Body getBody() {
        return body;
    }

}
