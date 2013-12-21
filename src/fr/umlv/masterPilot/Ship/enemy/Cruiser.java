package fr.umlv.masterPilot.Ship.enemy;

import fr.umlv.masterPilot.Ship.RayFire;
import fr.umlv.masterPilot.Ship.SpaceShip;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;

public class Cruiser implements SpaceShip {

    private final int maskBit;
    private final int category;
    private World world;
    private int x_axis;
    private int y_axis;
    private Body body;
    private Vec2 cruiserSpeed = new Vec2(0, -30f);
    private Vec2 rayonForce = new Vec2(0f, -10f);
    private Vec2 shoot1;
    private Vec2 shoot2;
    private Vec2 shoot3;
    private Vec2 shoot4;
    private Vec2 shoot5;
    private Vec2 shoot6;
    private Vec2 shoot7;
    private Vec2 shoot8;

    public Cruiser(World world, int x_axis, int y_axis) {
        super();
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.shoot1 = new Vec2(-15, -15);
        this.shoot2 = new Vec2(+15, -15);
        this.shoot3 = new Vec2(-5, -15);
        this.shoot4 = new Vec2(+5, -15);

        /**
         * Interactions with the other bodies.
         */
        this.category = MasterPilotWorld.ENEMY;
        this.maskBit = MasterPilotWorld.PLANET
                | MasterPilotWorld.SHIELD
                | MasterPilotWorld.SHOOT
                | MasterPilotWorld.BOMB
                | MasterPilotWorld.MEGABOMB
                | MasterPilotWorld.HERO;
    }

    public void create() {

        /**
         * Primitive form
         */
        PolygonShape ps = new PolygonShape();

        /**
         * Number of vertices.
         */
        Vec2[] vertices = new Vec2[4];
        vertices[0] = new Vec2(x_axis - 15, y_axis);
        vertices[1] = new Vec2(x_axis - 15, y_axis - 10);
        vertices[2] = new Vec2(x_axis + 15, y_axis - 10);
        vertices[3] = new Vec2(x_axis + 15, y_axis);
        ps.set(vertices, 4);

        /**
         * Body definition of the Cruiser
         */
        BodyDef bd = new BodyDef();
        bd.position.set(x_axis, y_axis);
        bd.type = BodyType.DYNAMIC;
        bd.userData = this.getClass();

        /**
         * Body fixtures of the Cruiser
         */
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 0.5f;
        fd.friction = 0f;
        fd.restitution = 0.5f;
        fd.filter.categoryBits = this.category;
        fd.filter.maskBits = this.maskBit;
//        Color.cyan
        fd.userData = new EnemyBehaviour(this, Color.CYAN);

        /**
         * Integrate the body in the world.
         */
        Body body = this.world.createBody(bd);
        body.createFixture(fd);
        this.body = body;
    }

    @Override
    public Body getBody() {
        return this.body;
    }

    @Override
    public void right() {
        Vec2 force = body.getWorldVector(new Vec2(+100f, 0));
        this.body.setTransform(body.getPosition(), this.body.getAngle());
        this.body.applyForceToCenter(force);
    }

    @Override
    public void left() {
        Vec2 force = body.getWorldVector(new Vec2(-100f, 0));
        this.body.setTransform(body.getPosition(), this.body.getAngle());
        this.body.applyForceToCenter(force);

    }

    @Override
    public void up() {
        Vec2 force = body.getWorldVector(new Vec2(0, +5f));
        this.body.setTransform(body.getPosition(), this.body.getAngle());
        this.body.applyForceToCenter(force);
    }

    @Override
    public void down() {
        Vec2 force = body.getWorldVector(new Vec2(0, -5f));
        this.body.setTransform(body.getPosition(), this.body.getAngle());
        this.body.applyForceToCenter(force);
    }

    @Override
    public void fire() {
        /**
         * I get the actual tip coordinate in the world
         */

        // under part
        Vec2 worldPoint1 = body.getWorldPoint(shoot1);
        Vec2 worldPoint2 = body.getWorldPoint(shoot2);
        Vec2 worldPoint3 = body.getWorldPoint(shoot3);
        Vec2 worldPoint4 = body.getWorldPoint(shoot4);

        /**
         * create the shoot
         */

        // Under part
        RayFire rayon1 = new RayFire(this.world, worldPoint1.x, worldPoint1.y);
        rayon1.create();
        RayFire rayon2 = new RayFire(this.world, worldPoint2.x, worldPoint2.y);
        rayon2.create();
        RayFire rayon3 = new RayFire(this.world, worldPoint3.x, worldPoint3.y);
        rayon3.create();
        RayFire rayon4 = new RayFire(this.world, worldPoint4.x, worldPoint4.y);
        rayon4.create();

        /**
         * Apply force to the specific point
         */
        Vec2 force = body.getWorldVector(rayonForce);

        // Under part
        Vec2 point1 = body.getWorldPoint(rayon1.getBody().getWorldCenter());
        Vec2 point2 = body.getWorldPoint(rayon2.getBody().getWorldCenter());
        Vec2 point3 = body.getWorldPoint(rayon3.getBody().getWorldCenter());
        Vec2 point4 = body.getWorldPoint(rayon4.getBody().getWorldCenter());

        /**
         * need to do transform to position the shoot in good direction
         */

        // Under part
        rayon1.getBody().setTransform(worldPoint1, body.getAngle());
        rayon1.getBody().applyForce(force, point1);
        rayon2.getBody().setTransform(worldPoint2, body.getAngle());
        rayon2.getBody().applyForce(force, point2);
        rayon3.getBody().setTransform(worldPoint3, body.getAngle());
        rayon3.getBody().applyForce(force, point3);
        rayon4.getBody().setTransform(worldPoint4, body.getAngle());
        rayon4.getBody().applyForce(force, point4);


    }

    @Override
    public void fireBomb() {
        throw new NotImplementedException();
    }

    @Override
    public void shield() {
        throw new NotImplementedException();
    }
}
