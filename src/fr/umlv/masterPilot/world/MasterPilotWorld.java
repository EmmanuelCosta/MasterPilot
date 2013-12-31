package fr.umlv.masterPilot.world;

import fr.umlv.masterPilot.bomb.Bomb;
import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.graphic.MasterPilot2D;
import fr.umlv.masterPilot.ship.RayFireManager;
import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.ship.hero.Hero;
import fr.umlv.masterPilot.ship.hero.TrailManager;
import fr.umlv.masterPilot.star.Star;

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
import java.util.*;
import java.util.List;


/**
 * created by Babala Costa Emmanuel
 * <p>
 * This represent MasterPilotWorld
 */
public class MasterPilotWorld implements ContactListener {

    /**
     * this is the available mode game
     * CHEAT : AUTOMATIC SHIELD FOR SPACESHIP HERO
     * HARDCORE : YOU MANAGE IT MANUALY
     */
    public static enum MODE{
        CHEAT,HARDCORE;
    }
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
    
    /**
     * this is a enny manager
     */
    private final HashMap<Body, SpaceShip> enemyManager;
    
    /**
     * this is a Bomb manager
     */
    private final HashMap<Body, Bomb> bombManager;
    
    /**
     * this is a star Manager
     */
    private final HashMap<Body, Star> starManager;
    
    /**
     * register here all the body to be remove from the jbox 2d world
     */
    private final ArrayList<Body> destroyBody;
    
    /**
     * keep reference o main character of the game
     */
    private Hero hero;

    /**
     *
     * @param masterPilot2D
     */
    public MasterPilotWorld(Graphics2D masterPilot2D) {
        this.world = new World(new Vec2(0, 0f));
        this.world.setContactListener(this);
        this.masterPilot2D = new MasterPilot2D(masterPilot2D);
        this.enemyManager = new HashMap<>();
        this.bombManager = new HashMap<>();
        this.starManager = new HashMap<>();
        this.destroyBody = new ArrayList<>();
    }

    /**
     * get the Hero register in the world
     * @return
     */
    public Hero getHero() {
        return this.hero;
    }

    /**
     * register Hero in the MasterPilotWorld
     *
     * @param hero
     */
    public void setHero(Hero hero) {
        Objects.requireNonNull(hero);
        this.hero = hero;
    }

    /**
     * call this to initiate
     * the plate with the given size
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
     * refresh the panel
     * Background color is Black
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

    /**
     * get the instance of jbox 2d world register in thjat class
     * his the one used for the game physic purpose
     * @return
     */
    public World getWorld() {
        return world;
    }

