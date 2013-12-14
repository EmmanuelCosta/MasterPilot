package fr.umlv.masterPilot.bomb;

import fr.umlv.masterPilot.world.MasterPilot;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.awt.*;

/**
 * classic bomb are normal bomb
 * by default is hero friendly but
 * enemy hostile
 * <p>
 * created By Babala Costa Emmanuel
 */
public class ClassicBomb {

    private  int radius = 2;
    private final int maskBit;
    private final int category;
    private final Color color;
    private World world;
    private float x_axis;
    private float y_axis;
    private Body body;

    public ClassicBomb(World world, float x_axis, float y_axis) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;


        this.category = MasterPilot.ENEMY;
        this.maskBit = MasterPilot.HERO | MasterPilot.PLANET;

        this.color = Color.WHITE;


    }

    public ClassicBomb(World world, float x_axis, float y_axis, int category, int maskBit, Color color) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;

        this.category = category;
        this.maskBit = maskBit;

        this.color = color;
    }

    public ClassicBomb(World world, float x_axis, float y_axis, int category, int maskBit, Color color,int radius) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;

        this.category = category;
        this.maskBit = maskBit;

        this.color = color;

        this.radius=radius;
    }

    public void create() {

        // create ball
        CircleShape cs = new CircleShape();

        cs.m_radius = radius;

        // Create an JBox2D body defination for ball.
        BodyDef bd = new BodyDef();

        bd.position.set(x_axis, y_axis);
        bd.type = BodyType.DYNAMIC;

        bd.allowSleep=true;
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

    public void applyForce() {
        Vec2 f = new Vec2(5000f, 5000f);
        Vec2 p = body.getWorldPoint(body.getLocalCenter().add(
                new Vec2(0.0f, 200.0f)));
        body.applyLinearImpulse(f, p);


    }

    public Body getBody() {
        return this.body;
    }

    public float getX() {
        return this.x_axis;
    }

    public float getY() {
        return this.y_axis;
    }
}
