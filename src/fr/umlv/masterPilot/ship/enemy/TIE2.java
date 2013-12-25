package fr.umlv.masterPilot.ship.enemy;

import fr.umlv.masterPilot.ship.RayFire;
import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.ship.hero.Hero;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.util.Timer;

/**
 * Created by emmanuel on 22/12/13.
 */
public class TIE2 implements SpaceShip {

    private final int maskBit;
    private final int category;
    private final Timer timer = new Timer();
    private final int x_axis;
    private final int y_axis;
    private final Hero hero;
    private final Vec2 forceLeft = new Vec2(-150f, -5f);
    private final Vec2 forceRight = new Vec2(+150f, -5f);
    private final Vec2 forceUp = new Vec2(-5f, +150f);
    private final Vec2 forceDown = new Vec2(-5f, -150f);
    private final World world;
    private final Vec2 fireUp = new Vec2(0f, -10000f);
    private final Vec2 fireDown = new Vec2(0f, 10000f);
    private Body body;
    private Vec2 shoot1;
    private Vec2 shoot2;
    private volatile boolean fire;

    public TIE2(World world, int x_axis, int y_axis, Hero hero) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.hero = hero;


        /**
         *  Interactions with the other bodies.
         */
        this.category = MasterPilotWorld.ENEMY;
        this.maskBit = MasterPilotWorld.SHIELD
                | MasterPilotWorld.SHOOT
                | MasterPilotWorld.BOMB
                | MasterPilotWorld.MEGABOMB
                | MasterPilotWorld.HERO;
    }

    public void create() {

        /**
         * Primitive form
         */

        CircleShape cs = new CircleShape();
        cs.setRadius(5);


        /**
         * Body definition of the TIE
         */
        BodyDef bd = new BodyDef();
        bd.position.set(x_axis, y_axis);
        bd.type = BodyType.DYNAMIC;
        bd.userData = this.getClass();

        /**
         * Body fixtures of the TIE
         */
        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 1.5f;
        fd.friction = 8f;
        fd.restitution = 0.5f;
        fd.filter.categoryBits = this.category;
        fd.filter.maskBits = this.maskBit;

        fd.userData = new EnemyBehaviour(this, Color.BLUE);


/**************  LEFT WING  *********************/

        ChainShape chs = new ChainShape();

        chs.setRadius(3);

        /**
         * Number of vertices.
         */
        Vec2[] vertices = new Vec2[3];
        vertices[0] = new Vec2(-5, +10);
        vertices[1] = new Vec2(-15, 0);

        vertices[2] = new Vec2(-5, -10);

        this.shoot1 = vertices[1];

        chs.createChain(vertices, 3);


        FixtureDef fs = new FixtureDef();
        fs.shape = chs;

        fs.isSensor = false;
        fs.density = 0.0f;
        fs.friction = 0.3f;
        fs.restitution = 0.5f;

        fs.filter.categoryBits = MasterPilotWorld.ENEMY;
        fs.filter.maskBits = MasterPilotWorld.HERO | MasterPilotWorld.SHIELD;


        fs.userData = new EnemyBehaviour(this, Color.BLUE);

        /**** RIGHT  WING **********************************/

        ChainShape chr = new ChainShape();

        chs.setRadius(3);

        /**
         * Number of vertices.
         */
        Vec2[] rVertices = new Vec2[3];
        rVertices[0] = new Vec2(5, +10);
        rVertices[1] = new Vec2(15, 0);

        rVertices[2] = new Vec2(5, -10);

        this.shoot2 = rVertices[1];

        chr.createChain(rVertices, 3);


        //ps.set(vertices[1], vertices[2]);


        FixtureDef fr = new FixtureDef();
        fr.shape = chr;

        fr.isSensor = false;
        fr.density = 0.0f;
        fr.friction = 0.3f;
        fr.restitution = 0.5f;

        fr.filter.categoryBits = MasterPilotWorld.ENEMY;
        fr.filter.maskBits = MasterPilotWorld.HERO | MasterPilotWorld.SHIELD;

        fr.userData = new EnemyBehaviour(this, Color.BLUE);


/*****************************************************/
        bd.angularDamping = 2.0f;
        bd.linearDamping = 0.0999f;
        Body body = this.world.createBody(bd);
        body.createFixture(fd);
        body.createFixture(fs);
        body.createFixture(fr);


        this.body = body;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    fire = true;
                    try {
                        Thread.sleep(800);
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
        float x_distance = body.getPosition().x - hero.getBody().getPosition().x;
        float y_distance = body.getPosition().y - hero.getBody().getPosition().y;
        int limit = 100;

//        if(x_distance > 800 || y_distance > 800){
//            this.body.setTransform(new Vec2(this.hero.getBody().getPosition().x +200,
//                    this.hero.getBody().getPosition().y+300),
//                    this.getBody().getAngle());
//        }

//        if (x_distance > -30 && x_distance < 30 && fire == true) {
//
//            fire();
//            fire = false;
//        }

        if ((x_distance >=-2*limit && x_distance <=2*limit) && (y_distance >=-2*limit && y_distance <=2*limit)
                && fire == true) {

            fire();
            fire = false;
        }

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

        /**
         * I get the actual tip coordinate in the world
         */
        Vec2 worldPoint1 = body.getWorldPoint(shoot1);
        Vec2 worldPoint2 = body.getWorldPoint(shoot2);

        /**
         * create the shoot
         */
        int maskBit = MasterPilotWorld.SHIELD | MasterPilotWorld.PLANET | MasterPilotWorld.HERO;
        int category = MasterPilotWorld.ENEMY;
        RayFire rayon1 = new RayFire(this.world, worldPoint1.x, worldPoint1.y, category, maskBit, Color.ORANGE);
        rayon1.create();
        RayFire rayon2 = new RayFire(this.world, worldPoint2.x, worldPoint2.y, category, maskBit, Color.ORANGE);
        rayon2.create();

        /**
         * Apply force to the specific point
         */

//        float y_distance = body.getPosition().y - hero.getBody().getPosition().y;
//        Vec2 force;
//        if (y_distance > 0) {
//            force = body.getWorldVector(fireUp);
//        } else {
//            force = body.getWorldVector(fireDown);
//            ;
//        }
//
//
//        Vec2 point1 = body.getWorldPoint(rayon1.getBody().getWorldCenter());
//        Vec2 point2 = body.getWorldPoint(rayon2.getBody().getWorldCenter());

        /**
         * need to do transform to position the shoot
         * in good direction
         */
        rayon1.getBody().setTransform(worldPoint1, hero.getBody().getAngle());

       // rayon1.getBody().applyForce(force, point1);
        rayon2.getBody().setTransform(worldPoint2, hero.getBody().getAngle());

       // rayon2.getBody().applyForce(force, point2);



        Vec2 worldCenter = rayon1.getBody().getWorldCenter();
        Vec2 worldCenter2 = rayon2.getBody().getWorldCenter();
        Vec2 blastDir = worldCenter.sub(hero.getBody().getPosition());

        rayon1.getBody().applyLinearImpulse(blastDir.mul(-10000), worldCenter);
        rayon2.getBody().applyLinearImpulse(blastDir.mul(-10000), worldCenter2);



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
