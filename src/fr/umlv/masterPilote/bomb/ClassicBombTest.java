package fr.umlv.masterPilote.bomb;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class ClassicBombTest {

	private final int radius = 8;
	private World world;
	private int x_axis;
	private int y_axis;
	private Body body;

	public ClassicBombTest(World world, int x_axis, int y_axis) {
		this.world = world;
		this.x_axis = x_axis;
		this.y_axis = y_axis;
	}

	public void create() {

		// create ball
		CircleShape cs = new CircleShape();

		cs.m_radius = radius;

		// Create an JBox2D body defination for ball.
		BodyDef bd = new BodyDef();

		bd.position.set(x_axis, y_axis);
		bd.type = BodyType.DYNAMIC;

		// Create a fixture for ball
		FixtureDef fd = new FixtureDef();
		// applique toi a cs
		fd.shape = cs;
		fd.density = 0.5f;
		fd.friction = 0.5f;
		fd.restitution = 0.5f;

		fd.filter.categoryBits = 0x001;
		fd.filter.maskBits = 0x001;

		fd.userData = this;

		// body
		Body body = this.world.createBody(bd);
		body.createFixture(fd);
		this.body = body;

	}

	public void applyForce() {
		Vec2 f = new Vec2(1000f,3000f);
		Vec2 p = body.getWorldPoint(body.getLocalCenter().add(
				new Vec2(0.0f, 200.0f)));
		body.applyLinearImpulse(f, p);
		
		//System.out.println("velocity "+body.getLinearVelocity());
		System.out.println("position "+body.getPosition());
	}

	public Body getBody() {
		return body;
	}

}
