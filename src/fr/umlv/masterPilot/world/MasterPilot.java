package fr.umlv.masterPilot.world;

import fr.umlv.masterPilot.Graphic.MasterPilot2D;
import fr.umlv.masterPilot.hero.Shield;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
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
 *
 * This represent MasterPilot World
 *
 */
public class MasterPilot {

    /**
     * This is category of MasterPilot world
     */
    public static int HERO = 0x0001;
    public static int ENEMY = 0x0002;
    public static int PLANET = 0x0004;

    public static int SHOOT = 0x0008;
    public static int BOMB = 0x00016;
    

    /**
     * this is my world
     */

    private final World world;

    /**
     * this help to do translation and rotation
     */
    private final Transform xf = new Transform();
    private final Vec2 center = new Vec2();
    private final Vec2 axis = new Vec2();
    private final Vec2 v1 = new Vec2();
    private final Vec2 v2 = new Vec2();
    private final Vec2Array tlvertices = new Vec2Array();
    private final MasterContactListener contactListener;
    /**
     * use this to render purpose
     */
    private MasterPilot2D masterPilot2D;
    /**
     * keep reference of main character of the game
     */
    private Body Hero;

    public MasterPilot(Graphics masterPilot2D) {
        this.world = new World(new Vec2(0, 0f));
        this.contactListener = new MasterContactListener( this.world);
        this.world.setContactListener(contactListener);
        this.masterPilot2D = new MasterPilot2D(masterPilot2D);


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
        MasterPilot2D mp2D = masterPilot2D;
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
        MasterPilot2D mp2D = masterPilot2D;
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

    /**
     * set Hero to this class
     * and to the contact listener
     * in order to to specific action on it
     * @param hero
     */
    public void setHero(Body hero) {
        Objects.requireNonNull(hero);
        this.Hero = hero;
        this.contactListener.setHero(hero);
    }

    /**
     * this iterate on every body in the world
     * and call the {@code drawShape}
     */
    public void draw() {
/**
 * this loop iterate on all body attach to the world
 *
 */

        for (Body b = this.world.getBodyList(); b != null; b = b.getNext()) {
            xf.set(b.getTransform());
            for (Fixture f = b.getFixtureList(); f != null; f = f.getNext()) {

                drawShape((Class)b.getUserData(),f, xf);
            }
        }
    }

    /**
     * this call the {@code MasterPilot2D }
     * to draw fixture of the World
     *
     *The key is to use  Transform.mulToOutUnsafe() method to apply
     *
     * @param clazz
     * @param fixture : fixture of the body to draw
     * @param xf      : necessary for translation purpose
     */
    private void drawShape(Class clazz,Fixture fixture, Transform xf) {

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
                if(clazz == Shield.class){
                    masterPilot2D.drawCircle(center, radius, axis, color, false);
                }else{
                    masterPilot2D.drawCircle(center, radius, axis, color, true);
                }

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

                masterPilot2D.drawFilledPolygon(vertices, vertexCount, color);
            }
            break;
            case EDGE: {
                EdgeShape edge = (EdgeShape) fixture.getShape();
                Transform.mulToOutUnsafe(xf, edge.m_vertex1, v1);
                Transform.mulToOutUnsafe(xf, edge.m_vertex2, v2);
                masterPilot2D.drawSegment(v1, v2, color);
            }
            break;


            default:
                break;
        }

    }

    /**
     * This will set the (0,0) to the
     *
     * specify param
     *
     * use this to change referentiel
     * made a translation of (0,0) -> (i,j)
     *
     * @param i
     * @param j
     */
    public void setLandMark(int i, int j) {
        this.masterPilot2D.setLandMark(i,j);

    }

    /**
     * This method resize camera to
     * the position specify
     *
     * @param position
     */
    public void setCamera(Vec2 position) {
        this.masterPilot2D.setCamera(position);
    }


}
