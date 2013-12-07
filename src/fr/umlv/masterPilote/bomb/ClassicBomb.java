package fr.umlv.masterPilote.bomb;

import fr.umlv.masterPilote.MasterPilote;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

public class ClassicBomb {

    private final int radius = 2;
    private World world;
    private int x_axis;
    private int y_axis;
    private Body body;

    public ClassicBomb(World world, int x_axis, int y_axis) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;
    }

    public void create() {

        // create ball
        CircleShape cs = new CircleShape();

        cs.m_radius = radius;

        // Create an JBox2D body defination for ball.
        BodyDef bd = new BodyDef();

        bd.position.set(x_axis, y_axis);
        bd.type = BodyType.DYNAMIC;

        // Create a fixture for ball
        FixtureDef fd = new FixtureDef();
        // applique toi a cs
        fd.shape = cs;
        fd.density = 0.0f;
        fd.friction = 0.1f;
        fd.restitution = 0.5f;

        fd.filter.categoryBits = MasterPilote.ENEMY;

        fd.filter.maskBits = MasterPilote.HERO | MasterPilote.PLANET;

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
