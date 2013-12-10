package fr.umlv.masterPilote;

import fr.umlv.masterPilote.Graphic.MasterPilote2DDebug;
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

import java.awt.*;
import java.util.Objects;

/**
 * created by Babala Costa Emmanuel
 */
public class MasterPilote {


    public static int HERO = 0x0001;
    public static int ENEMY = 0x0002;
    public static int PLANET = 0x0004;
    private final World world;
    private final Color3f color = new Color3f();
    private final Transform xf = new Transform();
    private final Vec2 center = new Vec2();
    private final Vec2 axis = new Vec2();
    private final Vec2 v1 = new Vec2();
    private final Vec2 v2 = new Vec2();
    private final Vec2Array tlvertices = new Vec2Array();
    private DebugDraw graphic;
    private Body Hero;

    public MasterPilote(Graphics graphic) {
        this.world = new World(new Vec2(0, 0f));
        this.graphic = new MasterPilote2DDebug(graphic);


    }

    public void initWorld() {
        // TODO
    }

    /**
     * call this to initiate
     * the plate
     *
     * @param WIDTH
     * @param HEIGHT
     */
    public void initPlateForm(int WIDTH, int HEIGHT) {
        MasterPilote2DDebug mp2D = (MasterPilote2DDebug) graphic;
        Graphics2D graphics = (Graphics2D) mp2D.getGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fill(new Rectangle(0, 0, WIDTH, HEIGHT));
    }

    public void repaint(int WIDTH, int HEIGHT) {
        MasterPilote2DDebug mp2D = (MasterPilote2DDebug) graphic;
        Graphics2D graphics = (Graphics2D) mp2D.getGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fill(new Rectangle(0, 0, WIDTH, HEIGHT));

    }

    public World getWorld() {
        return world;
    }

    public Body getHero() {
        return Hero;
    }

    public void setHero(Body hero) {
        Objects.requireNonNull(hero);
        this.Hero = hero;
    }

    /**
     * call this to draw every body
     * in the World
     */
    public void draw() {
        int i = 0;

        for (Body b = this.world.getBodyList(); b != null; b = b.getNext()) {
            xf.set(b.getTransform());
            for (Fixture f = b.getFixtureList(); f != null; f = f.getNext()) {
                // TODO SET TYPE
                color.set(0.5f, 0.5f, 0.3f);
                drawShape(f, xf, color);
            }
        }
    }

    /**
     * this specify how to draw every type of shape
     * in the World
     *
     * @param fixture
     * @param xf
     * @param color
     */
    private void drawShape(Fixture fixture, Transform xf, Color3f color) {

        switch (fixture.getType()) {
            case CIRCLE: {
                CircleShape circle = (CircleShape) fixture.getShape();

                // Vec2 center = Mul(xf, circle.m_p);
                Transform.mulToOutUnsafe(xf, circle.m_p, center);
                float radius = circle.m_radius;
                xf.q.getXAxis(axis);
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

    /**
     * This will set the (0,0) to the
     * specify param
     *
     * @param i
     * @param j
     */
    public void setLandMark(int i, int j) {
        this.graphic.getViewportTranform().setExtents(i, j);

    }

    /**
     * This method resize camera to
     * the position specify
     *
     * @param position
     */
    public void setCamera(Vec2 position) {
        this.graphic.getViewportTranform().setCenter(position);
    }

    public void updateScreen() {
//        Graphics2D dispose = (Graphics2D) graphic;

    }
}
