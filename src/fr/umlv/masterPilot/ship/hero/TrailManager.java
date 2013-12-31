package fr.umlv.masterPilot.ship.hero;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This will manage will all trail in the game
 * Trail is design to be the dragged of a spaceship
 * Created by emmanuel on 28/12/13.
 */
public class TrailManager {

    private static  final LinkedList<DistanceTrail> list = new LinkedList<>();
    public static int limitLive = 5;

    /**
     * add all RayFire to the Manager
     * @param initPosition : the initial position of the body which create the trail
     * @param trail
     */
    public static void addTrail(Vec2 initPosition,Trail trail){
        list.add(new DistanceTrail(initPosition, trail,limitLive));
    }

    /**
     * add trail to the manager
     * @param initPosition : the initial position of the body which create the trail
     * @param trail
     * @param live : the live of the trail
     *             in fact it represent distance after what the trail must be supress
     *             the bigger he is, the long trail will be
     */
    public static void addTrail(Vec2 initPosition,Trail trail,int live){
        list.add(new DistanceTrail(initPosition, trail,live));
    }

    public static void remove(Body body) {
        Iterator<DistanceTrail> iterator = list.iterator();
        
        while(iterator.hasNext()){
            DistanceTrail next = iterator.next();
            if(next.getBodyTrail().equals(body)){
                iterator.remove();
                return;
            }
        }
    }

    /**
     * This class contains information about the
     *distance traveled by a trail
     */
    public  static class DistanceTrail{
        private Vec2 refBody;
        private Trail trail;
        private  final int live;
        private DistanceTrail(Vec2 initPosition, Trail trail,int limitLive) {
            this.refBody = initPosition;
            this.trail = trail;
            this.live=limitLive;
        }

        /**
         * calculate the x_axis distance between the trail and the initialPosition x_axis of the body
         * object which create him
         * @return the distance travel in x_axis coordinate
         */
        public float getX_distance() {
            return refBody.x - trail.getBody().getPosition().x;
        }
        /**
         * calculate the y_axis distance between the trail
         * and the initialPosition y_axis of the body
         * object which create him
         * @return the distance travel in y_axis coordinate
         */
        public float getY_distance() {
            return refBody.y - trail.getBody().getPosition().y;
        }

        public Body getBodyTrail() {
            return trail.getBody();
        }

        /**
         * the duration live of the trail
         * @return
         */
        public int getLive() {
            return live;
        }
    }

    /**
     *it will return a list of DistanceTrail object which contain
     * specific information about the distance between the trail and the spaceship which create it
     * @return List of DistanceTrail @see DistanceTrail
     */
    public static LinkedList<DistanceTrail> getList() {
        return list;
    }
}
