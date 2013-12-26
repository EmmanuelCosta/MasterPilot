package fr.umlv.masterPilot.ship.enemy;

import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.ship.hero.Hero;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Rot;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import java.awt.*;
import java.util.ArrayList;

public class Squadron implements SpaceShip {
    private final int maskBit;
    private final int category;
    private final World world;
    private final int x_axis;
    private final int y_axis;
    private final Hero hero;
    private final Vec2 forceLeft = new Vec2(-150f, -0f);
    private final Vec2 forceRight = new Vec2(+150f, -0f);
    private final Vec2 forceUp = new Vec2(-0f, +150f);
    private final Vec2 forceDown = new Vec2(-0f, -150f);
    private final ArrayList<Body> bodyJointList;
    private Body body;

    public Squadron(World world, int x_axis, int y_axis, Hero hero) {


        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;

        this.hero = hero;
        /**
         * Interactions with the other bodies.
         */
        this.category = MasterPilotWorld.ENEMY;
        this.maskBit = MasterPilotWorld.SHIELD
                | MasterPilotWorld.SHOOT
                | MasterPilotWorld.BOMB
                | MasterPilotWorld.MEGABOMB
                | MasterPilotWorld.HERO;

        this.bodyJointList = new ArrayList<>();
    }

    @Override
    public void create() {

        Vec2[] vertices = new Vec2[4];
        vertices[0] = new Vec2(-5, -5);
        vertices[1] = new Vec2(-5, 5);
        vertices[2] = new Vec2(5, 5);
        vertices[3] = new Vec2(5, -5);


        PolygonShape ps = new PolygonShape();
        //ps.set(vertices, 4);
        ps.setAsBox(10, 10);


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
        fd.friction = 20f;
        fd.restitution = 0.0005f;
        fd.filter.categoryBits = this.category;
        fd.filter.maskBits = this.maskBit;


        fd.userData = new SquadronBehaviour(this, Color.GREEN, this.bodyJointList);


        /**
         * Integrate the body in the world.
         */
        bd.angularDamping = 2.0f;
        bd.linearDamping = 0.0999f;
        Body body = this.world.createBody(bd);
        body.createFixture(fd);
        this.body = body;


        Vec2[] position = new Vec2[7];
        position[0] = new Vec2(x_axis + 25, y_axis + 25);
        position[1] = new Vec2(x_axis - 25, y_axis - 25);
        position[2] = new Vec2(x_axis - 25, y_axis + 25);
        position[3] = new Vec2(x_axis + 25, y_axis - 25);
        position[4] = new Vec2(x_axis + 40, y_axis - 15);
        position[5] = new Vec2(x_axis - 40, y_axis - 15);
        position[6] = new Vec2(x_axis + 0, y_axis - 25);
        //TRIANGLE JOINT CREATION
        for (int i = 0; i < 7; i++) {

            Vec2[] triangle = new Vec2[3];
            triangle[0] = new Vec2(0, -10);
            triangle[1] = new Vec2(-5, 0);
            triangle[2] = new Vec2(5, 0);

            ps.set(triangle, 3);

            FixtureDef ft = new FixtureDef();
            ft.shape = ps;
            ft.density = 1.0f;
            ft.friction = 0.2f;
            ft.filter.maskBits = maskBit;
            ft.filter.categoryBits = category;
            ft.userData = new TriangleBehaviour(this, Color.BLUE);

            BodyDef bg = new BodyDef();
            bg.position.set(x_axis, y_axis);
            bg.type = BodyType.DYNAMIC;

            bg.position.set(position[i].x, position[i].y);

            Body bodyTriangle = this.world.createBody(bg);

            bodyTriangle.createFixture(ft);
            bodyJointList.add(bodyTriangle);
            Vec2 anchor = new Vec2(x_axis, y_axis);


            RevoluteJointDef jd = new RevoluteJointDef();
            jd.collideConnected = false;
            //   jd.enableLimit = true;


            jd.initialize(body, bodyTriangle, body.getWorldCenter());
//            jd.enableLimit = true;
//            jd.upperAngle = 15.5f;
            world.createJoint(jd);


            body.createFixture(ft);
        }
        //***********************


    }

    @Override
    public void right() {
        Vec2 force = body.getWorldVector(forceRight);
        this.body.setTransform(body.getPosition(), this.body.getAngle());


        this.body.applyForceToCenter(force);

    }

    @Override
    public void left() {
        Vec2 force = body.getWorldVector(forceLeft);

        this.body.setTransform(body.getPosition(), this.body.getAngle());

        this.body.applyForceToCenter(force);


    }

    @Override
    public void up() {
        Vec2 force = body.getWorldVector(forceUp);
        this.body.setTransform(body.getPosition(), this.body.getAngle());

        this.body.applyForceToCenter(force);

    }

    @Override
    public void down() {
        Vec2 force = body.getWorldVector(forceDown);
        this.body.setTransform(body.getPosition(), this.body.getAngle());

        this.body.applyForceToCenter(force);

    }

    @Override
    public void doMove() {
        float x_distance = body.getPosition().x - hero.getBody().getPosition().x;
        float y_distance = body.getPosition().y - hero.getBody().getPosition().y;
        int limit = 100;

        fire();

        if (x_distance <= 0 && y_distance >= 0) {

            if (x_distance > -limit) {
                left();

            } else {

                right();
            }

            down();


        } else if (x_distance <= 0 && y_distance <= 0) {


            right();

            if (y_distance > -limit) {
                down();
            } else {
                up();
            }


        } else if (x_distance >= 0 && y_distance <= 0) {


            if (x_distance < limit) {
                right();
            } else {
                left();
            }
            up();

        } else if (x_distance >= 0 && y_distance >= 0) {


            left();

            if (y_distance < limit) {
                up();
            } else {
                down();
            }


        }

    }

    @Override
    public void fire() {
        float angle1 = this.hero.getBody().getAngle();
        for (Body bd : bodyJointList) {
           double angle2 = Math.atan2(bd.getPosition().y,bd.getPosition().x);

            bd.setTransform(bd.getPosition(),bd.getAngle()-(float)angle2);


        }


    }

    @Override
    public void fireBomb() {
        // TODO Auto-generated method stub

    }

    @Override
    public void shield() {
        // TODO Auto-generated method stub

    }

    @Override
    public Body getBody() {
        return this.body;
    }


}
