package fr.umlv.masterPilot.ship.hero;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by emmanuel on 28/12/13.
 */
public class TrailManager {

    private static  final LinkedList<DistanceTrail> list = new LinkedList<>();
    public static int limitLive = 500;

    /**
     * add all RayFire to the Manager
     * @param initPosition
     * @param trail
     */
    public static void addTrail(Vec2 initPosition,Trail trail){
        list.add(new DistanceTrail(initPosition, trail,limitLive));
    }

    /**
     * see
     * @param initPosition
     * @param trail
     * @param live
     */
    public static void addTrail(Vec2 initPosition,Trail trail,int live){
        list.add(new DistanceTrail(initPosition, trail,live));
    }

    public static void remove(Body body) {
        Iterator<DistanceTrail> iterator = list.iterator();
        while(iterator.hasNext()){

            DistanceTrail next = iterator.next();
            if(next.getBodyBullet().equals(body)){

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

        private Vec2 initPosition;
        private Trail trail;
        private  final int live;
        private DistanceTrail(Vec2 initPosition, Trail trail,int limitLive) {
            this.initPosition = initPosition;
            this.trail = trail;
            this.live=limitLive;
        }

        public float getX_distance() {
            return initPosition.x - trail.getBody().getPosition().x;
        }
        public float getY_distance() {
            return initPosition.y - trail.getBody().getPosition().y;
        }

        public Body getBodyBullet() {
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
