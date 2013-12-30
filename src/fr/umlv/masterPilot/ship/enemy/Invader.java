package fr.umlv.masterPilot.ship.enemy;

import fr.umlv.masterPilot.ship.RayFire;
import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.ship.hero.Hero;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import java.awt.*;


/**
 * his a ennemy which look like a cardinal cross
 * have some complex behavior
 */
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
    private Vec2 shootDown;
    private Vec2 shootLeft;
    private Vec2 shootRight;
    private Body body;
    private volatile boolean fire;

    /**
     *
     * @param world
     * @param x_axis : X coordinate of his initial position in yhe world
     * @param y_axis : Y cordinate of his initial position in yhe world
     * @param hero : the hero
     */
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
        this.forceUp = new Vec2(0, +70000f);
        this.forceLeft = new Vec2(-1000f, 0);
        this.forceRight = new Vec2(+1000f, 0);
        this.forceDown = new Vec2(0, -70000f);

    }

    @Override
    public void create() {

        PolygonShape ps = new PolygonShape();

        /**
         * Number of vertices.
         */
        Vec2[] vertices = new Vec2[6];
        vertices[0] = new Vec2(-5f, -10f);
        vertices[1] = new Vec2(-0, -20f);
        vertices[2] = new Vec2(+5f, -10f);
        vertices[3] = new Vec2(+5f, +10f);
        vertices[4] = new Vec2(+0, +20f);
        vertices[5] = new Vec2(-5f, +10f);

        this.shootUp = vertices[4];
        this.shootDown = vertices[1];
        ps.set(vertices,6);

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
        body = this.world.createBody(bd);

        /**
         * Body fixtures of the Invader
         */
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 1.5f;
        fd.friction = 8f;
        fd.restitution = 2.f;
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
        leftWingVertices[1] = new Vec2(-15, +5);
        leftWingVertices[2] = new Vec2(-20, +0);
        leftWingVertices[3] = new Vec2(-15, -5);
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
        fdlw.restitution = 2f;
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
        rightWingVertices[1] = new Vec2(+15, +5);
        rightWingVertices[2] = new Vec2(+20, +0);
        rightWingVertices[3] = new Vec2(+15, -5);
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
        fdrw.restitution = 2.f;
        fdrw.filter.categoryBits = this.category;
        fdrw.filter.maskBits = this.maskBit;
        fdrw.userData = new EnemyBehaviour(this, Color.green);
        body.createFixture(fdrw);

        /************* CREATE SHIELD ****************/
        
        CircleShape shield = new CircleShape();
        shield.setRadius(25);
        FixtureDef fshield = new FixtureDef();
        fshield.shape = shield;

        fshield.isSensor = true;
        fshield.density = 0.0f;
        fshield.friction = 0.3f;
        fshield.restitution = 0.5f;

        fshield.filter.categoryBits = MasterPilotWorld.ENEMY;
        fshield.filter.maskBits = MasterPilotWorld.SHOOT | MasterPilotWorld.PLANET
                | MasterPilotWorld.SHIELD | MasterPilotWorld.HERO;
        fshield.userData = new EnemyShieldBehaviour(5);
        body.createFixture(fshield);
        
        /********************************************************/
        
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
    
    public void moveHorizontal() {
        float x_distanceABS = Math.abs(body.getPosition().x - hero.getBody().getPosition().x);
        int limit = 200;
        
        /**
         * Horizontal movement
         */
        if (body.getPosition().x >= 0 && hero.getBody().getPosition().x >= 0) {
            if (body.getPosition().x > hero.getBody().getPosition().x && x_distanceABS > limit) {
                left();
            } else if(hero.getBody().getPosition().x > body.getPosition().x  && x_distanceABS > limit){
                right();
            }
        } else if (body.getPosition().x >= 0 && hero.getBody().getPosition().x < 0) {
            if (x_distanceABS > limit) {
                  left();
            }
        } else if (body.getPosition().x < 0 && hero.getBody().getPosition().x >= 0) {
            if (x_distanceABS > limit) {
                right();
          }
        } else if (body.getPosition().x < 0 && hero.getBody().getPosition().x < 0) {
            if (body.getPosition().x > hero.getBody().getPosition().x && x_distanceABS > limit) {
                left();
            }else if(hero.getBody().getPosition().x > body.getPosition().x && x_distanceABS > limit){
                right();
            }
        }
    }

    @Override
    public void doMove() {
        double distance = Math.sqrt(Math.pow(body.getPosition().x - hero.getBody().getPosition().x, 2)
                + Math.pow(body.getPosition().y - hero.getBody().getPosition().y, 2));
        float y_distanceABS = Math.abs(body.getPosition().y - hero.getBody().getPosition().y);
        int limit = 200;

        /**
         * Vertical movement
         */
        if (body.getPosition().y >= 0 && hero.getBody().getPosition().y >= 0) {
            if (body.getPosition().y > hero.getBody().getPosition().y && y_distanceABS > limit) {
                down();
            }else if(hero.getBody().getPosition().y > body.getPosition().y && y_distanceABS > limit){
                up();
            }else if (y_distanceABS <= limit) {
                moveHorizontal();
            }    
        } else if (body.getPosition().y >= 0 && hero.getBody().getPosition().y < 0) {
            if (y_distanceABS > limit) {
                down();
            } else if (y_distanceABS <= limit) {
                moveHorizontal();
            }
        } else if (body.getPosition().y < 0 && hero.getBody().getPosition().y >= 0) {
            if (y_distanceABS > limit) {
                up();
            } else if (y_distanceABS <= limit) {
                moveHorizontal();
            }
        } else if (body.getPosition().y < 0 && hero.getBody().getPosition().y < 0) {
            if (body.getPosition().y > hero.getBody().getPosition().y && y_distanceABS > limit) {
                down();
            } else if (hero.getBody().getPosition().y > body.getPosition().y && y_distanceABS > limit){
                up();
            } else if (y_distanceABS <= limit) {
                moveHorizontal();
            }
        }  
        
        /**
         * Shoot or not
         */
        if (distance <= 300  && fire == true) {
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
        rayonDown.getBody().setTransform(worldPointDown, hero.getBody().getAngle());
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
        Vec2 pointDown = rayonDown.getBody().getWorldCenter();
        Vec2 blastDirDown = pointDown.sub(hero.getBody().getPosition());
        rayonDown.getBody().applyLinearImpulse(blastDirDown.mul(-10000), pointDown);

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
        throw new UnsupportedOperationException();
    }

    @Override
    public void shield() {
        throw new UnsupportedOperationException();
    }

    public Body getBody() {
        return body;
    }

}
