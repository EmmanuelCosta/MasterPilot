package fr.umlv.masterPilot.ship.enemy;

import java.awt.Color;
import fr.umlv.masterPilot.ship.RayFireManager;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import fr.umlv.masterPilot.ship.RayFire;
import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.ship.hero.Hero;
import fr.umlv.masterPilot.world.MasterPilotWorld;

/**
 * @author sybille
 * this is an ennemy
 * it look like a ball with 4 shape attached to him
 */
public class SpaceBall implements SpaceShip {

    private int maskBit;
    private int category;
    private float x_axis;
    private float y_axis;
    private Hero hero;
    private World world;
    private Body body;
    private Vec2 shootUp;
    private Vec2 shootLeft;
    private Vec2 shootRight;
    private Vec2 shootDown;
    private Vec2 forceUp;
    private Vec2 forceLeft;
    private Vec2 forceRight;
    private Vec2 forceDown;
    private volatile boolean fire;
    /**
     *
     * @param world
     * @param x_axis : X coordinate of his initial position in yhe world
     * @param y_axis : Y cordinate of his initial position in yhe world
     * @param hero : the hero
     */
    public SpaceBall(World world, float x_axis, float y_axis, Hero hero) {
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
        /**
         * Primitive form
         */

        CircleShape cs = new CircleShape();
        cs.setRadius(10);

        /**
         * Body definition of the SpaceBall
         */
        BodyDef bd = new BodyDef();
        bd.position.set(x_axis, y_axis);
        bd.type = BodyType.DYNAMIC;
        bd.userData = this.getClass();
        bd.angularDamping = 2.0f;
        bd.linearDamping = 0.0999f;
        this.body = this.world.createBody(bd);

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
        fd.userData = new EnemyBehaviour(this, Color.CYAN);
        body.createFixture(fd);

        /************* LOSANGE UP *********************/

        PolygonShape up = new PolygonShape();
        Vec2 upVertices[] = new Vec2[4];
        upVertices[0] = new Vec2(0, +10);
        upVertices[1] = new Vec2(+5, +15);
        upVertices[2] = new Vec2(0, +20);
        upVertices[3] = new Vec2(-5, +15);

        this.shootUp = upVertices[2];
        up.set(upVertices, 4);

        FixtureDef fdup = new FixtureDef();
        fdup.shape = up;
        fdup.density = 1.5f;
        fdup.friction = 8f;
        fdup.restitution = 0.5f;
        fdup.filter.categoryBits = this.category;
        fdup.filter.maskBits = this.maskBit;
        fdup.userData = new EnemyBehaviour(this, Color.BLUE);
        body.createFixture(fdup);

        /************* LOSANGE DOWN *******************/

        PolygonShape down = new PolygonShape();
        Vec2 downVertices[] = new Vec2[4];
        downVertices[0] = new Vec2(0, -10);
        downVertices[1] = new Vec2(+5, -15);
        downVertices[2] = new Vec2(0, -20);
        downVertices[3] = new Vec2(-5, -15);

        this.shootDown = downVertices[2];
        down.set(downVertices, 4);

        FixtureDef fddown = new FixtureDef();
        fddown.shape = down;
        fddown.density = 1.5f;
        fddown.friction = 8f;
        fddown.restitution = 0.5f;
        fddown.filter.categoryBits = this.category;
        fddown.filter.maskBits = this.maskBit;
        fddown.userData = new EnemyBehaviour(this, Color.BLUE);
        body.createFixture(fddown);

        /************* LOSANGE LEFT *******************/

        PolygonShape left = new PolygonShape();
        Vec2 leftVertices[] = new Vec2[4];
        leftVertices[0] = new Vec2(-10, 0);
        leftVertices[1] = new Vec2(-15, +5);
        leftVertices[2] = new Vec2(-20, 0);
        leftVertices[3] = new Vec2(-15, -5);

        this.shootLeft = leftVertices[2];
        left.set(leftVertices, 4);

        FixtureDef fdleft = new FixtureDef();
        fdleft.shape = left;
        fdleft.density = 1.5f;
        fdleft.friction = 8f;
        fdleft.restitution = 0.5f;
        fdleft.filter.categoryBits = this.category;
        fdleft.filter.maskBits = this.maskBit;
        fdleft.userData = new EnemyBehaviour(this, Color.BLUE);
        body.createFixture(fdleft);

        /************* LOSANGE RIGHT ******************/

        PolygonShape right = new PolygonShape();
        Vec2 rightVertices[] = new Vec2[4];
        rightVertices[0] = new Vec2(+10, 0);
        rightVertices[1] = new Vec2(+15, +5);
        rightVertices[2] = new Vec2(+20, 0);
        rightVertices[3] = new Vec2(+15, -5);

        this.shootRight = rightVertices[2];
        right.set(rightVertices, 4);

        FixtureDef fdright = new FixtureDef();
        fdright.shape = right;
        fdright.density = 1.5f;
        fdright.friction = 8f;
        fdright.restitution = 0.5f;
        fdright.filter.categoryBits = this.category;
        fdright.filter.maskBits = this.maskBit;
        fdright.userData = new EnemyBehaviour(this, Color.BLUE);
        body.createFixture(fdright);

        /**
         * Make the SpaceBall fire
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
    public void fire() {
        /**
         * I get the actual tip coordinate in the world
         */
        Vec2 worldPointUp = body.getWorldPoint(shootUp);
        Vec2 worldPointDown = body.getWorldPoint(shootDown);
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

        RayFire rayonDown = new RayFire(this.world, worldPointDown.x,
                worldPointDown.y, MasterPilotWorld.ENEMY, MasterPilotWorld.HERO
                        | MasterPilotWorld.BOMB | MasterPilotWorld.MEGABOMB
                        | MasterPilotWorld.SHIELD | MasterPilotWorld.PLANET,
                Color.yellow);
        rayonDown.create();

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
        rayonDown.getBody().setTransform(worldPointDown,
                hero.getBody().getAngle());
        rayonLeft.getBody().setTransform(worldPointLeft,
                hero.getBody().getAngle());
        rayonRight.getBody().setTransform(worldPointRight,
                hero.getBody().getAngle());

        /**
         * Tansform the position of the shoot UP in the right direction
         */
        Vec2 pointUp = rayonUp.getBody().getWorldCenter();
        Vec2 blastDirUp = pointUp.sub(hero.getBody().getPosition());
        rayonUp.getBody().applyLinearImpulse(blastDirUp.mul(-10000), pointUp);

        /**
         * Tansform the position of the shoot DOWN in the right direction
         */
        Vec2 pointDown = rayonDown.getBody().getWorldCenter();
        Vec2 blastDirDown = pointDown.sub(hero.getBody().getPosition());
        rayonDown.getBody().applyLinearImpulse(blastDirDown.mul(-10000),
                pointDown);

        /**
         * Tansform the position of the shoot DOWN in the right direction
         */
        Vec2 pointLeft = rayonLeft.getBody().getWorldCenter();
        Vec2 blastDirLeft = pointLeft.sub(hero.getBody().getPosition());
        rayonLeft.getBody().applyLinearImpulse(blastDirLeft.mul(-10000),
                pointLeft);

        /**
         * Tansform the position of the shoot DOWN in the right direction
         */
        Vec2 pointRight = rayonRight.getBody().getWorldCenter();
        Vec2 blastDirRight = pointUp.sub(hero.getBody().getPosition());
        rayonRight.getBody().applyLinearImpulse(blastDirRight.mul(-10000),
                pointRight);

        RayFireManager.addRayFire(new Vec2().set(body.getPosition()), rayonRight);
        RayFireManager.addRayFire(new Vec2().set(body.getPosition()), rayonLeft);

        RayFireManager.addRayFire(new Vec2().set(body.getPosition()), rayonDown);
        RayFireManager.addRayFire(new Vec2().set(body.getPosition()), rayonUp);

    }

