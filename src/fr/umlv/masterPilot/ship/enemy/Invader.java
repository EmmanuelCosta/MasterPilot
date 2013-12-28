package fr.umlv.masterPilot.ship.enemy;

import fr.umlv.masterPilot.ship.RayFire;
import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.ship.hero.Hero;
import fr.umlv.masterPilot.world.MasterPilotWorld;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;

public class Invader implements SpaceShip {

    private int maskBit;
    private int category;
    private int x_axis;
    private int y_axis;
    private Hero hero;
    private Vec2 forceLeft;
    private Vec2 forceRight;
    private Vec2 forceUp;
    private Vec2 forceDown;
    private World world;
    private Vec2 shootUp;
    private Vec2 shootLeft;
    private Vec2 shootRight;
    private Body body;
    private Vec2 shoot1;
    private Vec2 shoot2;
    private volatile boolean fire;

    public Invader(World world, int x_axis, int y_axis, Hero hero) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.hero = hero;

        /**
         * Interactions with the other bodies.
         */
        this.category = MasterPilotWorld.ENEMY;
        this.maskBit = MasterPilotWorld.SHIELD | MasterPilotWorld.SHOOT
                | MasterPilotWorld.BOMB | MasterPilotWorld.MEGABOMB
                | MasterPilotWorld.HERO | MasterPilotWorld.PLANET;

