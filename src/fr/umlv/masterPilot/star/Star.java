package fr.umlv.masterPilot.star;

import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.world.MasterPilot;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.*;

import java.awt.*;

/**
 * created a start in the world
 * Created by emmanuel
 */
public class Star {
    private final int radius = 100;
    private final int maskBit;
    private final int category;
    private final World world;
    private final float x_axis;
    private final float y_axis;
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
        this.category = MasterPilot.PLANET;
        this.maskBit = MasterPilot.ENEMY | MasterPilot.HERO
                | MasterPilot.SHOOT | MasterPilot.SHIELD
                | MasterPilot.MEGABOMB |MasterPilot.BOMB;
    }

    public Star(World world, float x_axis, float y_axis,Color color) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.color = color;
        this.category = MasterPilot.PLANET;
        this.maskBit = MasterPilot.ENEMY | MasterPilot.HERO
                | MasterPilot.SHOOT | MasterPilot.SHIELD
                | MasterPilot.MEGABOMB |MasterPilot.BOMB;
    }

    public Star(World world, float x_axis, float y_axis, int category, int maskBit, Color color) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;

        this.category = category;
        this.maskBit = maskBit;

        this.color = color;
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
        fd.density = 1500f;
        fd.friction = 50f;
        fd.restitution = 100f;


        /**
         * we set the color of
         * the body
         */
        fd.userData = new UserSpec() {
            @Override
            public void onCollide(Fixture fix2, boolean flag) {
                /**
                 * i have to put hero shield when  it
                 * will collide with a star
                 */
                if(fix2.getFilterData().categoryBits == MasterPilot.SHIELD){

                    fix2.m_isSensor = false;
                }
            }

            @Override
            public Color getColor() {
                return color;
            }
        };

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
