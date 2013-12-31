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

import java.awt.*;

/**
 * this is an ennemy look like a rod
 * and can only fire in one side
 * can only goes left to right or up and down
 *  @author Babala Costa Emmanuel
 */
public class Cruiser implements SpaceShip {
    private  final Vec2 maxSpeed;
    private final int maskBit;
    private final int category;
    private final World world;
    private final int x_axis;
    private final int y_axis;
    private final Hero hero;
    private final Vec2 rayonForce = new Vec2(+0f, -150f);
    private final Vec2 forceLeft = new Vec2(-400f, +0f);
    private final Vec2 forceRight = new Vec2(+400f, +0f);
    private final Vec2 forceUp = new Vec2(+0f, +170000f);
    private final Vec2 forceDown = new Vec2(+0f, -170000f);
    private Vec2 shoot1;
    private Vec2 shoot2;
    private Vec2 shoot3;
    private Vec2 shoot4;
    private volatile boolean fire;
    private Body body;
    private boolean direction = false;
    private Vec2 reference;
    private Thread thread;
    
    /**
     *
     * @param world
     * @param x_axis : X coordinate of his initial position in yhe world
     * @param y_axis : Y cordinate of his initial position in yhe world
     * @param hero : the hero
     */
    public Cruiser(World world, int x_axis, int y_axis, Hero hero) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.hero = hero;
        this.maxSpeed =hero.getMaxSpeed();

        /**
         * Interactions with the other bodies.
         */
        this.category = MasterPilotWorld.ENEMY;
        this.maskBit = MasterPilotWorld.PLANET | MasterPilotWorld.SHIELD
                | MasterPilotWorld.SHOOT | MasterPilotWorld.BOMB
                | MasterPilotWorld.MEGABOMB | MasterPilotWorld.HERO;
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
        vertices[0] = new Vec2(-15, +5);
        vertices[1] = new Vec2(-15, -5);
        vertices[2] = new Vec2(+15, -5);
        vertices[3] = new Vec2(+15, +5);
        this.shoot1 = vertices[1];
        this.shoot2 = vertices[2];
        this.shoot3 = new Vec2(vertices[1].x + 8, vertices[1].y);
        this.shoot4 = new Vec2(vertices[1].x + 20, vertices[1].y);
        this.reference = vertices[0];
        ps.set(vertices, 4);

        /**
         * Body definition of the Cruiser
         */
        BodyDef bd = new BodyDef();
        bd.position.set(x_axis, y_axis);
        bd.type = BodyType.DYNAMIC;
        bd.userData = this.getClass();
        bd.angularDamping = 4f;
        bd.linearDamping = 0.1f;

        /**
         * Body fixtures of the Cruiser
         */
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 2.5f;
        fd.friction = 10f;
        fd.restitution = 0.05f;
        fd.filter.categoryBits = this.category;
        fd.filter.maskBits = this.maskBit;
        fd.userData = new EnemyBehaviour( Color.CYAN);

        /*********************************************************/

        CircleShape cs = new CircleShape();
        cs.setRadius(25);
        
        FixtureDef fs = new FixtureDef();
        fs.shape = cs;
        fs.isSensor = true;
        fs.density = 0.0f;
        fs.friction = 0.3f;
        fs.restitution = 0.5f;
        fs.filter.categoryBits = MasterPilotWorld.ENEMY;
        fs.filter.maskBits = MasterPilotWorld.SHOOT | MasterPilotWorld.PLANET
                | MasterPilotWorld.SHIELD | MasterPilotWorld.HERO;
        fs.userData = new EnemyShieldBehaviour(3);

        /**********************************************************/

        /**
         * Integrate the body in the world.
         */
        Body body = this.world.createBody(bd);
        body.createFixture(fd);
        body.createFixture(fs);
        this.body = body;

        this.thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    fire = true;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public Body getBody() {
        return this.body;
    }

