package fr.umlv.masterPilot.bomb;

import fr.umlv.masterPilot.Interface.Bomb;
import fr.umlv.masterPilot.common.CustomQueryCalledBack;
import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.hero.Hero;
import fr.umlv.masterPilot.world.MasterPilot;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.awt.*;
import java.util.Iterator;

/**
 * Created by emmanuel on 19/12/13.
 */
public class GenericBomb implements Bomb {
    private final int maskBit;
    private final int category;
    private final Bomb.BombType bombType;
    private BombState bombstate =BombState.NOT_ARMED;
    /**
     * true : bomb armed explode on collision
     * false: not armed
     */
    private final boolean flag;
    private World world;
    private float x_axis;
    private float y_axis;
    private Body body;



    public GenericBomb(World world, float x_axis, float y_axis, BombType bombType) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;

        switch (bombType) {


            case BOMB:
                this.category = MasterPilot.BOMB;
                break;
            case MEGABOMB:
                this.category = MasterPilot.MEGABOMB;
                break;
            default:

                throw new UnsupportedOperationException();
        }

        this.maskBit = MasterPilot.PLANET | MasterPilot.ENEMY;
        this.bombType = bombType;
        this.flag = false;
    }

    public GenericBomb(World world, float x_axis, float y_axis, BombType bombType, boolean flag) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;

        switch (bombType) {


            case BOMB:
                this.category = MasterPilot.BOMB;
                break;
            case MEGABOMB:
                this.category = MasterPilot.MEGABOMB;
                break;
            default:
                throw new UnsupportedOperationException();
        }

        this.maskBit = MasterPilot.PLANET | MasterPilot.ENEMY;
        this.bombType = bombType;
        this.flag = flag;
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
       fd.userData = new UserSpec() {
           private boolean hide = false;
           private boolean addable = false;
           private boolean destroyed=false;
           private int state =0;
           @Override
           public void onCollide(Fixture fix2, boolean flag) {
               /**
                * hero take bomb
                */
                if((fix2.getFilterData().categoryBits == MasterPilot.HERO
                        || fix2.getFilterData().categoryBits == MasterPilot.SHIELD) && state == 0){
                    addable =true;
                    hide =true;
                    state ++;


                }else if(bombstate != BombState.NOT_ARMED){
                    hide=false;
                }
                 if(flag == true){
                    if(bombstate == BombState.ARMED  ){
                        /**
                         * can collide if is not hero or shield
                         */

                        if(fix2.getFilterData().categoryBits != MasterPilot.HERO
                            && fix2.getFilterData().categoryBits != MasterPilot.SHIELD){
                            boum();
                            destroyed=true;

                        }
                        /**
                         * collide with hero or shield
                         * only after bomb has been lauch
                         * not inside the launcher
                         */
                       else if ((fix2.getFilterData().categoryBits == MasterPilot.HERO
                                  || fix2.getFilterData().categoryBits == MasterPilot.SHIELD) && state >= 2){
                            boum();
                            destroyed=true;

                        }
                        /**
                         * if is hero or shield do not explode
                         * when launching
                         */
                        else if((fix2.getFilterData().categoryBits == MasterPilot.HERO
                                || fix2.getFilterData().categoryBits == MasterPilot.SHIELD) ){
                            state ++;
                            hide = false;

                        }

                    }

                }
           }

           @Override
           public boolean isDestroyedSet() {
               //hide =true;
               return destroyed;
           }

           @Override
           public boolean isAddableBomb() {

               return addable;
           }

           @Override
           public Color getColor() {
               return Color.LIGHT_GRAY;
           }

           @Override
           public boolean getSensor() {
               return hide;
           }
       };


        // body
        Body body = this.world.createBody(bd);
        body.createFixture(fd);
        this.body = body;
    }

    /**
     * this will create an expulsion effect
     */
    @Override
    public void boum() {
        int numRays = 720;
        Vec2 center = this.body.getPosition();
        float blastRadius = 100f;
        float powerBlast = 0;
        if (this.bombType == BombType.MEGABOMB) {
            powerBlast = -5000000f;
        } else {
            powerBlast = 5000000f;
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
        return null;
    }

    @Override
    public void setBombState(BombState bombState) {
        this.bombstate=bombState;
    }

}