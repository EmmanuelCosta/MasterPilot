package fr.umlv.masterPilot.world;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import java.util.Objects;

/**
 * Created by emmanuel on 11/12/13.
 */
public class MasterContactListener implements ContactListener {

    private Body hero;
    private  World world;

    public MasterContactListener(World world) {
        Objects.requireNonNull(world);
        this.world=world;
    }

    @Override
    public void beginContact(Contact contact) {
       if(hero != null && world!=null){
//          contact.getFixtureA().

       }

    }

    @Override
    public void endContact(Contact contact) {
        System.out.println("end to touch");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public void setHero(Body hero) {
        this.hero = hero;
    }
}
