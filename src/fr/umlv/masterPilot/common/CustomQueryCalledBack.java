package fr.umlv.masterPilot.common;

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
 * world.queryAABB(this, AABB)
 * this AABB param convert a radius on which all body can be touch
 * <p>
 * so that query stored all that body in a list
 * <p>
 * so you can retreive it and perform some action on that
 */
public class CustomQueryCalledBack implements QueryCallback {
    public List<Body> bodyTouch = new ArrayList<>();

    public List<Body> getBodyTouch() {
        return bodyTouch;
    }

    @Override
    public boolean reportFixture(Fixture fixture) {

        bodyTouch.add(fixture.getBody());
        return true;
    }
}