package fr.umlv.masterPilot.ship.hero;

import org.jbox2d.dynamics.Body;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by emmanuel on 28/12/13.
 */
public class TrailManager {

    private static  final LinkedList<DistanceTrail> list = new LinkedList<>();
    public static int limitLive = 5;

    /**
     * add all RayFire to the Manager
     * @param body
     * @param trail
     */
    public static void addTrail(Body body,Trail trail){
        list.add(new DistanceTrail(body, trail,limitLive));
    }

    /**
     * see
     * @param body
     * @param trail
     * @param live
     */
    public static void addTrail(Body body,Trail trail,int live){
        list.add(new DistanceTrail(body, trail,live));
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
     *
     */
    public  static class DistanceTrail{

        private Body refBody;
        private Trail trail;
        private  final int live;
        private DistanceTrail(Body refBody, Trail trail,int limitLive) {
            this.refBody = refBody;
            this.trail = trail;
            this.live=limitLive;
        }

        public float getX_distance() {
            return refBody.getPosition().x - trail.getBody().getPosition().x;
        }
        public float getY_distance() {
            return refBody.getPosition().y - trail.getBody().getPosition().y;
        }

        public Body getBodyTrail() {
            return trail.getBody();
        }

        public int getLive() {
            return live;
        }
    }

    public static LinkedList<DistanceTrail> getList() {
        return list;
    }
}
