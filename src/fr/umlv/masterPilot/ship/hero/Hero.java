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
 * This class create his body and attach it in the world
 * <p>
 * created by Emmanuel Babala Costa
 */
public class Hero implements KeyMotionObserver, SpaceShip {

    private final int x_axis;
    private final int y_axis;
    private final Color color;
    private final World world;
    private final Vec2 heroSpeed = new Vec2(0, -700f);
    private final Vec2 classicBombSpeed = new Vec2(0, -3000.0f);


    private Body body;
    private Bomb.BombType bombType = Bomb.BombType.NONE;
    private Bomb cBomb;

    /**
     * create the hero at the specify coordinate
     *
     * @param world
     * @param x_axis
     * @param y_axis
     */
    public Hero(World world, int x_axis, int y_axis) {
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.world = world;
        this.color = Color.lightGray;
    }

    /**
     * create the hero at the specify coordinate with the specify color
     *
     * @param world
     * @param x_axis
     * @param y_axis
     * @param color
     */
    public Hero(World world, int x_axis, int y_axis, Color color) {
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.world = world;
        this.color = color;
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

        fd.userData = new HeroBehaviuor();
        // fd garder une reference sur la classe
        //fd.isSensor=true;
        // si un contact verifier si il est du type shield
        //si c'est le cas dessiner

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.angularDamping = 2.0f;
        bd.linearDamping = 0.1f;

        bd.userData = this.getClass();


        bd.position.set(x_axis, y_axis);
        bd.angle = 3.14f;
        bd.allowSleep = false;
/**************************SHIELD ************************************************************************/
//Create chield protection
        CircleShape cs = new CircleShape();
        cs.setRadius(23);
        FixtureDef fs = new FixtureDef();
        fs.shape = cs;

        fs.isSensor = true;
        fs.density = 0.0f;
        fs.friction = 0.3f;
        fs.restitution = 0.5f;

        fs.filter.categoryBits = MasterPilotWorld.SHIELD;


        fs.userData = new HeroShieldBehaviour();
/*************************************************************************************************************/
        Body body = this.world.createBody(bd);

        body.createFixture(fs);
        body.createFixture(fd);


        this.body = body;
//		body.createFixture(sd2);


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

//                    this.bombType = Bomb.BombType.NONE;
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

        ;
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
        // this.body.applyTorque(200f);

        //
        // this.body.setTransform(body.getPosition(), this.body.getAngle() + 0.05f);

    }

    @Override
    public void up() {

        Vec2 force = body.getWorldVector(heroSpeed);
        Vec2 point = body.getWorldPoint(body.getLocalCenter().add(
                new Vec2(0.0f, 100.0f)));

        this.body.applyForce(force, point);
//MAKE TRAIL
        makeTrail();
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


//LEFT
        Trail cBomb = new Trail(this.world, worldPoint.x, worldPoint.y,
                MasterPilotWorld.TRAIL, MasterPilotWorld.PLANET
                | MasterPilotWorld.ENEMY
                | MasterPilotWorld.RADAR, Color.CYAN, 2);
        cBomb.create();

//RIGHT
        cBomb = new Trail(this.world, worldPoint2.x, worldPoint2.y,
                MasterPilotWorld.TRAIL, MasterPilotWorld.PLANET
                | MasterPilotWorld.ENEMY
                | MasterPilotWorld.RADAR, Color.CYAN, 2);
        cBomb.create();

//MIDDLE
        cBomb = new Trail(this.world, worldPoint3.x, worldPoint3.y,
                MasterPilotWorld.TRAIL, MasterPilotWorld.PLANET
                | MasterPilotWorld.ENEMY
                | MasterPilotWorld.RADAR, Color.CYAN, 2);
        cBomb.create();

        TrailManager.addTrail(new Vec2().set(worldPoint3), cBomb);




    }

    @Override
    public void down() {
        throw new UnsupportedOperationException();
    }

    public void setBomb(Bomb bomb) {
        if (!Objects.isNull(bomb) && bomb.getBombeState() != Bomb.BombState.ARMED) {
            this.bombType = bomb.getBombType();
            this.cBomb = bomb;
        }
    }
}