    @Override
    public void right() {
        Vec2 worldPoint1 = body.getWorldPoint(shoot1);
        Vec2 worldPoint2 = body.getWorldPoint(shoot2);
        Vec2 force;
        
        if (worldPoint1.x < worldPoint2.x) {
            force = body.getWorldVector(forceRight);
        } else {
            force = body.getWorldVector(forceLeft);
        }
        
        this.body.setTransform(body.getPosition(), this.body.getAngle());
        Vec2 lVelocity = this.body.getLinearVelocity();

        if (Math.abs(lVelocity.y) <= maxSpeed.y) {
            this.body.applyForceToCenter(force);
        }
    }

    @Override
    public void left() {
        Vec2 worldPoint1 = body.getWorldPoint(shoot1);
        Vec2 worldPoint2 = body.getWorldPoint(shoot2);
        Vec2 force;
        
        if (worldPoint1.x < worldPoint2.x) {
            force = body.getWorldVector(forceLeft);
        } else {
            force = body.getWorldVector(forceRight);
        }
        
        this.body.setTransform(body.getPosition(), this.body.getAngle());
        Vec2 lVelocity = this.body.getLinearVelocity();

        if (Math.abs(lVelocity.y) <= maxSpeed.y) {
            this.body.applyForceToCenter(force);
        }
    }

    @Override
    public void up() {
        Vec2 worldPoint1 = body.getWorldPoint(shoot1);
        Vec2 worldPoint2 = body.getWorldPoint(shoot2);
        Vec2 force;
        
        if (worldPoint1.x < worldPoint2.x) {
            force = body.getWorldVector(forceUp);
        } else {
            force = body.getWorldVector(forceDown);
        }
        
        this.body.setTransform(body.getPosition(), this.body.getAngle());
        Vec2 lVelocity = this.body.getLinearVelocity();

        if (Math.abs(lVelocity.y) <= maxSpeed.y) {
            this.body.applyForceToCenter(force);
        }
    }

    @Override
    public void down() {
        Vec2 worldPoint1 = body.getWorldPoint(shoot1);
        Vec2 worldPoint2 = body.getWorldPoint(shoot2);
        Vec2 force;
        
        if (worldPoint1.x < worldPoint2.x) {
            force = body.getWorldVector(forceDown);
        } else {
            force = body.getWorldVector(forceUp);
        }
        
        this.body.setTransform(body.getPosition(), this.body.getAngle());
        Vec2 lVelocity = this.body.getLinearVelocity();

        if (Math.abs(lVelocity.y) <= maxSpeed.y) {
            this.body.applyForceToCenter(force);
        }
    }

