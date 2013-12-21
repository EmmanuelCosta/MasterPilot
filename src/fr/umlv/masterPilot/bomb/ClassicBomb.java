package fr.umlv.masterPilot.bomb;

import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.world.MasterPilot;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.*;

import java.awt.*;

/**
 * classic bomb are normal bomb
 * by default is hero friendly but
 * enemy hostile
 * <p>
 * created By Babala Costa Emmanuel
 */
public class ClassicBomb {

    private final int maskBit;
    private final int category;
    private final Color color;
    private final World world;
    private final float x_axis;
    private final float y_axis;
    private int radius = 2;
    private Body body;

    public ClassicBomb(World world, float x_axis, float y_axis) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;


        this.category = MasterPilot.ENEMY;
        this.maskBit = MasterPilot.PLANET ;

        this.color = Color.WHITE;


    }


    public ClassicBomb(World world, float x_axis, float y_axis, int category, int maskBit, Color color, int radius) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;

        this.category = category;
        this.maskBit = maskBit;

        this.color = color;

        this.radius = radius;
    }

    public void create() {


        CircleShape cs = new CircleShape();

        cs.m_radius = radius;

        BodyDef bd = new BodyDef();

        bd.position.set(x_axis, y_axis);
        bd.type = BodyType.DYNAMIC;

        bd.allowSleep = true;
        bd.userData = this.getClass();


        FixtureDef fd = new FixtureDef();
        // applique toi a cs
        fd.shape = cs;
        fd.density = 0.0f;
        fd.friction = 0.0f;
        fd.restitution = 0.0f;

        fd.filter.categoryBits = this.category;

        fd.filter.maskBits = this.maskBit;

        fd.isSensor =true;
        fd.userData = new UserSpec() {


            @Override
            public void onCollide(Fixture fix2, boolean flag) {


            }

            @Override
            public Color getColor() {
                return color;
            }

            @Override
            public boolean isDestroyable() {
                return true;
            }

            @Override
            public boolean getSensor() {

                return false;
            }
        };

        // body
        Body body = this.world.createBody(bd);
        body.createFixture(fd);
        this.body = body;
    }

    public Body getBody() {
        return this.body;
    }


}
