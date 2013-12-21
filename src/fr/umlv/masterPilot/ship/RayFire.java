package fr.umlv.masterPilot.ship;

import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.awt.*;

/**
 * Ray bomb are normal bomb
 * by default is hero friendly but
 * enemy ans star hostile
 * <p>
 * created By Babala Costa Emmanuel
 */
public class RayFire {

    private final int maskBit;
    private final int category;
    private final Color color;
    private final World world;
    private final float x_axis;
    private final float y_axis;
    private Body body;

    /**
     * create a ray Bomb at the given position
     * @param world
     * @param x_axis : x coordinate  of the bomb
     * @param y_axis : y coordinate of the bomb
     *
     *               by default is category is SHOOT
     *               and his mask is PLANET and ENNEMY so it means
     *               it can collide with them
     */
    public RayFire(World world, float x_axis, float y_axis) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;

        this.category = MasterPilotWorld.SHOOT;
        this.maskBit = MasterPilotWorld.PLANET | MasterPilotWorld.ENEMY;

        this.color = Color.WHITE;

    }

    /***
     *
     * @param world
     * @param x_axis
     * @param y_axis
     * @param category the category of the rayBomb
     * @param maskBit  help to set with which category he can collide
     * @param color
     */
    public RayFire(World world, float x_axis, float y_axis, int category, int maskBit, Color color) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.category = category;
        this.maskBit = maskBit;
        this.color = color;
    }

    /**
     * create the rayBomb in the world
     */
    public void create() {
        /**
         * create body shape and specification
         */
        Vec2 vertices[] = new Vec2[2];
        vertices[0] = new Vec2(0, 0);
        vertices[1] = new Vec2(0, 5f);
        EdgeShape ps = new EdgeShape();
        ps.set(vertices[0], vertices[1]);

        /**
         * create body
         */
        BodyDef bd = new BodyDef();
        bd.position.set(x_axis, y_axis);
        bd.type = BodyType.DYNAMIC;
        bd.userData = this.getClass();

        /**
         *  Create a fixture
         */
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 0.0f;
        fd.friction = 0.1f;
        fd.restitution = 0.5f;
        fd.filter.categoryBits = this.category;
        fd.filter.maskBits = this.maskBit;
        fd.userData = new UserSpec() {
            private boolean destroy=false;
            @Override
            public void onCollide(Fixture fix2, boolean flag) {
                if(flag == false){
                    this.destroy=true;
                }
            }

            @Override
            public boolean isDestroyable() {
                return destroy;
            }

            @Override
            public Color getColor() {
                return color;
            }
        };

        /**
         * join the fixture to the body
         */
        Body body = this.world.createBody(bd);
        body.createFixture(fd);
        bd.allowSleep = true;
        this.body = body;

    }


    public Body getBody() {
        return this.body;
    }

}
