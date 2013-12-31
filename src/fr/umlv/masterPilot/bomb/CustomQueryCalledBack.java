package fr.umlv.masterPilot.bomb;

/**
 * Created by emmanuel on 17/12/13.
 */

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import java.util.ArrayList;
import java.util.List;

/**
 * our custom query called back
 * store in a list all the body touched by the given AABB param in the world
 * while using this method world.queryAABB(this, AABB)
 *
 * <p>
 * so that query stored all that body in a list
 * <p>
 * so you can retreive it and perform some action on that
 */
public class CustomQueryCalledBack implements QueryCallback {
    private final ArrayList<Body> bodyTouch = new ArrayList<>();

    public List<Body> getBodyTouch() {
        return bodyTouch;
    }

    @Override
    public boolean reportFixture(Fixture fixture) {
        bodyTouch.add(fixture.getBody());
        return true;
    }
}