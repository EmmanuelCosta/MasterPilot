package fr.umlv.masterPilot.ship.enemy;
import fr.umlv.masterPilot.ship.RayFire;
import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.ship.hero.Hero;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.awt.*;

public class Invader implements SpaceShip{
 
        private int maskBit;
        private int category;
        private  int x_axis;
        private int y_axis;
        private Hero hero;
        private final Vec2 forceLeft = new Vec2(-150f, -5f);
        private final Vec2 forceRight = new Vec2(+150f, -5f);
        private final Vec2 forceUp = new Vec2(-5f, +150f);
        private final Vec2 forceDown = new Vec2(-5f, -150f);
        private World world;
        private final Vec2 fireUp = new Vec2(0f, -10000f);
        private final Vec2 fireLeft = new Vec2(0f, 10000f);
        private final Vec2 fireRight = new Vec2(0f, 10000f);
        private Body body;
        private Vec2 shoot1;
        private Vec2 shoot2;
        private volatile boolean fire;

        public Invader(World world, int x_axis, int y_axis, Hero hero) {
            this.world = world;
            this.x_axis = x_axis;
            this.y_axis = y_axis;
            this.hero = hero;


            /**
             *  Interactions with the other bodies.
             */
            this.category = MasterPilotWorld.ENEMY;
            this.maskBit = MasterPilotWorld.SHIELD
                    | MasterPilotWorld.SHOOT
                    | MasterPilotWorld.BOMB
                    | MasterPilotWorld.MEGABOMB
                    | MasterPilotWorld.HERO
            |MasterPilotWorld.PLANET;
        }

        public void create() {

            PolygonShape ps = new PolygonShape();
            
            /**
             * Number of vertices.
             */
            Vec2[] vertices = new Vec2[5];
            vertices[0] = new Vec2(-5, -10);
            vertices[1] = new Vec2(-5, +15);
            vertices[2] = new Vec2(+0, +20);
            vertices[3] = new Vec2(+5, +15);
            vertices[4] = new Vec2(+5, -10);
              
            ps.set(vertices, 5);

            /**
             * Body definition of the Invader
             */
            BodyDef bd = new BodyDef();
            bd.position.set(x_axis, y_axis);
            bd.type = BodyType.DYNAMIC;
            bd.userData = this.getClass();
            bd.angularDamping = 2.0f;
            bd.linearDamping = 0.0999f;
            bd.angularDamping = 2.0f;
            bd.linearDamping = 0.0999f;

            /**
             * Body fixtures of the Invader
             */
            FixtureDef fd = new FixtureDef();
            fd.shape = ps;
            fd.density = 1.5f;
            fd.friction = 8f;
            fd.restitution = 0.5f;
            fd.filter.categoryBits = this.category;
            fd.filter.maskBits = this.maskBit;

            fd.userData = new EnemyBehaviour(this, Color.green);
    
    /*****************  LEFT WINGS  ***********************/
            PolygonShape leftWing = new PolygonShape();
            
            /**
             * Number of vertices.
             */
            Vec2[] LeftWingVertices  = new Vec2[5];
            vertices[0] = new Vec2(-5, +5);
            vertices[1] = new Vec2(-10, +5);
            vertices[2] = new Vec2(-15, +0);
            vertices[3] = new Vec2(-10, -5);
            vertices[4] = new Vec2(-5, -5);
           
            leftWing.set(LeftWingVertices, 5);

            /**
             * Body fixtures of the Invader
             */
            FixtureDef fdlw = new FixtureDef();
            fdlw.shape = leftWing;
            fdlw.density = 1.5f;
            fdlw.friction = 8f;
            fdlw.restitution = 0.5f;
            fdlw.filter.categoryBits = this.category;
            fdlw.filter.maskBits = this.maskBit;

            fdlw.userData = new EnemyBehaviour(this, Color.green);
            
    /*****************  RIGHT WINGS  ***********************/
          
            PolygonShape rightWing = new PolygonShape();
            
            /**
             * Number of vertices.
             */
            Vec2[] rightWingVertices = new Vec2[5];
            vertices[0] = new Vec2(+5, +5);
            vertices[1] = new Vec2(+10, +5);
            vertices[2] = new Vec2(+0, +15);
            vertices[3] = new Vec2(+10, -5);
            vertices[4] = new Vec2(+5, -5);
        
            rightWing.set(rightWingVertices, 5);

            /**
             * Body fixtures of the Invader
             */
            FixtureDef fdrw = new FixtureDef();
            fdrw.shape = ps;
            fdrw.density = 1.5f;
            fdrw.friction = 8f;
            fdrw.restitution = 0.5f;
            fdrw.filter.categoryBits = this.category;
            fdrw.filter.maskBits = this.maskBit;

            fdrw.userData = new EnemyBehaviour(this, Color.green);
            
    /**************  PRBOTTOM CIRCLE  *********************/
            CircleShape cs = new CircleShape();
            cs.setRadius(5);

            FixtureDef fdc = new FixtureDef();
            fdc.shape = cs;
            fdc.isSensor = false;
            fdc.density = 1.5f;
            fdc.friction = 8f;
            fdc.restitution = 0.5f;
            fdc.filter.categoryBits = MasterPilotWorld.ENEMY;
            fdc.filter.maskBits = MasterPilotWorld.HERO | MasterPilotWorld.SHIELD;
            fdc.userData = new EnemyBehaviour(this, Color.green);
            
            Body body = this.world.createBody(bd);
            body.createFixture(fd);
            body.createFixture(fdlw);
            body.createFixture(fdrw);
            body.createFixture(fdc);

            this.body = body;
        }

        @Override
        public void right() {
         
        }

        @Override
        public void left() {

        }

        @Override
        public void up() {
    
        }

        @Override
        public void down() {
          
        }

        @Override
        public void doMove() {
        }

        @Override
        public void fire() {
        }

        @Override
        public void fireBomb() {
            throw new NotImplementedException();
        }

        @Override
        public void shield() {
            throw new NotImplementedException();
        }

        public Body getBody() {
            return body;
        }

}
