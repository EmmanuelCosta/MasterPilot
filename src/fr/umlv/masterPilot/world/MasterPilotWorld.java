package fr.umlv.masterPilot.world;

import fr.umlv.masterPilot.bomb.Bomb;
import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.graphic.MasterPilot2D;
import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.ship.hero.Hero;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.ChainShape;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * created by Babala Costa Emmanuel
 * <p>
 * This represent MasterPilotWorld
 */
public class MasterPilotWorld implements ContactListener {

    /**
     * This is category of MasterPilotworld
     */
    public static int HERO = 0x0001;
    public static int ENEMY = 0x0002;
    public static int PLANET = 0x0004;
    public static int SHOOT = 0x0008;
    public static int BOMB = 0x010;
    public static int MEGABOMB = 0x020;
    public static int SHIELD = 0x040;
    public static int TRAIL = 0x080;
    public static int RADAR = 0x100;
    /**
     * this is my world
     */

    private final World world;
    /**
     * use this to render purpose
     */
    private final MasterPilot2D masterPilot2D;
    private final HashMap<Body, SpaceShip> enemyManager;
    private final HashMap<Body, Bomb> bombManager;

    private final ArrayList<Body> destroyBody;
    /**
     * keep reference o main character of the game
     */
    private Hero hero;

    public Hero getHero() {
        return this.hero;
    }
    
    public MasterPilotWorld(Graphics2D masterPilot2D) {
        this.world = new World(new Vec2(0, 0f));

        this.world.setContactListener(this);
        this.masterPilot2D = new MasterPilot2D(masterPilot2D);


        this.enemyManager = new HashMap<>();
        this.bombManager = new HashMap<>();
        this.destroyBody = new ArrayList<>();


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

        Transform xf = new Transform();
        for (Body b = this.world.getBodyList(); b != null; b = b.getNext()) {
            xf.set(b.getTransform());
            for (Fixture f = b.getFixtureList(); f != null; f = f.getNext()) {

                drawShape(f, xf);
            }
        }
    }

