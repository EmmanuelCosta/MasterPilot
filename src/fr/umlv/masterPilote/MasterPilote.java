package fr.umlv.masterPilote;

import fr.umlv.masterPilote.Graphic.MasterPilote2D;
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

    /**
     * This is category of MasterPilote world
     */
    public static int HERO = 0x0001;
    public static int ENEMY = 0x0002;
    public static int PLANET = 0x0004;
    /**
     * this is my world
     */
    private final World world;
    private final Color3f color = new Color3f();
    /**
     * this help to do translation and rotation
     */
    private final Transform xf = new Transform();
    private final Vec2 center = new Vec2();
    private final Vec2 axis = new Vec2();
    private final Vec2 v1 = new Vec2();
    private final Vec2 v2 = new Vec2();
    private final Vec2Array tlvertices = new Vec2Array();
    /**
     * use this to render purpose
     */
    private MasterPilote2D graphic;
    /**
     * keep reference of main character of the game
     */
    private Body Hero;

    public MasterPilote(Graphics graphic) {
        this.world = new World(new Vec2(0, 0f));
//        this.graphic = new MasterPilote2DDebug(graphic);
        this.graphic = new MasterPilote2D(graphic);


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
//        MasterPilote2DDebug mp2D = (MasterPilote2DDebug) graphic;
        MasterPilote2D mp2D = graphic;
        Graphics2D graphics = (Graphics2D) mp2D.getGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fill(new Rectangle(0, 0, WIDTH, HEIGHT));
    }

    /**
     * reset the panel
     *
     * @param WIDTH
     * @param HEIGHT
     */
    public void repaint(int WIDTH, int HEIGHT) {
//        MasterPilote2DDebug mp2D = (MasterPilote2DDebug) graphic;
        MasterPilote2D mp2D = graphic;
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
     * this iterate on every body in the world
     * and call the {@code drawShape}
     */
    public void draw() {


        for (Body b = this.world.getBodyList(); b != null; b = b.getNext()) {
            xf.set(b.getTransform());
            for (Fixture f = b.getFixtureList(); f != null; f = f.getNext()) {

                drawShape(f, xf);
            }
        }
    }

    /**
     * this call the {@code MasterPilote2D }
     * to draw fixture of the World
     * in the graphic panel
     *
     * @param fixture : fixture of the body to draw
     * @param xf      : necessary for translation purpose
     */
    private void drawShape(Fixture fixture, Transform xf) {

        /**
         * retreive body color
         *
         */
        Color color = (Color) fixture.getUserData();

        switch (fixture.getType()) {
            case CIRCLE: {
                CircleShape circle = (CircleShape) fixture.getShape();

                Transform.mulToOutUnsafe(xf, circle.m_p, center);
                float radius = circle.m_radius;
                xf.q.getXAxis(axis);
                graphic.drawFilledCircle(center, radius, axis, color);
            }
            break;

            case POLYGON: {
                PolygonShape poly = (PolygonShape) fixture.getShape();
                int vertexCount = poly.m_count;
                assert (vertexCount <= Settings.maxPolygonVertices);
                Vec2[] vertices = tlvertices.get(Settings.maxPolygonVertices);

                for (int i = 0; i < vertexCount; ++i) {
                    Transform.mulToOutUnsafe(xf, poly.m_vertices[i], vertices[i]);
                }

                graphic.drawFilledPolygon(vertices, vertexCount, color);
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
