package fr.umlv.masterPilote.hero;

import fr.umlv.masterPilote.world.MasterPilote;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.*;

import java.awt.*;

/**
 * Created by emmanuel on 11/12/13.
 */
public class Shield {
    private final int maskBit;
    private final int category;
    private final Color color;
    private int radius = 10;
    private World world;
    private float x_axis;
    private float y_axis;
    private Body body;

    public Shield(World world, float x_axis, float y_axis, int radius) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;

        this.radius = radius;


        this.category = MasterPilote.ENEMY;
        this.maskBit = MasterPilote.HERO | MasterPilote.PLANET;

        this.color = Color.WHITE;


    }

    public Shield(World world, float x_axis, float y_axis, int category, int maskBit, Color color) {
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
        bd.type = BodyType.DYNAMIC;
        bd.userData = this.getClass();

        // Create a fixture for ball
        FixtureDef fd = new FixtureDef();
        // applique toi a cs
        fd.shape = cs;
        fd.density = 0.0f;
        fd.friction = 0.1f;
        fd.restitution = 0.5f;

        fd.filter.categoryBits = this.category;

        fd.filter.maskBits = this.maskBit;

        fd.userData = color;

        // body
        Body body = this.world.createBody(bd);
        body.createFixture(fd);
        this.body = body;


    }

    public Body getBody() {
        return this.body;
    }


}
