package fr.umlv.masterPilote.star;

import fr.umlv.masterPilote.world.MasterPilote;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.*;

import java.awt.*;

/**
 * Created by emmanuel on 08/12/13.
 */
public class Star {
    private final int radius = 100;
    private final int maskBit;
    private final int category;
    private final Color color;
    private final World world;
    private final float x_axis;
    private final float y_axis;
    private Body body;

    public Star(World world, float x_axis, float y_axis) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.color = Color.RED;
        this.category = MasterPilote.PLANET;
        this.maskBit = MasterPilote.ENEMY | MasterPilote.HERO;
    }

    public Star(World world, float x_axis, float y_axis, int category, int maskBit, Color color) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;

        this.category = category;
        this.maskBit = maskBit;

        this.color = color;
    }

    public void create() {

        // create ball
        CircleShape cs = new CircleShape();

        cs.m_radius = radius;

        // Create an JBox2D body defination for ball.
        BodyDef bd = new BodyDef();

        bd.position.set(x_axis, y_axis);
        bd.type = BodyType.STATIC;
        bd.allowSleep=true;
        bd.userData = this.getClass();


        // Create a fi xture for ball
        FixtureDef fd = new FixtureDef();
        // applique toi a cs
        fd.shape = cs;
        fd.density = 1500f;
        fd.friction = 200f;
        fd.restitution = 100f;



        /**
         * we set the color of
         * the body
         */
        fd.userData = this.color;

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
