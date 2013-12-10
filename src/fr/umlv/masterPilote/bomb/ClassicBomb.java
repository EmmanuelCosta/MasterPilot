package fr.umlv.masterPilote.bomb;

import fr.umlv.masterPilote.MasterPilote;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import java.awt.*;

/**
 * classic bomb are normal bomb
 * by default is hero friendly but
 * enemy hostile
 *
 * created By Babala Costa Emmanuel
 */
public class ClassicBomb {

	private final int radius = 2;
    private final int maskBit;
    private final int category;
    private final Color color;
    private World world;
	private float x_axis;
	private float y_axis;
	private Body body;

	public ClassicBomb(World world, float x_axis, float y_axis) {
		this.world = world;
		this.x_axis = x_axis;
		this.y_axis = y_axis;

        this.category = MasterPilote.ENEMY;
        this.maskBit = MasterPilote.HERO | MasterPilote.PLANET;

            this.color = Color.WHITE;

	}

    public ClassicBomb(World world, float x_axis, float y_axis,int category, int maskBit,Color color) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;

        this.category = category;
        this.maskBit=maskBit;

        this.color = color;
    }

	public void create() {

		// create ball
		CircleShape cs = new CircleShape();

		cs.m_radius = radius;

		// Create an JBox2D body defination for ball.
		BodyDef bd = new BodyDef();

		bd.position.set(x_axis, y_axis);
		bd.type = BodyType.DYNAMIC;
        bd.userData=this;

		// Create a fixture for ball
		FixtureDef fd = new FixtureDef();
		// applique toi a cs
		fd.shape = cs;
		fd.density = 0.0f;
		fd.friction = 0.1f;
		fd.restitution = 0.5f;

		fd.filter.categoryBits = this.category;

		fd.filter.maskBits = this.maskBit;

		fd.userData = color;

		// body
		Body body = this.world.createBody(bd);
		body.createFixture(fd);
		this.body = body;

	}

	public void applyForce() {
		Vec2 f = new Vec2(5000f,5000f);
		Vec2 p = body.getWorldPoint(body.getLocalCenter().add(
				new Vec2(0.0f, 200.0f)));
		body.applyLinearImpulse(f, p);



	}

	public Body getBody() {
		return body;
	}

}
