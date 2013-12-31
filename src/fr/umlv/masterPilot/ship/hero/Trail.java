package fr.umlv.masterPilot.ship.hero;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.*;
import java.awt.*;

/**
 *
 *
 * is the trace left by hero spaceship when it move up
 * <p>
 * created By Babala Costa Emmanuel
 */
public class Trail {
    private final int maskBit;
    private final int category;
    private final Color color;
    private final World world;
    private final float x_axis;
    private final float y_axis;
    private int radius = 2;
    private Body body;

    /**
     *
     *@param world
     * @param x_axis : X coordinate of his initial position in yhe world
     * @param y_axis : Y cordinate of his initial position in yhe world
     * @param category : Define the category
     * @param maskBit : Define the order category with which it can collide
     *
     * @param color :color of trail
     * @param radius :radius of trail
     */
    public Trail(World world, float x_axis, float y_axis, int category, int maskBit, Color color, int radius) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.category = category;
        this.maskBit = maskBit;
        this.color = color;
        this.radius = radius;
    }

    /**
     * create a body trail in the jbox 2d world
     */
    public void create() {
        CircleShape cs = new CircleShape();
        cs.m_radius = radius;

        BodyDef bd = new BodyDef();
        bd.position.set(x_axis, y_axis);
        bd.type = BodyType.DYNAMIC;
        bd.allowSleep = true;
        bd.userData = this.getClass();

        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 0.0f;
        fd.friction = 0.0f;
        fd.restitution = 0.0f;
        fd.filter.categoryBits = this.category;
        fd.filter.maskBits = this.maskBit;
        fd.userData = new TrailBehavior(this.color);

        Body body = this.world.createBody(bd);
        body.createFixture(fd);
        this.body = body;
    }

    public Body getBody() {
        return this.body;
    }
}
