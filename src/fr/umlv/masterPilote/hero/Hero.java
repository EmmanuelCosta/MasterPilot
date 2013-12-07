package fr.umlv.masterPilote.hero;

import fr.umlv.masterPilote.Interface.Bomb;
import fr.umlv.masterPilote.Interface.SpaceShip;
import fr.umlv.masterPilote.Interface.fr.umlv.masterPilote.Interface.keyMotion.KeyMotionObserver;
import fr.umlv.masterPilote.MasterPilote;
import fr.umlv.zen3.KeyboardEvent;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

public class Hero implements KeyMotionObserver, SpaceShip {

    private final int x_axis;
    private final int y_axis;
    private World world;
    private Body body;

    public Hero(World world, int x_axis, int y_axis) {
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.world = world;
    }

    public void create() {

//        Transform xf1 = new Transform();
//        xf1.q.set(0.3524f * MathUtils.PI);
//        Rot.mulToOutUnsafe(xf1.q, new Vec2(1.0f, 0.0f), xf1.p);
//
//        System.out.println(xf1.p + " " + xf1.q);
        Vec2 vertices[] = new Vec2[4];
//        vertices[0] = Transform.mul(xf1, new Vec2(15.0f, 0.0f));
//        vertices[1] = Transform.mul(xf1, new Vec2(0.0f, -40.0f));
//        vertices[2] = Transform.mul(xf1, new Vec2(-15f, 0.0f));
//        System.out.println(vertices[0] + " " + vertices[1] + " " + vertices[2]);
        vertices[0] = new Vec2(15.0f, 0.0f);
        vertices[1] = new Vec2(0.0f, -40.0f);
        vertices[2] = new Vec2(-15.0f, 0.0f);
        vertices[3] = new Vec2(0.0f, 20f);


        System.out.println(vertices[0] + " " + vertices[1] + " " + vertices[2]);

        PolygonShape poly1 = new PolygonShape();
        poly1.set(vertices, 4);

        FixtureDef sd1 = new FixtureDef();
        sd1.shape = poly1;
        sd1.filter.categoryBits = MasterPilote.HERO;
        sd1.filter.maskBits = MasterPilote.ENEMY | MasterPilote.PLANET;
        sd1.density = 0.0f;
        sd1.friction = 0.1f;
        sd1.restitution = 0.5f;

//		Transform xf2 = new Transform();
//		xf2.q.set(-0.3524f * MathUtils.PI);
//		Rot.mulToOut(xf2.q, new Vec2(-1.0f, 0.0f), xf2.p);

//		vertices[0] = Transform.mul(xf2, new Vec2(-1.0f, 0.0f));
//		vertices[1] = Transform.mul(xf2, new Vec2(10.0f, 0.0f));
//		vertices[2] = Transform.mul(xf2, new Vec2(0.0f, 0.5f));

//        vertices[0] =  new Vec2(10.0f, 0.0f);
//        vertices[1] =  new Vec2(0.0f, 0.0f);
//        vertices[2] = new Vec2(0.0f, 10f);
//		System.out.println("vertces = " + vertices[0] + "  " + vertices[1]
//				+ " " + vertices[2]);
//		PolygonShape poly2 = new PolygonShape();
//		poly2.set(vertices, 3);

//		FixtureDef sd2 = new FixtureDef();
//		sd2.shape = poly2;
//		sd2.density = 2.0f;

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.angularDamping = 5.0f;
        bd.linearDamping = 0.1f;


        bd.position.set(x_axis, y_axis);
        bd.angle = MathUtils.PI;
        bd.allowSleep = false;
        Body body = this.world.createBody(bd);
        body.createFixture(sd1);
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

    }

    @Override
    public void fireBomb(Bomb bomb) {

    }

    @Override
    public void shield() {

    }

    @Override
    public void right() {

    }

    @Override
    public void left() {
        System.out.println("left");

        this.body.applyTorque(500f);

    }

    @Override
    public void up() {
        Vec2 force = body.getWorldVector(new Vec2(0, -150.0f));
        Vec2 point = body.getWorldPoint(body.getWorldCenter());
        this.body.applyForce(force, point);
//        this.body.applyLinearImpulse(force,point);
    }

    @Override
    public void down() {
        throw new UnsupportedOperationException();
    }
}
