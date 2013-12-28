package fr.umlv.masterPilot.ship.enemy;

import java.awt.Color;
import java.util.Timer;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.ship.hero.Hero;

public class SpaceBall implements SpaceShip{

    private int maskBit;
    private int category;
    private float x_axis;
    private float y_axis;
    private Hero hero;
    private World world;
    private Body body;
    
    public SpaceBall(World world, float x, float y, Hero hero) {
        this.world = world;
        this.x_axis = x;
        this.y_axis = y;
        this.hero = hero;       
    } 
    

    @Override
    public void create() {
        /**
         * Primitive form
         */

        CircleShape cs = new CircleShape();
        cs.setRadius(10);


        /**
         * Body definition of the SpaceBall
         */
        BodyDef bd = new BodyDef();
        bd.position.set(x_axis, y_axis);
        bd.type = BodyType.DYNAMIC;
        bd.userData = this.getClass();
        bd.angularDamping = 2.0f;
        bd.linearDamping = 0.0999f;

        /**
         * Body fixtures of the TIE
         */
        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 1.5f;
        fd.friction = 8f;
        fd.restitution = 0.5f;
        fd.filter.categoryBits = this.category;
        fd.filter.maskBits = this.maskBit;
        fd.userData = new EnemyBehaviour(this, Color.BLUE);
        
        /************* LOSANGE UP *********************/
        
        PolygonShape up = new PolygonShape();
        Vec2 upVertices[] = new Vec2[4];
        upVertices[0] = new Vec2(0, +10);
        upVertices[1] = new Vec2(+5, +15);
        upVertices[2] = new Vec2(0, +20);
        upVertices[3] = new Vec2(-5, +15);
        up.set(upVertices, 4);
        
        FixtureDef fdup = new FixtureDef();
        fdup.shape = up;
        fdup.density = 1.5f;
        fdup.friction = 8f;
        fdup.restitution = 0.5f;
        fdup.filter.categoryBits = this.category;
        fdup.filter.maskBits = this.maskBit;
        fdup.userData = new EnemyBehaviour(this, Color.BLUE);
        body.createFixture(fdup);
        
        /************* LOSANGE DOWN *******************/
        
        PolygonShape down = new PolygonShape();
        Vec2 downVertices[] = new Vec2[4];
        downVertices[0] = new Vec2(0, -10);
        downVertices[1] = new Vec2(+5, -15);
        downVertices[2] = new Vec2(0, -20);
        downVertices[3] = new Vec2(-5, -15);
        down.set(downVertices, 4);
        
        FixtureDef fddown = new FixtureDef();
        fddown.shape = down;
        fddown.density = 1.5f;
        fddown.friction = 8f;
        fddown.restitution = 0.5f;
        fddown.filter.categoryBits = this.category;
        fddown.filter.maskBits = this.maskBit;
        fddown.userData = new EnemyBehaviour(this, Color.BLUE);
        body.createFixture(fddown);
        
        /************* LOSANGE LEFT *******************/
        
        PolygonShape left = new PolygonShape();
        Vec2 leftVertices[] = new Vec2[4];
        leftVertices[0] = new Vec2(0, -10);
        leftVertices[1] = new Vec2(+5, -15);
        leftVertices[2] = new Vec2(0, -20);
        leftVertices[3] = new Vec2(-5, -15);
        left.set(leftVertices, 4);
        
        FixtureDef fdleft = new FixtureDef();
        fdleft.shape = down;
        fdleft.density = 1.5f;
        fdleft.friction = 8f;
        fdleft.restitution = 0.5f;
        fdleft.filter.categoryBits = this.category;
        fdleft.filter.maskBits = this.maskBit;
        fdleft.userData = new EnemyBehaviour(this, Color.BLUE);
        body.createFixture(fdleft);
        
        /************* LOSANGE RIGHT ******************/
        
        PolygonShape right = new PolygonShape();
        Vec2 rightVertices[] = new Vec2[4];
        rightVertices[0] = new Vec2(+10, 0);
        rightVertices[1] = new Vec2(+15, +5);
        rightVertices[2] = new Vec2(+20, 0);
        rightVertices[3] = new Vec2(+15, -5);
        right.set(leftVertices, 4);
        
        FixtureDef fdright = new FixtureDef();
        fdright.shape = down;
        fdright.density = 1.5f;
        fdright.friction = 8f;
        fdright.restitution = 0.5f;
        fdright.filter.categoryBits = this.category;
        fdright.filter.maskBits = this.maskBit;
        fdright.userData = new EnemyBehaviour(this, Color.BLUE);
        body.createFixture(fdright);
        
        this.body = this.world.createBody(bd);
    }

      
    
    @Override
    public void right() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void left() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void up() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void down() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void fire() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void fireBomb() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void shield() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Body getBody() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void doMove() {
        // TODO Auto-generated method stub
        
    }

}