    @Override
    public void fire() {

        /**
         * I get the actual tip coordinate in the world
         */
        Vec2 worldPoint1 = body.getWorldPoint(shoot1);
        Vec2 worldPoint2 = body.getWorldPoint(shoot2);
        Vec2 worldPoint3 = body.getWorldPoint(shoot3);
        Vec2 worldPoint4 = body.getWorldPoint(shoot4);

        /**
         * create the shoot
         */
        RayFire rayon1 = new RayFire(this.world, worldPoint1.x, worldPoint1.y,
                MasterPilotWorld.ENEMY, MasterPilotWorld.HERO
                | MasterPilotWorld.BOMB | MasterPilotWorld.MEGABOMB
                | MasterPilotWorld.SHIELD | MasterPilotWorld.PLANET,
                Color.yellow);
        rayon1.create();
        RayFire rayon2 = new RayFire(this.world, worldPoint2.x, worldPoint2.y,
                MasterPilotWorld.ENEMY, MasterPilotWorld.HERO
                | MasterPilotWorld.BOMB | MasterPilotWorld.MEGABOMB
                | MasterPilotWorld.SHIELD | MasterPilotWorld.PLANET,
                Color.yellow);
        rayon2.create();
        RayFire rayon3 = new RayFire(this.world, worldPoint3.x, worldPoint3.y,
                MasterPilotWorld.ENEMY, MasterPilotWorld.HERO
                | MasterPilotWorld.BOMB | MasterPilotWorld.MEGABOMB
                | MasterPilotWorld.SHIELD | MasterPilotWorld.PLANET,
                Color.yellow);
        rayon3.create();
        RayFire rayon4 = new RayFire(this.world, worldPoint4.x, worldPoint4.y,
                MasterPilotWorld.ENEMY, MasterPilotWorld.HERO
                | MasterPilotWorld.BOMB | MasterPilotWorld.MEGABOMB
                | MasterPilotWorld.SHIELD | MasterPilotWorld.PLANET,
                Color.yellow);
        rayon4.create();

        /**
         * Apply force to the specific point
         */
        Vec2 force = body.getWorldVector(rayonForce);
        Vec2 point1 = body.getWorldPoint(rayon1.getBody().getWorldCenter());
        Vec2 point2 = body.getWorldPoint(rayon2.getBody().getWorldCenter());
        Vec2 point3 = body.getWorldPoint(rayon3.getBody().getWorldCenter());
        Vec2 point4 = body.getWorldPoint(rayon4.getBody().getWorldCenter());

        /**
         * need to do transform to position the shoot in good direction
         */
        rayon1.getBody().setTransform(worldPoint1, body.getAngle());
        rayon1.getBody().applyForce(force, point1);
        rayon2.getBody().setTransform(worldPoint2, body.getAngle());
        rayon2.getBody().applyForce(force, point2);
        rayon3.getBody().setTransform(worldPoint3, body.getAngle());
        rayon3.getBody().applyForce(force, point3);
        rayon4.getBody().setTransform(worldPoint4, body.getAngle());
        rayon4.getBody().applyForce(force, point4);

        /**
         * add to manager in order to suppress it
         */
        RayFireManager.addRayFire(new Vec2().set(body.getPosition()), rayon1);
        RayFireManager.addRayFire(new Vec2().set(body.getPosition()), rayon2);
        RayFireManager.addRayFire(new Vec2().set(body.getPosition()), rayon3);
        RayFireManager.addRayFire(new Vec2().set(body.getPosition()), rayon4);
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
    public void doMove() {
        float x_distance = body.getPosition().x - hero.getBody().getPosition().x;
        float y_distance = body.getPosition().y - hero.getBody().getPosition().y;
        int limit = 200;
        Vec2 worldPoint1 = body.getWorldPoint(shoot1);
        Vec2 worldPoint2 = body.getWorldPoint(shoot2);
        Vec2 worldPoint3 = body.getWorldPoint(reference);

        //MANAGE WITH RECUL WHEN COLLISION WITH HERO IS POSSIBLE
        if ((y_distance < 50 && y_distance >= 0) && ((x_distance <= 0 && x_distance > -50)
                || (x_distance >= 0 && x_distance < 50))) {
            up();
            return;
        } else if ((y_distance > -50 && y_distance <= 0) && ((x_distance <= 0 && x_distance > -50)
                || (x_distance >= 0 && x_distance < 50))) {
            down();
            return;
        }

        //MANAGE TORQUING
        if (y_distance > 0) {
            if (((int) (worldPoint1.y - worldPoint2.y) != 0) || (worldPoint1.y - worldPoint3.y >= 0)) {
                this.body.setLinearVelocity(new Vec2());
                this.body.setTransform(body.getPosition(), body.getAngle() - 0.05f);
                return;
            }
        } else if (y_distance < 0) {
            if (((int) (worldPoint1.y - worldPoint2.y) != 0) || (worldPoint1.y - worldPoint3.y <= 0)) {
                this.body.setLinearVelocity(new Vec2());
                this.body.setTransform(body.getPosition(), body.getAngle() + 0.05f);
                return;
            }
        }
        if ((x_distance >= -50 && x_distance <= 50) && (y_distance >= -limit && y_distance <= limit)
                && fire == true) {
            fire();
            fire = false;
        }
        if (y_distance > limit) {
            down();
            this.body.setLinearVelocity(new Vec2());
        } else if (y_distance < -limit) {
            up();
            this.body.setLinearVelocity(new Vec2());
        }
        if (x_distance < -limit && this.direction == false) {
            this.direction = true;
            this.body.setLinearVelocity(new Vec2());
        } else if (x_distance > limit && this.direction) {
            this.direction = false;
            this.body.setLinearVelocity(new Vec2());
        }
        if (this.direction) {
            right();
        } else {
            left();
        }
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