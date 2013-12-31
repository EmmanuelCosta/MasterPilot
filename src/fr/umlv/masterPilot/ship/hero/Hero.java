package fr.umlv.masterPilot.ship.hero;

import fr.umlv.masterPilot.bomb.Bomb;
import fr.umlv.masterPilot.ship.RayFire;
import fr.umlv.masterPilot.ship.RayFireManager;
import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.world.KeyMotionObserver;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import fr.umlv.zen3.KeyboardEvent;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.awt.*;
import java.util.Objects;


/**
 * Hero is the Main character of the Game
 * This class create his body and attach it in the jbox 2d world
 * <p>
 * created by Emmanuel Babala Costa
 */
public class Hero implements KeyMotionObserver, SpaceShip {
    private final int x_axis;
    private final int y_axis;
    private final World world;
    private final Vec2 heroSpeed = new Vec2(0, -700f);
    private final Vec2 classicBombSpeed = new Vec2(0, -3000.0f);
    private final MasterPilotWorld.MODE mode;
    private Body body;
    private Bomb.BombType bombType = Bomb.BombType.NONE;
    private Bomb cBomb;
    private final Vec2 maxSpeed = new Vec2(5, 5);
    private final HeroBehavior heroBehavior;

    /**
     * create the hero at the specify coordinate
     *
     * @param world
     * @param x_axis
     * @param y_axis
     */
    public Hero(World world, int x_axis, int y_axis, MasterPilotWorld.MODE mode) {
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.world = world;
        this.mode = mode;
        this.heroBehavior = new HeroBehavior(mode);
    }

    /**
     * create the hero at the specify coordinate with the specify color
     *
     * @param world
     * @param x_axis
     * @param y_axis
     * @param color
     */
    public Hero(World world, int x_axis, int y_axis, Color color, MasterPilotWorld.MODE mode) {
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.world = world;
        this.mode = mode;
        this.heroBehavior = new HeroBehavior(mode);
    }

