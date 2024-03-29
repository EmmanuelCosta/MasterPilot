package fr.umlv.masterPilot.bomb;

import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import java.util.Iterator;

/**
 *
 * this bomb will can create a bomb of two type
 * explode and implode
 * this behaviour depend on the type given in constructor
 *  @author Babala Costa Emmanuel
 */
public class GenericBomb implements Bomb {
    private final int maskBit;
    private final int category;
    private final Bomb.BombType bombType;


    private BombState bombstate = BombState.NOT_ARMED;
    private final World world;
    private final float x_axis;
    private final float y_axis;
    private  Body body;

    /**
     * create a bomb in the position given with the specify type
     * @param world : The jbox world @see World
     * @param x_axis x_coordinate
     * @param y_axis y_coordinate
     * @param bombType type of the bomb @see BombType
     */
    public GenericBomb(World world, float x_axis, float y_axis, BombType bombType) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;

        switch (bombType) {
            case BOMB:
                this.category = MasterPilotWorld.BOMB;
                break;
            case MEGABOMB:
                this.category = MasterPilotWorld.MEGABOMB;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        this.maskBit = MasterPilotWorld.PLANET | MasterPilotWorld.ENEMY| MasterPilotWorld.HERO;
        this.bombType = bombType;
    }

    public void create() {
        PolygonShape ps = new PolygonShape();

        Vec2[] vertices = new Vec2[6];
        vertices[0] = new Vec2(0f, 5f);
        vertices[1] = new Vec2(5f, 10f);
        vertices[2] = new Vec2(10f, 10f);
        vertices[3] = new Vec2(15f, 5f);
        vertices[4] = new Vec2(10f, 0f);
        vertices[5] = new Vec2(5f, 0f);
        ps.set(vertices, 6);

        BodyDef bd = new BodyDef();
        bd.position.set(x_axis, y_axis);
        bd.type = BodyType.DYNAMIC;
        bd.userData = this.getClass();

        // Create a fixture for TIE
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 0.5f;
        fd.friction = 0.09f;
        fd.restitution = 0.5f;
        fd.filter.categoryBits = this.category;
        fd.filter.maskBits = this.maskBit;
        fd.userData = new BombBehaviour(this);

        Body body = this.world.createBody(bd);
        body.createFixture(fd);
        this.body = body;
    }


    @Override
    public void boum() {
        Vec2 center = this.body.getPosition();
        float blastRadius = 100f;
        float powerBlast = 0;
        if (this.bombType == BombType.MEGABOMB) {
            powerBlast = -10000000f;
        } else {
            powerBlast = 10000000f;
        }

        CustomQueryCalledBack queryCallback = new CustomQueryCalledBack();

        //create an aabb vector represent a surface
        Vec2 sub = center.sub(new Vec2(blastRadius, blastRadius));
        Vec2 add = center.add(new Vec2(blastRadius, blastRadius));
        AABB aabb = new AABB(sub, add);
        this.world.queryAABB(queryCallback, aabb);

        //check which of these bodies have their center of mass within the blast radius
        Iterator<Body> bodyTouched = queryCallback.getBodyTouch().iterator();

        while (bodyTouched.hasNext()) {
            Body body = bodyTouched.next();
            Vec2 bodyCom = body.getWorldCenter();

            //ignore bodies outside the blast range
            if ((bodyCom.sub(center)).length() >= blastRadius)
                continue;

            applyBlastImpulse(body, center, bodyCom, powerBlast);
        }
    }

    @Override
    public BombType getBombType() {
        return this.bombType;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public BombState getBombeState() {
        return this.bombstate;
    }

    @Override
    public void setBombState(BombState bombState) {
        this.bombstate = bombState;
    }

}
