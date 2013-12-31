package fr.umlv.masterPilot.world;

import fr.umlv.zen3.KeyboardEvent;

/**
 * This will be implemented by all classes which have to catch keyMotion and
 * notify a list of object which way for that event
 * Created by emmanuel on 07/12/13.
 */
public interface KeyMotionObservable {
    public void addObserver(KeyMotionObserver observer);

    public void notifyObserver(KeyboardEvent keyEvent);

    public void delObserver(KeyMotionObserver observer);
}
