package fr.umlv.masterPilot.common;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import java.awt.*;
import java.util.*;

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
     * true flag  tag the begining of contact
     * false the end
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
    public default boolean isDestroyable() {
        return false;
    }

    /**
     * return the color to give to the object
     * @return
     */
    public default Color getColor() {
        return Color.LIGHT_GRAY;
    }

    /**
     * If the hero get a bomb, verify if the specific bomb can be added as an item.
     * if the specific bomb is an item, the hero can use it.
     * @return
     */
    public default boolean isItem() {
        return false;
    }

    /**
     * retreive all the body joint to the main body
     * @return
     */
    public default java.util.List<Body> getJointBody(){
        return null;
    }

    public   default boolean hasJointBody(){
        return false;
    }
}
