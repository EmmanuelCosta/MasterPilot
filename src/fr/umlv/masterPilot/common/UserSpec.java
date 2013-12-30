package fr.umlv.masterPilot.common;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import java.awt.*;
import java.util.*;

/**
 * This class have to be given in fixture.userData object of jbox2D
 * it will contains the basic behaviour of body when collision is detected
 *
 * Created by emmanuel on 19/12/13.
 */
public interface UserSpec {

    /**
     * add the basic behaviuor  of your
     * object while collision is detect
     *
     * true flag  tag the begining of contact
     * false the end
     * @param fix2
     * @param flag
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
     * other wise it can explode
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

    /**
     * tell if body has other joint body
     * important for destroying every bodies joint to the main body
     * @return
     */
    public   default boolean hasJointBody(){
        return false;
    }
}