    @Override
    public void fireBomb() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shield() {
        // TODO Auto-generated method stub
    }

    @Override
    public Body getBody() {
        return this.body;
    }

    @Override
    public void doMove() {
        double distance = Math.sqrt(Math.pow(body.getPosition().x - hero.getBody().getPosition().x, 2)
                                  + Math.pow(body.getPosition().y - hero.getBody().getPosition().y, 2));
        int limit = 200;

        /**
         * Horizontal movement
         */
        if (body.getPosition().x >= 0 && hero.getBody().getPosition().x >= 0) {
            if (body.getPosition().x > hero.getBody().getPosition().x && distance > limit) {
                left();
            } else if(hero.getBody().getPosition().x > body.getPosition().x  && distance > limit){
                right();
            }
        } else if (body.getPosition().x >= 0 && hero.getBody().getPosition().x < 0) {
            if (distance > limit) {
                  left();
            }
        } else if (body.getPosition().x < 0 && hero.getBody().getPosition().x >= 0) {
            if (distance > limit) {
                right();
          }
        } else if (body.getPosition().x < 0 && hero.getBody().getPosition().x < 0) {
            if (body.getPosition().x > hero.getBody().getPosition().x && distance > limit) {
                left();
            }else if(hero.getBody().getPosition().x > body.getPosition().x && distance > limit){
                right();
            }
        }

        /**
         * Vertical movement
         */
        if (body.getPosition().y >= 0 && hero.getBody().getPosition().y >= 0) {
            if (body.getPosition().y > hero.getBody().getPosition().y && distance > limit) {
                down();
            }else if(hero.getBody().getPosition().y > body.getPosition().y && distance > limit){
                up();
            }
        } else if (body.getPosition().y >= 0 && hero.getBody().getPosition().y < 0) {
            if (distance > limit) {
                down();
            }
        } else if (body.getPosition().y < 0 && hero.getBody().getPosition().y >= 0) {
            if (distance > limit) {
                up();
            }
        } else if (body.getPosition().y < 0 && hero.getBody().getPosition().y < 0) {
            if (body.getPosition().y > hero.getBody().getPosition().y && distance > limit) {
                down();
            }else if(hero.getBody().getPosition().y > body.getPosition().y && distance > limit){
                up();
            }
        }

        /**
         * Shoot or not
         */
        if (distance <= limit && fire == true) {
            fire();
            fire = false;
        }
    }

}
