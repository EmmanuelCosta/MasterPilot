package fr.umlv.masterPilote.hero;

import fr.umlv.masterPilote.Interface.Bomb;
import fr.umlv.masterPilote.Interface.SpaceShip;
import fr.umlv.masterPilote.Interface.fr.umlv.masterPilote.Interface.keyMotion.KeyMotionObserver;
import fr.umlv.masterPilote.world.MasterPilote;
import fr.umlv.masterPilote.bomb.RayBomb;
import fr.umlv.zen3.KeyboardEvent;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.awt.*;

public class Hero implements KeyMotionObserver, SpaceShip {

    private final int x_axis;
    private final int y_axis;
    private final Color color;
    private World world;
    private Body body;
    private Vec2 heroSpeed = new Vec2(0, -30f);
    private Vec2 classicBombSpeed = new Vec2(0, -3000.0f);

    public Hero(World world, int x_axis, int y_axis) {
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.world = world;
        this.color = Color.lightGray;
    }

    public Hero(World world, int x_axis, int y_axis,Color color) {
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.world = world;
        this.color = color;
    }

    public void create() {

        Vec2 vertices[] = new Vec2[4];

        vertices[0] = new Vec2(15.0f, 0.0f);
        vertices[1] = new Vec2(0.0f, -40.0f);
        vertices[2] = new Vec2(-15.0f, 0.0f);
        vertices[3] = new Vec2(0.0f, 20f);



        PolygonShape poly1 = new PolygonShape();
        poly1.set(vertices, 4);

        FixtureDef fd = new FixtureDef();
        fd.shape = poly1;
        fd.filter.categoryBits = MasterPilote.HERO;
        fd.filter.maskBits = MasterPilote.ENEMY | MasterPilote.PLANET;
        fd.density = 0.0f;
        fd.friction = 0.01f;
        fd.restitution = 0.5f;

        fd.userData = color;


        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.angularDamping = 0.0f;
        bd.linearDamping = 0.01f;

        bd.userData = this;


        bd.position.set(x_axis, y_axis);
        bd.angle = 3.14f;
        bd.allowSleep = false;
        Body body = this.world.createBody(bd);
        body.createFixture(fd);

        this.body = body;
//		body.createFixture(sd2);


    }

    public Body getBody() {
        return body;
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
                fire();
                break;
            case "b":
//                fireBomb();
                break;
            case "s":
                break;

            default:
                break;

        }
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

      ;
        /**
         * create the shoot
         */

        RayBomb cBomb = new RayBomb(this.world, worldPoint.x, worldPoint.y);


        cBomb.create();

        Vec2 force = body.getWorldVector(classicBombSpeed);
        Vec2 point = body.getWorldPoint(cBomb.getBody().getWorldCenter());

        /**
         * need to do transform to position the shoot
         * in good direction
         */


        cBomb.getBody().setTransform(worldPoint, body.getAngle());


        cBomb.getBody().applyLinearImpulse(force, point);

    }

    @Override
    public void fireBomb(Bomb bomb) {

    }

    @Override
    public void shield() {

    }

    @Override
    public void right() {
        // this.body.applyTorque(500f);


        // this.body.applyAngularImpulse(500);

//TODO ROTATION WITH TORQUES INSTEAD
        this.body.setTransform(body.getPosition(), this.body.getAngle() - 0.05f);

    }

    @Override
    public void left() {

//        this.body.applyTorque(-50000f);


        this.body.setTransform(body.getPosition(), this.body.getAngle() + 0.05f);

    }

    @Override
    public void up() {
//TODO MANAGE ACCELERATION
        Vec2 force = body.getWorldVector(heroSpeed);
        Vec2 point = body.getWorldPoint(body.getWorldCenter());
//        this.body.applyForce(force, point);
this.body.applyLinearImpulse(force,point);

//        this.body.m_linearVelocity

//        Vec2 linearVelocity = this.body.getLinearVelocity();
//        System.out.println(linearVelocity + " 1");
//        Vec2 v = new Vec2(linearVelocity.x * 200, linearVelocity.y * 200);
//        this.body.setLinearVelocity(v);
//        System.out.println(linearVelocity + " 2");
    }

    @Override
    public void down() {
        throw new UnsupportedOperationException();
    }
    
    public int getX() {
        return this.x_axis;
    }
    
    public int getY() {
        return this.y_axis;
    }
}
