package fr.umlv.masterPilote.fr.umlv.masterPilote.star;

import fr.umlv.masterPilote.MasterPilote;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/**
 * Created by emmanuel on 08/12/13.
 */
public class Star {
    private final int radius = 100;
    private final int maskBit;
    private final int category;
    private World world;
    private float x_axis;
    private float y_axis;
    private Body body;

    public Star(World world, float x_axis, float y_axis) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;

        this.category = MasterPilote.PLANET;
        this.maskBit = MasterPilote.ENEMY | MasterPilote.HERO;
    }

    public Star(World world, float x_axis, float y_axis, int category, int maskBit) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;

        this.category = category;
        this.maskBit = maskBit;
    }

    public void create() {

        // create ball
        CircleShape cs = new CircleShape();

        cs.m_radius = radius;

        // Create an JBox2D body defination for ball.
        BodyDef bd = new BodyDef();

        bd.position.set(x_axis, y_axis);
        bd.type = BodyType.STATIC;

        // Create a fixture for ball
        FixtureDef fd = new FixtureDef();
        // applique toi a cs
        fd.shape = cs;
        fd.density = 1500f;
        fd.friction = 200f;
        fd.restitution = 100f;

        fd.filter.categoryBits = this.category;

        fd.filter.maskBits = this.maskBit;

        fd.userData = this;

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
        return body;
    }

}
