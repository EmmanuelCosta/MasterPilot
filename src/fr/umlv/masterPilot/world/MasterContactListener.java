package fr.umlv.masterPilot.world;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by emmanuel on 11/12/13.
 */
public class MasterContactListener implements ContactListener {


    private  final World world;
    private final List<Body> list;

    public MasterContactListener(World world) {
        Objects.requireNonNull(world);
        this.world=world;
        list = new ArrayList<>();
    }

    @Override
    public void beginContact(Contact contact) {

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if(fixtureA.getFilterData().categoryBits == MasterPilot.ENEMY){
            System.out.println("destroy done");
           // this.world.destroyBody(fixtureA.getBody());
            list.add(fixtureA.getBody());
        }else if(fixtureB.getFilterData().categoryBits == MasterPilot.ENEMY){
            System.out.println("destroy done");
            this.world.destroyBody(fixtureB.getBody());
            list.add(fixtureB.getBody());
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public List<Body> getList() {
        return list;
    }


}
