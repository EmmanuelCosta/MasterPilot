package fr.umlv.masterPilote.hero;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Rot;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import fr.umlv.masterPilote.world.MasterPiloteWorld;

public class Hero2 {
	private final int x_axis;
	private final int y_axis;
	private Body body;

	public Hero2(int x_axis, int y_axis) {
		super();
		this.x_axis = x_axis;
		this.y_axis = y_axis;
		this.body = create();
	}

	private Body create() {

		MasterPiloteWorld.world.setGravity(new Vec2(0.0f, 0.0f));

		Transform xf1 = new Transform();
		xf1.q.set(0.3524f * MathUtils.PI);
		Rot.mulToOutUnsafe(xf1.q, new Vec2(1.0f, 0.0f), xf1.p);

		Vec2 vertices[] = new Vec2[3];
		vertices[0] = Transform.mul(xf1, new Vec2(-1.0f, 0.0f));
		vertices[1] = Transform.mul(xf1, new Vec2(1.0f, 0.0f));
		vertices[2] = Transform.mul(xf1, new Vec2(0.0f, 0.5f));
		System.out.println("vertces = " + vertices[0] + "  " + vertices[1]
				+ " " + vertices[2]);
		PolygonShape poly1 = new PolygonShape();
		poly1.set(vertices, 3);

		FixtureDef sd1 = new FixtureDef();
		sd1.shape = poly1;
		sd1.density = 4.0f;

		Transform xf2 = new Transform();
		xf2.q.set(-0.3524f * MathUtils.PI);
		Rot.mulToOut(xf2.q, new Vec2(-1.0f, 0.0f), xf2.p);

		vertices[0] = Transform.mul(xf2, new Vec2(-1.0f, 0.0f));
		vertices[1] = Transform.mul(xf2, new Vec2(1.0f, 0.0f));
		vertices[2] = Transform.mul(xf2, new Vec2(0.0f, 0.5f));
		System.out.println("vertces = " + vertices[0] + "  " + vertices[1]
				+ " " + vertices[2]);
		PolygonShape poly2 = new PolygonShape();
		poly2.set(vertices, 3);

		FixtureDef sd2 = new FixtureDef();
		sd2.shape = poly2;
		sd2.density = 2.0f;

		BodyDef bd = new BodyDef();
		bd.type = BodyType.DYNAMIC;
		bd.angularDamping = 5.0f;
		bd.linearDamping = 0.1f;

		bd.position.set(0.0f, 2.0f);
		bd.angle = MathUtils.PI;
		bd.allowSleep = false;
		Body body = MasterPiloteWorld.world.createBody(bd);
		body.createFixture(sd1);
		body.createFixture(sd2);
		return body;

	}

	public Body getBody() {
		return body;
	}
}