    /**
     * this call the {@code MasterPilot2D }
     * to draw fixture of the World
     * <p>
     * The key is to use  Transform.mulToOutUnsafe() method to apply
     *
     * @param fixture : fixture of the body to draw
     * @param xf      : necessary for translation purpose
     */
    private void drawShape(Fixture fixture, Transform xf) {

        /**
         * retreive body color
         *
         */


        if (Objects.isNull(fixture) || Objects.isNull(fixture.getType())) {
            //Just ignore it
            System.out.println("null");
            throw new RuntimeException();

        }

//
        if (fixture.m_isSensor) return;
        UserSpec userData = (UserSpec) fixture.getUserData();
        Color color = userData.getColor();
        switch (fixture.getType()) {
            case CIRCLE: {
                Vec2 center = new Vec2();
                CircleShape circle = (CircleShape) fixture.getShape();
//take the default position and calculate his actual position in the world
                Transform.mulToOutUnsafe(xf, circle.m_p, center);
                float radius = circle.m_radius;

                /**
                 * if shield is set
                 * so we can draw it
                 */
                if (fixture.getFilterData().categoryBits == MasterPilotWorld.SHIELD) {
                    /**
                     * we draw only if
                     * shield can collide which other
                     */

                    if (!fixture.m_isSensor) {
                        masterPilot2D.drawCircle(center, radius, color, false);

                    }
                } else if(fixture.getFilterData().categoryBits != MasterPilotWorld.RADAR) {
                    masterPilot2D.drawCircle(center, radius, color, true);
                }

            }
            break;

            case POLYGON: {

                Vec2Array tlvertices = new Vec2Array();

                PolygonShape poly = (PolygonShape) fixture.getShape();
                int vertexCount = poly.m_count;
                assert (vertexCount <= Settings.maxPolygonVertices);
                Vec2[] vertices = tlvertices.get(Settings.maxPolygonVertices);

                for (int i = 0; i < vertexCount; ++i) {
                    Transform.mulToOutUnsafe(xf, poly.m_vertices[i], vertices[i]);
                }

                masterPilot2D.drawFilledPolygon(vertices, vertexCount, color);

                if (fixture.getFilterData().categoryBits == MasterPilotWorld.BOMB) {

                    Vec2 position = fixture.getBody().getWorldCenter();

                    masterPilot2D.drawString(position, "B");

                } else if (fixture.getFilterData().categoryBits == MasterPilotWorld.MEGABOMB) {
                    Vec2 position = fixture.getBody().getWorldCenter();

                    masterPilot2D.drawString(position, "M.B");
                }
            }
            break;
            case EDGE: {
                Vec2 v1 = new Vec2();
                Vec2 v2 = new Vec2();

                EdgeShape edge = (EdgeShape) fixture.getShape();
                Transform.mulToOutUnsafe(xf, edge.m_vertex1, v1);
                Transform.mulToOutUnsafe(xf, edge.m_vertex2, v2);

                masterPilot2D.drawSegment(v1,v2, color);
            }
            break;

            case CHAIN: {
                ChainShape chain = (ChainShape) fixture.getShape();
                int count = chain.m_count;
                Vec2[] vertices = chain.m_vertices;
                Vec2 v1 = new Vec2();
                Vec2 v2 = new Vec2();

                Transform.mulToOutUnsafe(xf, vertices[0], v1);
                for (int i = 1; i < count; ++i) {
                    Transform.mulToOutUnsafe(xf, vertices[i], v2);
                    masterPilot2D.drawSegment(v1, v2, color);
//                    masterPilot2D.drawCircle(v1, 0.05f);
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


        UserSpec userData = (UserSpec) fixtureA.getUserData();
        userData.onCollide(fixtureB, true);
        fixtureA.m_isSensor = userData.getSensor();

        UserSpec userData2 = (UserSpec) fixtureB.getUserData();
        userData2.onCollide(fixtureA, true);
        fixtureB.m_isSensor = userData2.getSensor();

        if (userData.isItem()) {
            Body b = fixtureA.getBody();

            Bomb bomb = this.bombManager.get(b);

            this.hero.setBomb(bomb);
        }
        if (userData2.isItem()) {
            Bomb bomb = this.bombManager.get(fixtureB.getBody());
            this.hero.setBomb(bomb);
        }
/**
 * collision with bomb
 */


    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();


        UserSpec userData = (UserSpec) fixtureA.getUserData();
        userData.onCollide(fixtureB, false);
        fixtureA.m_isSensor = userData.getSensor();

        if (userData.isDestroyable()) {
            destroyBody.add(fixtureA.getBody());
        }

        if(userData.hasJointBody()){
            List<Body> jointBody = userData.getJointBody();
            for (Body b = world.getBodyList(); b != null; b = b.getNext()){
               if(jointBody.contains(b)){
                   destroyBody.add(b);
               }

            }
        }
        UserSpec userData2 = (UserSpec) fixtureB.getUserData();
        userData2.onCollide(fixtureA, false);
        fixtureB.m_isSensor = userData2.getSensor();
        if (userData2.isDestroyable()) {
            destroyBody.add(fixtureB.getBody());
        }
        if(userData2.hasJointBody()){
            List<Body> jointBody = userData2.getJointBody();
            for (Body b = world.getBodyList(); b != null; b = b.getNext()){
                if(jointBody.contains(b)){
                    destroyBody.add(b);
                }

            }
        }


    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fixtureA = contact.getFixtureA();
        fixtureA.getBody().setTransform(new Vec2(80,100),1500);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    /**
     * this will return a body list of all body
     * which collide and it is destinate to be destroyed
     *
     * @return
     */
    public List<Body> getDestroyBody() {
        List<Body> newList = new ArrayList<>(destroyBody);
        /**
         * retreive spaceship destroyed from manager
         */
        for (Body bd : newList) {

            this.removeToSpaceshipManager(bd);

        }


        destroyBody.clear();
        return newList;
    }

    public Body getBodyHero() {
        return hero.getBody();
    }

    public void addToSpaceshipManager(Body bodySpaceship, SpaceShip spaceShip) {
        if (!enemyManager.containsKey(bodySpaceship)) {
            this.enemyManager.put(bodySpaceship, spaceShip);
        }
    }

    public SpaceShip removeToSpaceshipManager(Body bodySpaceship) {

        return this.enemyManager.remove(bodySpaceship);

    }

    public List<SpaceShip> getEnemyList() {
        return new ArrayList<>(this.enemyManager.values());
    }

    public void addToBombManager(Body body, Bomb bomb) {
        if (!this.bombManager.containsKey(body)) {
            this.bombManager.put(body, bomb);
        }
    }


    public void drawFrameworkClock(int hour,int minute,int second){
        this.masterPilot2D.drawFrameworkClock(hour,minute,second);
    }


    public void drawFrameworkEnd(boolean iswin, int x, int y) {
        this.masterPilot2D.drawFrameworkEnd(iswin,x,y);
    }
}
