package fr.umlv.masterPilot.ship.enemy;

import fr.umlv.masterPilot.ship.RayFire;
import fr.umlv.masterPilot.ship.RayFireManager;
import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.ship.hero.Hero;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import java.awt.*;
import java.util.ArrayList;

/**
 * an ennemy spaceShip which is protected by a 7 small triangle
 * the mother ship is a square
 * @author Babala Costa Emmanuel
 */
public class Squadron implements SpaceShip {
    private final int maskBit;
    private final int category;
    private final World world;
    private final int x_axis;
    private final int y_axis;
    private final Hero hero;
    private final Vec2 forceLeft = new Vec2(-150f, 0f);
    private final Vec2 forceRight = new Vec2(+150f, 0f);
    private final Vec2 forceUp = new Vec2(0f, +150f);
    private final Vec2 forceDown = new Vec2(0f, -150f);
    private final ArrayList<Body> bodyJointList;
    private final Vec2 maxSpeed;
    private volatile boolean fire;
    private Vec2 referenceVertices1;
    private Vec2 referenceVertices2;
    private Body body;
    private RadarBehaviour radar;
    private Thread thread;

    /**
     *
     * @param world
     * @param x_axis : X coordinate of his initial position in yhe world
     * @param y_axis : Y cordinate of his initial position in yhe world
     * @param hero : the hero
     */
    public Squadron(World world, int x_axis, int y_axis, Hero hero) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.hero = hero;
        this.maxSpeed=hero.getMaxSpeed();
        
