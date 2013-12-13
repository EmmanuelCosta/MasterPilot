package fr.umlv.masterPilot.enemy;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import fr.umlv.masterPilot.world.MasterPilot;
import fr.umlv.masterPilot.Interface.Bomb;
import fr.umlv.masterPilot.Interface.SpaceShip;
import fr.umlv.masterPilot.bomb.ClassicBomb;

import java.awt.*;

public class TIE implements SpaceShip{
 
    private World world;
    private int x_axis; // point x où apparait le TIE
    private int y_axis; // point y où apparait le TIE
    private Body body;
    private final int maskBit;
    private final int category;
    private Vec2 tieSpeed = new Vec2(0, -15f); // vitesse du TIE
    private Vec2 classicBombSpeed = new Vec2(0, -3000.0f);
    
    // We need to know the ship's positions in order to move and to make shoot the tie
    private int x_shipAxis;
    private int y_shipAxis;

    public TIE(World world, int x_axis, int y_axis, int x_shipAxis, int y_shipAxis) {
        this.world = world;
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.x_shipAxis = x_shipAxis;
        this.y_shipAxis = y_shipAxis;
        
        // Interactions avec les autres body
        this.category = MasterPilot.ENEMY;
        this.maskBit = MasterPilot.PLANET | MasterPilot.HERO | MasterPilot.ENEMY;
    }
    
    public void create() {

        // create ball
        PolygonShape ps = new PolygonShape();

        Vec2[] vertices = new Vec2[6];
        vertices[0] = new Vec2(0f, 5f);
        vertices[1] = new Vec2(5f, 10f);
        vertices[2] = new Vec2(25f, 10f);
        vertices[3] = new Vec2(30f, 5f);
        vertices[4] = new Vec2(25f, 0f);
        vertices[5] = new Vec2(5f, 0f);
        ps.set(vertices, 6);
        
        // Create an JBox2D body defination for TIE.
        BodyDef bd = new BodyDef();
        bd.position.set(x_axis, y_axis);
        bd.type = BodyType.DYNAMIC;

        bd.userData=this.getClass();

        // Create a fixture for TIE
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 0.5f;
        fd.friction = 0f;
        fd.restitution = 0.5f;
        fd.filter.categoryBits = this.category;
        fd.filter.maskBits = this.maskBit;
        fd.userData = Color.BLUE;

        // body
        Body body = this.world.createBody(bd);
        body.createFixture(fd);
        this.body = body;
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
        /**
         * I try to calculate the tip coordinate
         * and create a Bomb from that point
         */
        PolygonShape sha = (PolygonShape) body.getFixtureList().getShape();

        // Reccupération de tous les sommets du TIE.
        Vec2[] vertices = sha.getVertices();
        
        // Reccupere les deux sommets dont on a besoin pour tirer depuis le centre du segment.
        Vec2 s1 = body.getWorldPoint(vertices[4]);
        Vec2 s2 = body.getWorldPoint(vertices[5]);
        ClassicBomb tieShoot = new ClassicBomb(this.world, Math.abs(s2.x - s1.x), Math.abs(s2.y - s1.y));
        tieShoot.create();
        
        // Definit le tir
        Vec2 force = body.getWorldVector(classicBombSpeed);
        Vec2 point = body.getWorldPoint(body.getWorldCenter());
        tieShoot.getBody().applyForce(force, point);
        
    }

    @Override
    public void fireBomb(Bomb bomb) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void shield() {
        // TODO Auto-generated method stub
        
    }
}
