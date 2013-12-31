package fr.umlv.masterPilot.world;

import fr.umlv.zen3.KeyboardEvent;

/**
 * this have to be implements by object that wait a keyMotion event
 * it implements method that can be call when taht event has been notify
 * Created by emmanuel on 07/12/13.
 */
public interface KeyMotionObserver {
    public void onKeyPressed(KeyboardEvent keyEvent);
}
