package fr.umlv.masterPilote.world;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * Created by emmanuel on 11/12/13.
 */
public class MasterContactListener implements ContactListener {


    @Override
    public void beginContact(Contact contact) {
        System.out.println("begin to touch");

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
}
