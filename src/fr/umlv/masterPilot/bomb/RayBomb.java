package fr.umlv.masterPilot.bomb;

import fr.umlv.masterPilot.world.MasterPilot;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import java.awt.*;

/**
 * Ray bomb are normal bomb
 * by default is hero friendly but
 * enemy hostile
 *
 * created By Babala Costa Emmanuel
 */
public class RayBomb {

    private final int maskBit;
    private final int category;
    private final Color color;
    private  float angle =0;
    private float y1=0;
    private float x1=0;

    private World world;
    private float x_axis;
    private float y_axis;
    private Body body;

    public RayBomb(World world, float x_axis, float y_axis) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;

        this.category = MasterPilot.HERO;
        this.maskBit = MasterPilot.ENEMY| MasterPilot.PLANET;

        this.color = Color.WHITE;

    }

    public RayBomb(World world, float x_axis, float y_axis,float angle) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;

        this.category = MasterPilot.ENEMY;
        this.maskBit = MasterPilot.HERO | MasterPilot.PLANET;

        this.color = Color.WHITE;

        this.angle = angle;

    }

    public RayBomb(World world, float x_axis, float y_axis,int category, int maskBit,Color color) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;

        this.category = category;
        this.maskBit=maskBit;

        this.color = color;
    }





    public RayBomb(World world, float x_axis, float y_axis, float x1, float y1) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;

        this.x1 = x1;
        this.y1=y1;
        ;

        this.category = MasterPilot.ENEMY;
        this.maskBit = MasterPilot.HERO | MasterPilot.PLANET;

        this.color = Color.WHITE;


    }

    public void create() {

        Vec2 vertices[] = new Vec2[2];
        vertices[0] = new Vec2(0, 0);

        vertices[1] = new Vec2(0, 5f);

//        vertices[2] = new Vec2(1f, 0.0f);
//
//        vertices[3] = new Vec2(1f, 10.0f);


        EdgeShape ps = new EdgeShape();
        ps.set(vertices[0],vertices[1]);



        // Create an JBox2D body defination for ball.
        BodyDef bd = new BodyDef();

        bd.position.set(x_axis, y_axis);
        bd.type = BodyType.DYNAMIC;
        bd.userData=this.getClass();

        // Create a fixture for ball
        FixtureDef fd = new FixtureDef();
        // applique toi a cs
        fd.shape = ps;
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
        Vec2 f = new Vec2(5000f,5000f);
        Vec2 p = body.getWorldPoint(body.getLocalCenter().add(
                new Vec2(0.0f, 200.0f)));
        body.applyLinearImpulse(f, p);



    }

    public Body getBody() {
        return this.body;
    }

}