    /**
     * this iterate on every body in the world
     * and call the {@code drawShape} to draw the body
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
     *
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
            throw new RuntimeException();
        }

        if (fixture.m_isSensor) 
            return;
        
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
                } else if (fixture.getFilterData().categoryBits != MasterPilotWorld.RADAR) {
                    masterPilot2D.drawCircle(center, radius, color, true);
                }
            }
            break;

            case POLYGON: {
                Vec2Array tlvertices = new Vec2Array();
                PolygonShape poly = (PolygonShape) fixture.getShape();
                int vertexCount = poly.m_count;
                
                //BEcause Jbox doent allow a polygon with more than maxPolygonVertices
                assert (vertexCount <= Settings.maxPolygonVertices);
                
                //Create a vertices array of size given
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
                masterPilot2D.drawSegment(v1, v2, color);
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
     * made a translation of (0,0) to (i,j)
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
     * retrieve from userData the UserSpec implementation of the body
     * apply the onCollide method in order to perform his basic behavior
     * set the sensor for drawing it or not
     */
        UserSpec userData = (UserSpec) fixtureA.getUserData();
        userData.onCollide(fixtureB, true);
        fixtureA.m_isSensor = userData.getSensor();
        UserSpec userData2 = (UserSpec) fixtureB.getUserData();
        userData2.onCollide(fixtureA, true);
        fixtureB.m_isSensor = userData2.getSensor();
        /**
         * is he is an item
         * perform task on it
         */
        if (userData.isItem()) {
            Body b = fixtureA.getBody();
            Bomb bomb = this.bombManager.get(b);
            this.hero.setBomb(bomb);
        }
        if (userData2.isItem()) {
            Bomb bomb = this.bombManager.get(fixtureB.getBody());
            this.hero.setBomb(bomb);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        /**
         * retrieve from userData the UserSpec implementation of the body
         * apply the onCollide method in order to perform his basic behavior
         * set the sensor for drawing it or not
         */
        UserSpec userData = (UserSpec) fixtureA.getUserData();
        userData.onCollide(fixtureB, false);
        fixtureA.m_isSensor = userData.getSensor();
        
        /**
         * if the object is destroyable added it into the destroyBody List
         * and remove it from the rayFireManager if it is contains
         */
        if (userData.isDestroyable()) {
            destroyBody.add(fixtureA.getBody());
            RayFireManager.remove(fixtureA.getBody());
        }
        
        /**
         * this is for destroying every joint to the main body if it is destroy
         */
        if (userData.hasJointBody()) {
            List<Body> jointBody = userData.getJointBody();
            for (Body b = world.getBodyList(); b != null; b = b.getNext()) {
                if (jointBody.contains(b)) {
                    destroyBody.add(b);
                }
            }
        }
        UserSpec userData2 = (UserSpec) fixtureB.getUserData();
        userData2.onCollide(fixtureA, false);
        fixtureB.m_isSensor = userData2.getSensor();
        if (userData2.isDestroyable()) {
            destroyBody.add(fixtureB.getBody());
            RayFireManager.remove(fixtureB.getBody());
        }
        if (userData2.hasJointBody()) {
            List<Body> jointBody = userData2.getJointBody();
            for (Body b = world.getBodyList(); b != null; b = b.getNext()) {
                if (jointBody.contains(b)) {
                    destroyBody.add(b);
                }
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    /**
     * this will return a body list of all body
     * which collide and it is destinate to be destroyed
     *
     * @return list of body to destroy
     */
    public List<Body> getDestroyBody() {

        /**
         * suppress all register Ray fire if they have reached a certain distance
         */
        Iterator<RayFireManager.Distance> iterator = RayFireManager.getList().iterator();
        while (iterator.hasNext()) {
            RayFireManager.Distance distance = iterator.next();
            if (Math.abs(distance.getX_distance()) > distance.getLive() || Math.abs(distance.getY_distance()) > distance.getLive()) {
                destroyBody.add(distance.getBodyRayFire());
                iterator.remove();
            }
        }
        /**
         * because we want to erase the trail at the back of hero
         */
        Iterator<TrailManager.DistanceTrail> trailManagerIterator = TrailManager.getList().iterator();
        while (trailManagerIterator.hasNext()) {
            TrailManager.DistanceTrail live = trailManagerIterator.next();
            if (Math.abs(live.getX_distance()) > live.getLive() || Math.abs(live.getY_distance()) > live.getLive()) {
                destroyBody.add(live.getBodyTrail());
                trailManagerIterator.remove();
            }
        }
        List<Body> newList = new ArrayList<>(destroyBody);
        
        /**
         * retreive spaceship destroyed from manager
         */
        for (Body bd : newList) {
            SpaceShip spaceShip = this.removeToSpaceshipManager(bd);
            
            /**
             *  Do THIS in order to stop all living thread created in spaceship enemy
             * */
            if (Objects.nonNull(spaceShip)) {
                spaceShip.destroySpaceShip();
            }
        }

        /**
         * reset the destroyBodyList
         */
        destroyBody.clear();
        return newList;
    }

    /**
     * get the body of the hero register in the masterPilotWorld
     * @return the body of the hero register in the masterPilotWorld
     */
    public Body getBodyHero() {
        return hero.getBody();
    }

    /**
     * register a spaceship to the ennemyManager
     * @param spaceShip : the space ship register
     */
    public void addToSpaceshipManager( SpaceShip spaceShip) {
        Body bodySpaceship= spaceShip.getBody();
        if (!enemyManager.containsKey(bodySpaceship)) {
            this.enemyManager.put(bodySpaceship, spaceShip);
        }
    }

    /**
     * remove a spaceShip from the spaceship manager
     * @param bodySpaceship : the body of ennemy to remove
     * @return
     */
    public SpaceShip removeToSpaceshipManager(Body bodySpaceship) {
        return this.enemyManager.remove(bodySpaceship);
    }

    /**
     * retreive all existed ennemy in the world
     * @return
     */
    public List<SpaceShip> getEnemyList() {
        return new ArrayList<>(this.enemyManager.values());
    }

    /**
     * add a bomb to his manager
     * @param bomb the bomb to be had
     */
    public void addToBombManager( Bomb bomb) {
        Body body= bomb.getBody();
        if (!this.bombManager.containsKey(body)) {
            this.bombManager.put(body, bomb);
        }
    }

    /**
     * register the star to his manager in MasterPilotWorld
     * @param star : the star to be register
     */
    public void addToStarManager( Star star) {
        Body body = star.getBody();
        if (!this.starManager.containsKey(body)) {
            this.starManager.put(body, star);
        }
    }

    /**
     * use this to get the star register in the world
     * @return
     */
    public List<Star> getStarList() {
        return new ArrayList<>(this.starManager.values());
    }

    /**
     * use this to draw the framework clock in the top left side
     * @param hour
     * @param minute
     * @param second
     */
    public void drawFrameworkClock(int hour, int minute, int second) {
        this.masterPilot2D.drawFrameworkClock(hour, minute, second);
    }

    /**
     * this will draw the final status of the game
     * winner or lose
     * @param iswin
     * @param x
     * @param y
     */
    public void drawFrameworkEnd(boolean iswin, int x, int y) {
        this.masterPilot2D.drawFrameworkEnd(iswin, x, y);
    }
}
