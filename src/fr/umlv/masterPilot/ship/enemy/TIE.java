package fr.umlv.masterPilot.ship.enemy;

import fr.umlv.masterPilot.ship.RayFire;
import fr.umlv.masterPilot.ship.RayFireManager;
import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.ship.hero.Hero;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.awt.*;

/**
 * that ennemy look like a small circle with to wings
 * Created by emmanuel on 22/12/13.
 */
public class TIE implements SpaceShip {

    private  final Vec2 maxSpeed;
    private final int maskBit;
    private final int category;

    private final int x_axis;
    private final int y_axis;
    private final Hero hero;
    private final Vec2 forceLeft = new Vec2(-150f, -5f);
    private final Vec2 forceRight = new Vec2(+150f, -5f);
    private final Vec2 forceUp = new Vec2(-5f, +150f);
    private final Vec2 forceDown = new Vec2(-5f, -150f);
    private final World world;

    private Body body;
    private Vec2 shoot1;
    private Vec2 shoot2;
    private volatile boolean fire;
    private Thread thread;

    /**
     *
     * @param world
     * @param x_axis : X coordinate of his initial position in yhe world
     * @param y_axis : Y cordinate of his initial position in yhe world
     * @param hero : the hero
     */
    public TIE(World world, int x_axis, int y_axis, Hero hero) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.hero = hero;
        this.maxSpeed =hero.getMaxSpeed().sub(new Vec2(2,2));

        /**
         *  Interactions with the other bodies.
         */
        this.category = MasterPilotWorld.ENEMY;
        this.maskBit = MasterPilotWorld.SHIELD
                | MasterPilotWorld.SHOOT
                | MasterPilotWorld.BOMB
                | MasterPilotWorld.MEGABOMB
                | MasterPilotWorld.HERO
                | MasterPilotWorld.PLANET
        ;
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
        bd.angularDamping = 2.0f;
        bd.linearDamping = 0.0999f;

        /**
         * Body fixtures of the TIE
         */
        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 1.5f;
        fd.friction = 8f;
        fd.restitution = 10.5f;
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





        FixtureDef fr = new FixtureDef();
        fr.shape = chr;

        fr.isSensor = false;
        fr.density = 0.0f;
        fr.friction = 0.3f;
        fr.restitution = 1.5f;

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

        this.thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    fire = true;

                    try {
                        Thread.sleep(800);
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
        Vec2 force = body.getWorldVector(forceRight);
        this.body.setTransform(body.getPosition(), this.body.getAngle());

        Vec2 lVelocity = this.body.getLinearVelocity();

        if(  Math.abs(lVelocity.y) < maxSpeed.y){
            this.body.applyForceToCenter(force);
        }


    }

    @Override
    public void left() {
        Vec2 force = body.getWorldVector(forceLeft);

        this.body.setTransform(body.getPosition(), this.body.getAngle());
        Vec2 lVelocity = this.body.getLinearVelocity();

        if(  Math.abs(lVelocity.y) < maxSpeed.y){
            this.body.applyForceToCenter(force);
        }


    }

    @Override
    public void up() {
        Vec2 force = body.getWorldVector(forceUp);
        this.body.setTransform(body.getPosition(), this.body.getAngle());

        Vec2 lVelocity = this.body.getLinearVelocity();

        if(  Math.abs(lVelocity.y) < maxSpeed.y){
            this.body.applyForceToCenter(force);
        }

    }

    @Override
    public void down() {
        Vec2 force = body.getWorldVector(forceDown);
        this.body.setTransform(body.getPosition(), this.body.getAngle());

        Vec2 lVelocity = this.body.getLinearVelocity();

        if(  Math.abs(lVelocity.y) < maxSpeed.y){
            this.body.applyForceToCenter(force);
        }

    }

    @Override
    public void doMove() {
        float x_distance = body.getPosition().x - hero.getBody().getPosition().x;
        float y_distance = body.getPosition().y - hero.getBody().getPosition().y;
        int limit = 100;
        if(Math.abs(x_distance) > 1000 || Math.abs(y_distance)> 1000){
                adjustPath();
        }


        if ((x_distance >= -50 - limit && x_distance <= 50 + limit) && (y_distance >= -50 - limit && y_distance <= 50 + limit)
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

    /**
     * this is use to reduce distance between ennemy and hero
     * otherwise it can be difficult to catch it again
     */
private void adjustPath(){
    /**
     * I get the actual tip coordinate in the world
     */


    Vec2 position = hero.getBody().getPosition();
    this.body.setTransform(new Vec2(position.x+50,position.y+150),0);
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



        /**
         * need to do transform to position the shoot
         * in good direction
         */
        rayon1.getBody().setTransform(worldPoint1, hero.getBody().getAngle());

        rayon2.getBody().setTransform(worldPoint2, hero.getBody().getAngle());



        Vec2 worldCenter = rayon1.getBody().getWorldCenter();
        Vec2 worldCenter2 = rayon2.getBody().getWorldCenter();
        Vec2 blastDir = worldCenter.sub(hero.getBody().getPosition());

        rayon1.getBody().applyLinearImpulse(blastDir.mul(-10000), worldCenter);
        rayon2.getBody().applyLinearImpulse(blastDir.mul(-10000), worldCenter2);

        RayFireManager.addRayFire(new Vec2().set(body.getPosition()), rayon1);
        RayFireManager.addRayFire(new Vec2().set(body.getPosition()), rayon2);


    }

    @Override
    public void fireBomb() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shield() {
        throw new UnsupportedOperationException();
    }

    public Body getBody() {
        return body;
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
