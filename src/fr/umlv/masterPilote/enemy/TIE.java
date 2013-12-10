package fr.umlv.masterPilote.enemy;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class TIE {
 
    private World world;
    private int x_axis; // point x où apparait le TIE
    private int y_axis; // point y où apparait le TIE
    private Body body;

    public TIE(World world, int x_axis, int y_axis) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;
    }
    
    public void create() {

        // create ball
        PolygonShape ps = new PolygonShape();

        Vec2[] vertices = new Vec2[6];
        vertices[0] = new Vec2(0f, 5f);
        vertices[1] = new Vec2(5f, 10f);
        vertices[2] = new Vec2(25f, 10f);
        vertices[3] = new Vec2(30f, 5f);
        vertices[4] = new Vec2(25f, 0f);
        vertices[5] = new Vec2(5f, 0f);
        ps.set(vertices, 6);
        
        // Create an JBox2D body defination for TIE.
        BodyDef bd = new BodyDef();
        bd.position.set(x_axis, y_axis);
        bd.type = BodyType.DYNAMIC;

        // Create a fixture for TIE
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 0.5f;
        fd.friction = 0.5f;
        fd.restitution = 0.5f;
        fd.filter.categoryBits = 0x001;
        fd.filter.maskBits = 0x001;
        fd.userData = this;

        // body
        Body body = this.world.createBody(bd);
        body.createFixture(fd);
        this.body = body;
    }
}