    /**
     * we create the hero body and we add it to the world
     * we add also the shield here
     * which will be a circle arrounding the hero body
     */
    public void create() {
        Vec2 vertices[] = new Vec2[4];
        vertices[0] = new Vec2(9.5f, 0.0f);
        vertices[1] = new Vec2(0.0f, -15.0f);
        vertices[2] = new Vec2(-9.5f, 0.0f);
        vertices[3] = new Vec2(0.0f, 8f);

        PolygonShape poly1 = new PolygonShape();
        poly1.set(vertices, 4);

        FixtureDef fd = new FixtureDef();
        fd.shape = poly1;
        fd.filter.categoryBits = MasterPilotWorld.HERO;
        fd.filter.maskBits = MasterPilotWorld.ENEMY | MasterPilotWorld.PLANET | MasterPilotWorld.MEGABOMB | MasterPilotWorld.BOMB | MasterPilotWorld.TRAIL;
        fd.density = 0.09f;
        fd.friction = 0.001f;
        fd.restitution = 1.5f;
        fd.userData = this.heroBehavior;

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.angularDamping = 2.0f;
        bd.linearDamping = 0.1f;
        bd.userData = this.getClass();
        bd.position.set(x_axis, y_axis);
        bd.angle = 3.14f;
        bd.allowSleep = false;

/**************************SHIELD ************************************************************************/

        CircleShape cs = new CircleShape();
        cs.setRadius(23);

        FixtureDef fs = new FixtureDef();
        fs.shape = cs;
        fs.isSensor = true;
        fs.density = 0.0f;
        fs.friction = 0.3f;
        fs.restitution = 0.5f;
        fs.filter.categoryBits = MasterPilotWorld.SHIELD;
        fs.userData = new HeroShieldBehavior(mode);

        Body body = this.world.createBody(bd);
        body.createFixture(fs);
        body.createFixture(fd);
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public void doMove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onKeyPressed(KeyboardEvent keyEvent) {
        switch (keyEvent.toString().substring(3)) {
            case "UP":
                up();
                break;

            case "LEFT":
                left();
                break;

            case "RIGHT":
                right();
                break;

            case "SPACE":

                /**
                 * fire only if we are not in shield mode
                 */
                if (!isShieldSet()) {
                    fire();
                }
                break;

            case "B":
                if (!isShieldSet() && this.bombType != Bomb.BombType.NONE) {
                    fireBomb();
                }
                break;

            case "S":
                shield();
                break;

            default:
                break;
        }
    }

    /**
     * test if shield is set
     * return  true
     *
     * @return
     */
    private boolean isShieldSet() {
        Fixture m_next = this.body.getFixtureList().m_next;
        return !m_next.m_isSensor;
    }

    @Override
    public void fire() {
        /**
         * I try to calculate the tip coordinate
         * and create a Bomb from that point
         */
        PolygonShape sha = (PolygonShape) body.getFixtureList().getShape();

        /**
         * Here the tip is store on third vertices
         * careful if body construction change
         * so you must recalculate the tip too
         */
        Vec2[] vertices = sha.getVertices();

        /**
         * I get the actual tip coordinate in the world
         */
        Vec2 worldPoint = body.getWorldPoint(vertices[3]);

        /**
         * create the shoot
         */
        RayFire cBomb = new RayFire(this.world, worldPoint.x, worldPoint.y);
        cBomb.create();

        Vec2 force = body.getWorldVector(classicBombSpeed);
        Vec2 point = body.getWorldPoint(cBomb.getBody().getWorldCenter());

        /**
         * need to do transform to position the shoot
         * in good direction
         */
        cBomb.getBody().setTransform(worldPoint, body.getAngle());
        cBomb.getBody().applyForce(force, point);
        RayFireManager.addRayFire(new Vec2().set(body.getPosition()), cBomb);
    }

    @Override
    public void fireBomb() {
        launchBomb();
    }

    private void launchBomb() {
        /**
         * I try to calculate the tip coordinate
         * and create a Bomb from that point
         */
        PolygonShape sha = (PolygonShape) body.getFixtureList().getShape();

        /**
         * Here the tip is store on third vertices
         * careful if body construction change
         * so you must recalculate the tip too
         */
        Vec2[] vertices = sha.getVertices();

        /**
         * I get the actual tip coordinate in the world
         */
        Vec2 worldPoint = body.getWorldPoint(vertices[3]);

        /**
         * create the shoot
         */
        Vec2 force = body.getWorldVector(classicBombSpeed.mul(10000));
        Vec2 point = body.getWorldPoint(this.cBomb.getBody().getWorldCenter());

        /**
         * need to do transform to position the shoot
         * in good direction
         */
        this.cBomb.setBombState(Bomb.BombState.ARMED);
        this.cBomb.getBody().setTransform(worldPoint, body.getAngle());
        this.cBomb.getBody().applyLinearImpulse(force, point);
        this.bombType = Bomb.BombType.NONE;
    }

    /**
     * call this to set or unset the shield
     * dependant to his actual state
     */
    @Override
    public void shield() {
        Fixture m_next = this.body.getFixtureList().m_next;
        m_next.m_isSensor = !m_next.m_isSensor;
    }

    @Override
    public void right() {
        this.body.applyTorque(-1000f);
    }

    @Override
    public void left() {
        this.body.applyTorque(1000f);
    }

    @Override
    public void up() {
        Vec2 force = body.getWorldVector(heroSpeed);
        Vec2 point = body.getWorldPoint(body.getLocalCenter().add(
                new Vec2(0.0f, 100.0f)));
        Vec2 lVelocity = this.body.getLinearVelocity();

        if (Math.abs(lVelocity.y) < maxSpeed.y) {
            this.body.applyForce(force, point);
        }
        makeTrail();
    }

    @Override
    public void down() {
        throw new UnsupportedOperationException();
    }

    /**
     * use this to make trail beside the hero spaceship
     */
    private void makeTrail() {

        /**
         * I  calculate the tip coordinate of the back of my spaceship
         * and create a trail from that point
         */
        PolygonShape sha = (PolygonShape) body.getFixtureList().getShape();

        /**
         *
         * careful if body construction change
         * so you must recalculate the tip too
         */
        Vec2[] vertices = sha.getVertices();


        /**
         * I get the actual tip coordinate in the world
         */
        Vec2 worldPoint = body.getWorldPoint(vertices[0]);
        Vec2 worldPoint2 = body.getWorldPoint(vertices[2]);
        Vec2 worldPoint3 = body.getWorldPoint(vertices[1]);

        /**
         * create the trail
         */
        Trail trail1 = new Trail(this.world, worldPoint.x, worldPoint.y,
                MasterPilotWorld.TRAIL, MasterPilotWorld.PLANET
                | MasterPilotWorld.ENEMY
                | MasterPilotWorld.RADAR
                , Color.CYAN, 2);
        trail1.create();

        Trail trail2 = new Trail(this.world, worldPoint2.x, worldPoint2.y,
                MasterPilotWorld.TRAIL, MasterPilotWorld.PLANET
                | MasterPilotWorld.ENEMY
                | MasterPilotWorld.RADAR
                , Color.CYAN, 2);
        trail2.create();

        Trail trail3 = new Trail(this.world, worldPoint3.x, worldPoint3.y,
                MasterPilotWorld.TRAIL, MasterPilotWorld.PLANET
                | MasterPilotWorld.ENEMY
                | MasterPilotWorld.RADAR
                , Color.CYAN, 2);
        trail3.create();

        TrailManager.addTrail(this.getBody(), trail1);
        TrailManager.addTrail(this.getBody(), trail2);
        TrailManager.addTrail(this.getBody(), trail3);
    }

    /**
     * set a bomb in the hero item
     *
     * @param bomb
     */
    public void setBomb(Bomb bomb) {
        if (!Objects.isNull(bomb) && bomb.getBombeState() != Bomb.BombState.ARMED) {
            this.bombType = bomb.getBombType();
            this.cBomb = bomb;
        }
    }

    public Vec2 getMaxSpeed() {
        return maxSpeed;
    }

    public int getHeroLive() {
        /**
         * cheat mode infinite live
         */
        if(this.mode == MasterPilotWorld.MODE.CHEAT){
            return 1000;
        }
        /**
         * return the left live
         */
        return this.heroBehavior.getHeroLive();
    }
}
