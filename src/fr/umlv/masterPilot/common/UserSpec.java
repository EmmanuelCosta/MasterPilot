package fr.umlv.masterPilot.common;

import org.jbox2d.dynamics.Fixture;

import java.awt.*;

/**
 * This class contains the caracteristic
 * <p>
 * <p>
 * <p>
 * Created by emmanuel on 19/12/13.
 */
public interface UserSpec {
    /**
     * add the basic behaviuor  of your
     * object while collision is detect
     *
     * @param
     */
    public void onCollide(Fixture fix2, boolean flag);

    /**
     * get the actual sensor of object
     * so if is set to false you can collide
     * otherwise you can t
     *
     * @return
     */
    public default boolean getSensor() {
        return false;
    }

    /**
     * when true  the object must  be destroyed
     *
     * @return
     */
    public default boolean isDestroyedSet() {
        return false;
    }

    /**
     * return the color to give to the object
     * @return
     */
    public default Color getColor() {
        return Color.LIGHT_GRAY;
    }

    public default boolean isAddableBomb() {
        return false;
    }
}
