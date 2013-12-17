package fr.umlv.masterPilot.world;

import fr.umlv.masterPilot.Graphic.MasterPilot2D;
import fr.umlv.masterPilot.Interface.Bomb;
import fr.umlv.masterPilot.bomb.ExplodeBomb;
import fr.umlv.masterPilot.hero.Hero;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Settings;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.pooling.arrays.Vec2Array;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * created by Babala Costa Emmanuel
 * <p>
 * This represent MasterPilot World
 */
public class MasterPilot implements ContactListener {

    /**
     * This is category of MasterPilot world
     */
    public static int HERO = 0x0001;
    public static int ENEMY = 0x0002;
    public static int PLANET = 0x0004;
    public static int SHOOT = 0x0008;
    public static int BOMB = 0x00016;
    public static int MEGABOMB = 0x00032;
    public static int SHIELD = 0x00064;
    /**
     * this is my world
     */

    private final World world;
    /**
     * this help to do translation and rotation
     */
    private final Transform xf = new Transform();
    private final Vec2 center = new Vec2();
    private final Vec2 v1 = new Vec2();
    private final Vec2 v2 = new Vec2();
    private final Vec2Array tlvertices = new Vec2Array();
    /**
     * use this to render purpose
     */
    private final MasterPilot2D masterPilot2D;
    //private final MasterContactListener contactListener;
    private List<Body> destroyBody = new ArrayList<>();
    /**
     * keep reference of main character of the game
     */
    private Hero hero;


    public MasterPilot(Graphics masterPilot2D) {
        this.world = new World(new Vec2(0, 0f));
        //this.contactListener = new MasterContactListener( this.world);
        this.world.setContactListener(this);
        this.masterPilot2D = new MasterPilot2D(masterPilot2D);


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

    /**
     * set Hero to this class
     *
     * @param hero
     */
    public void setHero(Hero hero) {
        Objects.requireNonNull(hero);
        this.hero = hero;
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

                drawShape((Class) b.getUserData(), f, xf);
            }
        }
    }

    /**
     * this call the {@code MasterPilot2D }
     * to draw fixture of the World
     * <p>
     * The key is to use  Transform.mulToOutUnsafe() method to apply
     *
     * @param clazz
     * @param fixture : fixture of the body to draw
     * @param xf      : necessary for translation purpose
     */
    private void drawShape(Class clazz, Fixture fixture, Transform xf) {

        /**
         * retreive body color
         *
         */

        Color color = (Color) fixture.getUserData();
        if (Objects.isNull(fixture.getType())) {
            //Just ignore it
            throw new RuntimeException();
            // return;
        }
        switch (fixture.getType()) {
            case CIRCLE: {
                CircleShape circle = (CircleShape) fixture.getShape();

                Transform.mulToOutUnsafe(xf, circle.m_p, center);
                float radius = circle.m_radius;

                /**
                 * if shield is set
                 * so we can draw it
                 */
                if (fixture.getFilterData().categoryBits == MasterPilot.SHIELD) {
                    /**
                     * we draw only if
                     * shield can collide which other
                     */
                    if (!fixture.m_isSensor) {
                        masterPilot2D.drawCircle(center, radius, color, false);
                    }


                } else {
                    masterPilot2D.drawCircle(center, radius, color, true);
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

                if (fixture.getFilterData().categoryBits == MasterPilot.BOMB) {

                    Vec2 position = fixture.getBody().getWorldCenter();

                        masterPilot2D.drawString(position, "B");

                }else if (fixture.getFilterData().categoryBits == MasterPilot.MEGABOMB) {
                    Vec2 position = fixture.getBody().getWorldCenter();

                    masterPilot2D.drawString(position, "M.B");
                }
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
     * <p>
     * specify param
     * <p>
     * use this to change referentiel
     * made a translation of (0,0) -> (i,j)
     *
     * @param i
     * @param j
     */
    public void setLandMark(int i, int j) {
        this.masterPilot2D.setLandMark(i, j);

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

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();


/**
 * put the shield only if i won ' t it an item
 */
        if (fixtureA.getFilterData().categoryBits == MasterPilot.SHIELD &&
                fixtureB.getFilterData().categoryBits != MasterPilot.BOMB) {
            fixtureA.m_isSensor = false;
        }

        if (fixtureB.getFilterData().categoryBits == MasterPilot.SHIELD &&
                fixtureA.getFilterData().categoryBits != MasterPilot.BOMB) {
            fixtureB.m_isSensor = false;
        }
/**
 * collision with bomb
 */
        if (fixtureA.getFilterData().categoryBits == MasterPilot.BOMB
                || fixtureA.getFilterData().categoryBits == MasterPilot.MEGABOMB) {

            if (fixtureB.getBody().getUserData() == Hero.class) {
                // i have no bomb yet so that i can get it
                if (this.hero.getBombType() == Bomb.BombType.NONE) {
                    if(fixtureA.getFilterData().categoryBits == MasterPilot.BOMB){
                    this.hero.setBombType(Bomb.BombType.BOMB);
                    }else{
                        this.hero.setBombType(Bomb.BombType.MEGABOMB);
                    }
                    destroyBody.add(fixtureA.getBody());
                } else {
                    //  this.hero.triggerExplosion();
                }

            } else {
                this.hero.triggerExplosion();
                destroyBody.add(fixtureA.getBody());
            }

        }


        if (fixtureB.getFilterData().categoryBits == MasterPilot.BOMB ||
                fixtureB.getFilterData().categoryBits == MasterPilot.MEGABOMB) {

            if (fixtureA.getBody().getUserData() == Hero.class) {
                // i have no bomb yet so that i can get it
                if (this.hero.getBombType() == Bomb.BombType.NONE) {
                    if(fixtureB.getFilterData().categoryBits == MasterPilot.BOMB){
                        this.hero.setBombType(Bomb.BombType.BOMB);
                    }else{
                        this.hero.setBombType(Bomb.BombType.MEGABOMB);
                    }
                    destroyBody.add(fixtureB.getBody());
                } else {
                    // this.hero.triggerExplosion();
                }

            } else {
                this.hero.triggerExplosion();
                destroyBody.add(fixtureB.getBody());
            }
        }


    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();


        if (fixtureA.getFilterData().categoryBits == MasterPilot.SHIELD) {
            fixtureA.m_isSensor = true;
        }

        if (fixtureB.getFilterData().categoryBits == MasterPilot.SHIELD) {
            fixtureB.m_isSensor = true;
        }

        if (fixtureA.getFilterData().categoryBits == MasterPilot.SHOOT) {

            destroyBody.add(fixtureA.getBody());

        } else if (fixtureB.getFilterData().categoryBits == MasterPilot.SHOOT) {

            destroyBody.add(fixtureB.getBody());
        }
        if (fixtureA.getFilterData().categoryBits == MasterPilot.ENEMY) {
            destroyBody.add(fixtureA.getBody());

        } else if (fixtureB.getFilterData().categoryBits == MasterPilot.ENEMY) {
            destroyBody.add(fixtureB.getBody());
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public List<Body> getDestroyBody() {
        List<Body> newList = new ArrayList<>(destroyBody);

        destroyBody = new ArrayList<>();
        return newList;
    }

    public Body getBodyHero() {
        return hero.getBody();
    }
}
