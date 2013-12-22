package fr.umlv.masterPilot.ship.enemy;

import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.dynamics.joints.WeldJointDef;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Squadron implements SpaceShip {
    private final int maskBit;
    private final int category;
    private final World world;
    private final int x_axis;
    private final int y_axis;
    private Body body;
    private final ArrayList<Body> bodyJointList;

    public Squadron(World world, int x_axis, int y_axis) {


        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;

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


        fd.userData = new UserSpec() {
            private boolean collide = false;

            @Override
            public void onCollide(Fixture fix2, boolean flag) {

                if (fix2.getFilterData().categoryBits == MasterPilotWorld.SHOOT) {

                        collide = true;

                }
            }

            @Override
            public boolean isDestroyable() {
                return collide;
            }

            @Override
            public List<Body> getJointBody() {
                return bodyJointList;
            }

            @Override
            public boolean hasJointBody() {
                return true;
            }
        };

        /**
         * Integrate the body in the world.
         */
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
            //ft.userData = new TriangleBehaviour(this, Color.BLUE);

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
            jd.enableLimit = true;


            WeldJointDef weldJointDef = new WeldJointDef();
            PrismaticJointDef pr = new PrismaticJointDef();

            pr.initialize(body, bodyTriangle, anchor, new Vec2(x_axis, y_axis));

            weldJointDef.initialize(body, bodyTriangle, body.getWorldCenter());
            weldJointDef.collideConnected = false;
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
        // TODO Auto-generated method stub

    }

    @Override
    public void left() {
        // TODO Auto-generated method stub

    }

    @Override
    public void up() {
        // TODO Auto-generated method stub

    }

    @Override
    public void down() {
        // TODO Auto-generated method stub

    }

    @Override
    public void fire() {
        // TODO Auto-generated method stub

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