        /**
         * Forces definitions
         */
        this.forceUp = new Vec2(0, +1000f);
        this.forceLeft = new Vec2(-1000f, 0);
        this.forceRight = new Vec2(+1000f, 0);
        this.forceDown = new Vec2(0, -1000f);

    }

    @Override
    public void create() {

        PolygonShape ps = new PolygonShape();

        /**
         * Number of vertices.
         */
        Vec2[] vertices = new Vec2[7];
        vertices[0] = new Vec2(-5, -5);
        vertices[1] = new Vec2(-2.5f, -10f);
        vertices[2] = new Vec2(+2.5f, -10f);
        vertices[3] = new Vec2(+5, -5);
        vertices[4] = new Vec2(+5, +10);
        vertices[5] = new Vec2(+0, +20);
        vertices[6] = new Vec2(-5, +10);
        
        this.shootUp = vertices[5];
        ps.set(vertices, 7);

        /**
         * Body definition of the Invader
         */
        BodyDef bd = new BodyDef();
        bd.position.set(x_axis, y_axis);
        bd.type = BodyType.DYNAMIC;
        bd.userData = this.getClass();
        bd.angularDamping = 2.0f;
        bd.linearDamping = 0.0999f;
        bd.angularDamping = 2.0f;
        bd.linearDamping = 0.0999f;
        Body body = this.world.createBody(bd);

        /**
         * Body fixtures of the Invader
         */
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 1.5f;
        fd.friction = 8f;
        fd.restitution = 0.5f;
        fd.filter.categoryBits = this.category;
        fd.filter.maskBits = this.maskBit;
        fd.userData = new EnemyBehaviour(this, Color.green);
        body.createFixture(fd);

        /***************** LEFT WINGS ***********************/
        PolygonShape leftWing = new PolygonShape();

        /**
         * Number of vertices.
         */
        Vec2[] leftWingVertices = new Vec2[5];
        leftWingVertices[0] = new Vec2(-5, +5);
        leftWingVertices[1] = new Vec2(-10, +5);
        leftWingVertices[2] = new Vec2(-15, +0);
        leftWingVertices[3] = new Vec2(-10, -5);
        leftWingVertices[4] = new Vec2(-5, -5);
        
        this.shootLeft = leftWingVertices[2];
        leftWing.set(leftWingVertices, 5);

        /**
         * Body fixtures of the Invader
         */
        FixtureDef fdlw = new FixtureDef();
        fdlw.shape = leftWing;
        fdlw.density = 1.5f;
        fdlw.friction = 8f;
        fdlw.restitution = 0.5f;
        fdlw.filter.categoryBits = this.category;
        fdlw.filter.maskBits = this.maskBit;
        fdlw.userData = new EnemyBehaviour(this, Color.green);
        body.createFixture(fdlw);

        /***************** RIGHT WINGS ***********************/

        PolygonShape rightWing = new PolygonShape();

        /**
         * Number of vertices.
         */
        Vec2[] rightWingVertices = new Vec2[5];
        rightWingVertices[0] = new Vec2(+5, +5);
        rightWingVertices[1] = new Vec2(+10, +5);
        rightWingVertices[2] = new Vec2(+15, +0);
        rightWingVertices[3] = new Vec2(+10, -5);
        rightWingVertices[4] = new Vec2(+5, -5);
        
        this.shootRight = rightWingVertices[2];
        rightWing.set(rightWingVertices, 5);

        /**
         * Body fixtures of the Invader
         */
        FixtureDef fdrw = new FixtureDef();
        fdrw.shape = rightWing;
        fdrw.density = 1.5f;
        fdrw.friction = 8f;
        fdrw.restitution = 0.5f;
        fdrw.filter.categoryBits = this.category;
        fdrw.filter.maskBits = this.maskBit;
        fdrw.userData = new EnemyBehaviour(this, Color.green);
        body.createFixture(fdrw);

        /**
         * Make the Invader fire
         */
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (;;) {
                    fire = true;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

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
        if (fire == true) {
            fire();
            fire = false;
        }
    }

    @Override
    public void fire() {
        
        /**
         * I get the actual tip coordinate in the world
         */
        Vec2 worldPointUp = body.getWorldPoint(shootUp);
        Vec2 worldPointLeft = body.getWorldPoint(shootLeft);
        Vec2 worldPointRight = body.getWorldPoint(shootRight);

        /**
         * create the shoot
         */
        RayFire rayonUp = new RayFire(this.world, worldPointUp.x,
                worldPointUp.y, MasterPilotWorld.ENEMY, MasterPilotWorld.HERO
                        | MasterPilotWorld.BOMB | MasterPilotWorld.MEGABOMB
                        | MasterPilotWorld.SHIELD | MasterPilotWorld.PLANET,
                Color.yellow);
        rayonUp.create();

        RayFire rayonLeft = new RayFire(this.world, worldPointLeft.x,
                worldPointLeft.y, MasterPilotWorld.ENEMY, MasterPilotWorld.HERO
                        | MasterPilotWorld.BOMB | MasterPilotWorld.MEGABOMB
                        | MasterPilotWorld.SHIELD | MasterPilotWorld.PLANET,
                Color.yellow);
        rayonLeft.create();

        RayFire rayonRight = new RayFire(this.world, worldPointRight.x,
                worldPointRight.y, MasterPilotWorld.ENEMY,
                MasterPilotWorld.HERO | MasterPilotWorld.BOMB
                        | MasterPilotWorld.MEGABOMB | MasterPilotWorld.SHIELD
                        | MasterPilotWorld.PLANET, Color.yellow);
        rayonRight.create();

        /**
         * need to do transform to position the shoot in good direction
         */
        rayonUp.getBody().setTransform(worldPointUp, hero.getBody().getAngle());
        rayonLeft.getBody().setTransform(worldPointLeft, hero.getBody().getAngle());
        rayonRight.getBody().setTransform(worldPointRight, hero.getBody().getAngle());

        /**
         * Tansform the position of the shoot UP in the right direction
         */
        Vec2 pointUp = rayonUp.getBody().getWorldCenter();
        Vec2 blastDirUp = pointUp.sub(hero.getBody().getPosition());
        rayonUp.getBody().applyLinearImpulse(blastDirUp.mul(-10000), pointUp);

        /**
         * Tansform the position of the shoot DOWN in the right direction
         */
        Vec2 pointLeft = rayonLeft.getBody().getWorldCenter();
        Vec2 blastDirLeft = pointLeft.sub(hero.getBody().getPosition());
        rayonLeft.getBody().applyLinearImpulse(blastDirLeft.mul(-10000), pointLeft);

        /**
         * Tansform the position of the shoot DOWN in the right direction
         */
        Vec2 pointRight = rayonRight.getBody().getWorldCenter();
        Vec2 blastDirRight = pointUp.sub(hero.getBody().getPosition());
        rayonRight.getBody().applyLinearImpulse(blastDirRight.mul(-10000), pointRight);
    }

    @Override
    public void fireBomb() {
        throw new NotImplementedException();
    }

    @Override
    public void shield() {
        throw new NotImplementedException();
    }

    public Body getBody() {
        return body;
    }

}