        /**
         * Interactions with the other bodies.
         */
        this.category = MasterPilotWorld.ENEMY;
        this.maskBit = MasterPilotWorld.SHIELD
                | MasterPilotWorld.SHOOT
                | MasterPilotWorld.BOMB
                | MasterPilotWorld.MEGABOMB
                | MasterPilotWorld.PLANET
                | MasterPilotWorld.HERO;
        this.bodyJointList = new ArrayList<>();
    }

    @Override
    public void create() {
        Vec2[] vertices = new Vec2[4];
        vertices[0] = new Vec2(-8, -8);
        vertices[1] = new Vec2(-8, 8);
        vertices[2] = new Vec2(8, 8);
        vertices[3] = new Vec2(8, -8);
        this.referenceVertices1 = vertices[0];
        this.referenceVertices2 = vertices[3];

        PolygonShape ps = new PolygonShape();
        ps.set(vertices, 4);
        
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
        fd.userData = new SquadronBehaviour( Color.GREEN, this.bodyJointList);

        /**
         * Integrate the body in the world.
         */
        bd.angularDamping = 2.0f;
        bd.linearDamping = 0.0999f;
        Body body = this.world.createBody(bd);
        body.createFixture(fd);
        this.body = body;

        /************invisible radar**************/

        CircleShape cs = new CircleShape();
        cs.setRadius(75);
        
        FixtureDef fs = new FixtureDef();
        fs.shape = cs;
        fs.isSensor = false;
        fs.density = 0.0f;
        fs.friction = 0.3f;
        fs.restitution = 0.5f;
        fs.filter.categoryBits = MasterPilotWorld.RADAR;
        fs.filter.maskBits = MasterPilotWorld.HERO | MasterPilotWorld.SHOOT | MasterPilotWorld.SHIELD;
        this.radar = new RadarBehaviour();
        fs.userData = this.radar;
        body.createFixture(fs);

        /******************************************/

        Vec2[] position = new Vec2[7];
        position[0] = new Vec2(x_axis + 25, y_axis + 25);
        position[1] = new Vec2(x_axis - 25, y_axis - 25);
        position[2] = new Vec2(x_axis - 25, y_axis + 25);
        position[3] = new Vec2(x_axis + 25, y_axis - 25);
        position[4] = new Vec2(x_axis + 40, y_axis - 15);
        position[5] = new Vec2(x_axis - 40, y_axis - 15);
        position[6] = new Vec2(x_axis, y_axis - 25);
        
        //TRIANGLE JOINT CREATION
        for (int i = 0; i < 7; i++) {
            PolygonShape px = new PolygonShape();
            Vec2[] triangle = new Vec2[3];
            triangle[0] = new Vec2(0, -8);
            triangle[1] = new Vec2(-5, 0);
            triangle[2] = new Vec2(5, 0);
            px.set(triangle, 3);

            FixtureDef ft = new FixtureDef();
            ft.shape = px;
            ft.density = 1.0f;
            ft.friction = 0.2f;
            ft.filter.maskBits = maskBit;
            ft.filter.categoryBits = category;
            ft.userData = new TriangleBehaviour( Color.BLUE, this.bodyJointList);

            BodyDef bg = new BodyDef();
            bg.type = BodyType.DYNAMIC;
            bg.position.set(position[i].x, position[i].y);
            Body bodyTriangle = this.world.createBody(bg);
            bodyTriangle.createFixture(ft);
            bodyJointList.add(bodyTriangle);

            RevoluteJointDef jd = new RevoluteJointDef();
            jd.initialize(body, bodyTriangle, body.getWorldCenter());
            jd.collideConnected = false;
            jd.enableLimit = true;
            jd.upperAngle = 1;
            jd.enableMotor = true;
            world.createJoint(jd);
            body.createFixture(ft);
        }
        
        this.thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    fire = true;
                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        });
        this.thread.start();
    }

    @Override
    public void right() {
        Vec2 worldPoint1 = body.getWorldPoint( this.referenceVertices1);
        Vec2 worldPoint2 = body.getWorldPoint( this.referenceVertices2);
        Vec2 force;
        
        if (worldPoint1.x < worldPoint2.x) {
            force = body.getWorldVector(forceRight);
        } else {
            force = body.getWorldVector(forceLeft);
        }
        
        this.body.setTransform(body.getPosition(), this.body.getAngle());
        Vec2 lVelocity = this.body.getLinearVelocity();

        if (Math.abs(lVelocity.y) < maxSpeed.y) {
            this.body.applyForceToCenter(force);
        }
    }

    @Override
    public void left() {
        Vec2 worldPoint1 = body.getWorldPoint( this.referenceVertices1);
        Vec2 worldPoint2 = body.getWorldPoint( this.referenceVertices2);
        Vec2 force;
        
        if (worldPoint1.x < worldPoint2.x) {
            force = body.getWorldVector(forceLeft);
        } else {
            force = body.getWorldVector(forceRight);
        }
        
        this.body.setTransform(body.getPosition(), this.body.getAngle());
        Vec2 lVelocity = this.body.getLinearVelocity();

        if (Math.abs(lVelocity.y) < maxSpeed.y) {
            this.body.applyForceToCenter(force);
        }

    }

    @Override
    public void up() {
        Vec2 worldPoint1 = body.getWorldPoint( this.referenceVertices1);
        Vec2 worldPoint2 = body.getWorldPoint( this.referenceVertices2);
        Vec2 force;
        
        if (worldPoint1.x < worldPoint2.x) {
            force = body.getWorldVector(forceUp);
        } else {
            force = body.getWorldVector(forceDown);
        }
        
        this.body.setTransform(body.getPosition(), this.body.getAngle());
        Vec2 lVelocity = this.body.getLinearVelocity();

        if (Math.abs(lVelocity.y) < maxSpeed.y) {
            this.body.applyForceToCenter(force);
        }
    }

    @Override
    public void down() {
        Vec2 worldPoint1 = body.getWorldPoint( this.referenceVertices1);
        Vec2 worldPoint2 = body.getWorldPoint( this.referenceVertices2);
        Vec2 force;
        
        if (worldPoint1.x < worldPoint2.x) {
            force = body.getWorldVector(forceDown);
        } else {
            force = body.getWorldVector(forceUp);
        }
        
        this.body.setTransform(body.getPosition(), this.body.getAngle());
        Vec2 lVelocity = this.body.getLinearVelocity();

        if (Math.abs(lVelocity.y) < maxSpeed.y) {
            this.body.applyForceToCenter(force);
        }
    }


    @Override
    public void fire() {
        ArrayList<Body> bdt = new ArrayList<>();
        
        for (Body b = this.world.getBodyList(); b != null; b = b.getNext()) {
            if (this.bodyJointList.contains(b)) {
                bdt.add(b);
            }
        }
        for (Body bd : bdt) {

            Vec2 worldPoint1 = bd.getWorldPoint(new Vec2(0, -10));
            RayFire rayon1 = new RayFire(this.world, worldPoint1.x, worldPoint1.y, category, MasterPilotWorld.SHIELD
                    | MasterPilotWorld.BOMB
                    | MasterPilotWorld.MEGABOMB
                    | MasterPilotWorld.HERO, Color.CYAN);
            rayon1.create();
            Vec2 worldCenter = body.getWorldCenter();
            Vec2 blastDir = worldCenter.sub(hero.getBody().getPosition());
            rayon1.getBody().applyLinearImpulse(blastDir.mul(-10000), worldCenter);
            RayFireManager.addRayFire(new Vec2().set(body.getPosition()), rayon1);
        }
    }

    @Override
    public void doMove() {
        float x_distance = body.getPosition().x - hero.getBody().getPosition().x;
        float y_distance = body.getPosition().y - hero.getBody().getPosition().y;
        int limit = 150;
        
        tryToProtectTheMotherShip();

        //MANAGE WITH RECUL WHEN COLLISION WITH HERO IS POSSIBLE
        if ((y_distance < 50 && y_distance >= 0) && ((x_distance <= 0 && x_distance > -150)
                || (x_distance >= 0 && x_distance < 150))) {
            up();
            return;
        } else if ((y_distance > -150 && y_distance <= 0) && ((x_distance <= 0 && x_distance > -150)
                || (x_distance >= 0 && x_distance < 150))) {
            down();
            return;
        }
        
        /**
         * fire in this area
         */
        if (x_distance <= limit+50 && x_distance >= -limit-50
                && y_distance <= limit+50 && y_distance >= -limit-50 && fire == true) {
            fire();
            fire = false;
        }

        /**
         * set a limit altitude
         */
        if (y_distance >= 0 && y_distance <= limit) {
            up();
        } else if (y_distance >= limit) {
            down();
        } else if (y_distance <= 0 && y_distance >= -limit) {
            down();
        } else if (y_distance <= -limit) {
            up();
        }
        /**
         * set a limit latitude
         */
        if (x_distance >= 0 && x_distance <= limit) {
            right();
        } else if (x_distance >= limit) {
            left();
        } else if (x_distance <= 0 && x_distance > -limit) {
            left();
        } else if (x_distance <= -limit) {
            right();
        }
    }

    /**
     * algo of protection of the mothership
     */
    private void tryToProtectTheMotherShip() {
        Vec2 positionOfCollision = this.radar.getPositionOfCollision();
        
        if (!positionOfCollision.equals(new Vec2())) {
            for (Body b = this.world.getBodyList(); b != null; b = b.getNext()) {
                if (this.bodyJointList.contains(b)) {
                    Vec2 worldCenter = b.getWorldCenter();
                    Vec2 blastDir = worldCenter.sub(positionOfCollision);
                    body.applyLinearImpulse(blastDir.mul(9), worldCenter);
                    b.applyLinearImpulse(blastDir.mul(-9), worldCenter);
                }
            }
        }
    }

    @Override
    public void fireBomb() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void shield() {
        throw new UnsupportedOperationException();

    }

    @Override
    public Body getBody() {
        return this.body;
    }

    @Override
    public boolean destroySpaceShip() {
        if (this.thread.isAlive()) {
            thread.interrupt();
            return true;
        }
        return false;
    }
}
