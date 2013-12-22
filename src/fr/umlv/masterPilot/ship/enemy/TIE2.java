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
    private World world;
    private Body body;
    private Vec2 rayonForce = new Vec2(0f, -10f);
    private Vec2 shoot1;
    private Vec2 shoot2;

    public TIE2(World world, int x_axis, int y_axis, Hero hero) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.hero = hero;
        this.shoot1 = new Vec2(-20, -5);
        this.shoot2 = new Vec2(+20, -5);

        /**
         *  Interactions with the other bodies.
         */
        this.category = MasterPilotWorld.ENEMY;
        this.maskBit = MasterPilotWorld.PLANET
                | MasterPilotWorld.SHIELD
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
        fd.density = 0.5f;
        fd.friction = 0f;
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


        chs.createChain(vertices, 3);


        FixtureDef fs = new FixtureDef();
        fs.shape = chs;

        fs.isSensor = false;
        fs.density = 0.0f;
        fs.friction = 0.3f;
        fs.restitution = 0.5f;

        fs.filter.categoryBits = MasterPilotWorld.ENEMY;
        fs.filter.maskBits = MasterPilotWorld.HERO | MasterPilotWorld.PLANET | MasterPilotWorld.SHIELD ;


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


        chr.createChain(rVertices, 3);


        //ps.set(vertices[1], vertices[2]);


        FixtureDef fr = new FixtureDef();
        fr.shape = chr;

        fr.isSensor = false;
        fr.density = 0.0f;
        fr.friction = 0.3f;
        fr.restitution = 0.5f;

        fr.filter.categoryBits = MasterPilotWorld.ENEMY;
        fr.filter.maskBits = MasterPilotWorld.HERO | MasterPilotWorld.PLANET | MasterPilotWorld.SHIELD;

        fr.userData = new EnemyBehaviour(this, Color.BLUE);


/*****************************************************/

        Body body = this.world.createBody(bd);
        body.createFixture(fd);
        body.createFixture(fs);
        body.createFixture(fr);

        this.body = body;
    }

    @Override
    public void right() {
        throw new NotImplementedException();
    }

    @Override
    public void left() {
        throw new NotImplementedException();
    }

    @Override
    public void up() {
        throw new NotImplementedException();
    }

    @Override
    public void down() {
        throw new NotImplementedException();
    }

    public void move() {
        Vec2 force = body.getWorldVector(new Vec2(0, +10f));
        this.body.setTransform(body.getPosition(), -(hero.getBody().getAngle()));
        //this.body.applyForceToCenter(force);
        this.body.applyAngularImpulse(0.05f);
        this.body.applyForceToCenter(force);
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
        int maskBit = MasterPilotWorld.SHIELD | MasterPilotWorld.PLANET;
        int category = MasterPilotWorld.SHOOT;
        RayFire rayon1 = new RayFire(this.world, worldPoint1.x, worldPoint1.y, category, maskBit, Color.orange);
        rayon1.create();
        RayFire rayon2 = new RayFire(this.world, worldPoint2.x, worldPoint2.y, category, maskBit, Color.orange);
        rayon2.create();

        /**
         * Apply force to the specific point
         */
        Vec2 force = body.getWorldVector(rayonForce);
        Vec2 point1 = body.getWorldPoint(rayon1.getBody().getWorldCenter());
        Vec2 point2 = body.getWorldPoint(rayon2.getBody().getWorldCenter());

        /**
         * need to do transform to position the shoot
         * in good direction
         */
        rayon1.getBody().setTransform(worldPoint1, body.getAngle());
        rayon1.getBody().applyForce(force, point1);
        rayon2.getBody().setTransform(worldPoint2, body.getAngle());

        rayon2.getBody().applyForce(force, point2);


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
