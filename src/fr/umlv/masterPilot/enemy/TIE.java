package fr.umlv.masterPilot.enemy;

import fr.umlv.masterPilot.Interface.Bomb;
import fr.umlv.masterPilot.Interface.SpaceShip;
import fr.umlv.masterPilot.bomb.RayBomb;
import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.hero.Hero;
import fr.umlv.masterPilot.world.MasterPilot;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class TIE implements SpaceShip {

    private final int maskBit;
    private final int category;
    private final Timer timer = new Timer();
    private World world;
    private final int x_axis;
    private  final int y_axis;
    private Body body;
    private Vec2 rayonForce = new Vec2(0f, -10f);
    private Vec2 shoot1;
    private Vec2 shoot2;
    private final Hero hero;

    public TIE(World world, int x_axis, int y_axis, Hero hero) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.hero = hero;
        this.shoot1 = new Vec2(-20, -5);
        this.shoot2 = new Vec2(+20, -5);

        /**
         *  Interactions with the other bodies.
         */
        this.category = MasterPilot.ENEMY;
        this.maskBit = MasterPilot.PLANET
                | MasterPilot.SHIELD
                | MasterPilot.SHOOT
                | MasterPilot.BOMB
                | MasterPilot.MEGABOMB;
    }

    public int getX_axis() {
        return x_axis;
    }

    public int getY_axis() {
        return y_axis;
    }

    public void create() {

        /**
         * Primitive form
         */
        PolygonShape ps = new PolygonShape();

        /**
         * Number of vertices.
         */
        Vec2[] vertices = new Vec2[6];
        vertices[0] = new Vec2(-10, -5);
        vertices[1] = new Vec2(-20, +0);
        vertices[2] = new Vec2(-10, +5);
        vertices[3] = new Vec2(+10, +5);
        vertices[4] = new Vec2(+20, +0);
        vertices[5] = new Vec2(+10, -5);
        ps.set(vertices, 6);

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
        fd.shape = ps;
        fd.density = 0.5f;
        fd.friction = 0f;
        fd.restitution = 0.5f;
        fd.filter.categoryBits = this.category;
        fd.filter.maskBits = this.maskBit;

        fd.userData = new UserSpec() {

            private boolean destroy = false;

            @Override
            public void onCollide(Fixture fix2, boolean flag) {

                if (flag == false) {
                    if (fix2.getFilterData().categoryBits == (MasterPilot.SHOOT)
                            || fix2.getFilterData().categoryBits == (MasterPilot.SHIELD)) {

                        this.destroy = true;

                    }
                }
            }

            @Override
            public boolean isDestroyable() {
                return destroy;
            }

            @Override
            public Color getColor() {
                return Color.BLUE;
            }
        };

        /**
         * Integrate the body in the world.
         */
        Body body = this.world.createBody(bd);
        body.createFixture(fd);
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
        int maskBit = MasterPilot.SHIELD | MasterPilot.PLANET;
        int category = MasterPilot.SHOOT;
        RayBomb rayon1 = new RayBomb(this.world, worldPoint1.x, worldPoint1.y, category, maskBit, Color.orange);
        rayon1.create();
        RayBomb rayon2 = new RayBomb(this.world, worldPoint2.x, worldPoint2.y, category, maskBit, Color.orange);
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
