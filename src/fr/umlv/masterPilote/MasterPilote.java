package fr.umlv.masterPilote;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Settings;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.pooling.arrays.Vec2Array;

import fr.umlv.masterPilote.Graphic.MasterPilote2D;

public class MasterPilote {

	private World world;
	private DebugDraw graphic;

	public MasterPilote(Graphics graphic) {
		this.world = new World(new Vec2(0, 0));
		this.graphic = new MasterPilote2D(graphic);
	}

	public void initWorld() {
		// TODO
	}

	public void initPlateForm(int WIDTH, int HEIGHT) {
		MasterPilote2D mp2D = (MasterPilote2D) graphic;
		Graphics2D graphics = (Graphics2D) mp2D.getGraphics();
		graphics.setColor(Color.BLACK);
		graphics.fill(new Rectangle(0, 0, WIDTH, HEIGHT));
	}

    public void repaint(int WIDTH, int HEIGHT){
        MasterPilote2D mp2D = (MasterPilote2D) graphic;
        Graphics2D graphics = (Graphics2D) mp2D.getGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fill(new Rectangle(0, 0, WIDTH, HEIGHT));
    }
	public World getWorld() {
		return world;
	}

	private final Color3f color = new Color3f();
	private final Transform xf = new Transform();

	public void draw() {
        int i=0;
        for (Body b = this.world.getBodyList(); b != null; b = b.getNext()) {
            System.out.println(i++);
            xf.set(b.getTransform());
			for (Fixture f = b.getFixtureList(); f != null; f = f.getNext()) {
				// TODO SET TYPE
				color.set(0.5f, 0.5f, 0.3f);
				drawShape(f, xf, color);
			}
		}
	}

	private final Vec2 center = new Vec2();
	private final Vec2 axis = new Vec2();
	private final Vec2 v1 = new Vec2();
	private final Vec2 v2 = new Vec2();
	private final Vec2Array tlvertices = new Vec2Array();

	private void drawShape(Fixture fixture, Transform xf, Color3f color) {

		switch (fixture.getType()) {
		case CIRCLE: {
			CircleShape circle = (CircleShape) fixture.getShape();

			// Vec2 center = Mul(xf, circle.m_p);
			Transform.mulToOutUnsafe(xf, circle.m_p, center);
			float radius = circle.m_radius;
			xf.q.getXAxis(axis);
			System.out.println("center = " + center + " axis = " + axis);
			graphic.drawSolidCircle(center, radius, axis, color);
		}
			break;

		case POLYGON: {
			PolygonShape poly = (PolygonShape) fixture.getShape();
			int vertexCount = poly.m_count;
			assert (vertexCount <= Settings.maxPolygonVertices);
			Vec2[] vertices = tlvertices.get(Settings.maxPolygonVertices);

			for (int i = 0; i < vertexCount; ++i) {
				// vertices[i] = Mul(xf, poly.m_vertices[i]);
				Transform.mulToOutUnsafe(xf, poly.m_vertices[i], vertices[i]);
			}

			graphic.drawSolidPolygon(vertices, vertexCount, color);
		}
			break;
		case EDGE: {
			EdgeShape edge = (EdgeShape) fixture.getShape();
			Transform.mulToOutUnsafe(xf, edge.m_vertex1, v1);
			Transform.mulToOutUnsafe(xf, edge.m_vertex2, v2);
			graphic.drawSegment(v1, v2, color);
		}
			break;

		case CHAIN: {
			ChainShape chain = (ChainShape) fixture.getShape();
			int count = chain.m_count;
			Vec2[] vertices = chain.m_vertices;

			Transform.mulToOutUnsafe(xf, vertices[0], v1);
			for (int i = 1; i < count; ++i) {
				Transform.mulToOutUnsafe(xf, vertices[i], v2);
				graphic.drawSegment(v1, v2, color);
				graphic.drawCircle(v1, 0.05f, color);
				v1.set(v2);
			}
		}
			break;
		default:
			break;
		}

	}

	public void setLandMark(int i, int j) {
		this.graphic.getViewportTranform().setExtents(i, j);

	}

	public void erasePaint() {
		// this.graphic.reset();

	}

}
