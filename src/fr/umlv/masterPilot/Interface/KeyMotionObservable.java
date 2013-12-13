package fr.umlv.masterPilot.Interface;

import fr.umlv.zen3.KeyboardEvent;

/**
 * Created by emmanuel on 07/12/13.
 */
public interface KeyMotionObservable {
    public void addObserver(KeyMotionObserver observer);

    public void notifyObserver(KeyboardEvent keyEvent);

    public void delObserver(KeyMotionObserver observer);
}
