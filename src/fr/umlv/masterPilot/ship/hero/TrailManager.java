package fr.umlv.masterPilot.ship.hero;

import fr.umlv.masterPilot.ship.RayFire;
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
     * @param bullet
     */
    public static void addTrail(Vec2 initPosition,Bullet bullet){
        list.add(new DistanceTrail(initPosition,bullet,limitLive));
    }

    /**
     * see
     * @param initPosition
     * @param bullet
     * @param live
     */
    public static void addTrail(Vec2 initPosition,Bullet bullet,int live){
        list.add(new DistanceTrail(initPosition,bullet,live));
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
        private  Bullet bullet;
        private  final int live;
        private DistanceTrail(Vec2 initPosition, Bullet bullet,int limitLive) {
            this.initPosition = initPosition;
            this.bullet = bullet;
            this.live=limitLive;
        }

        public float getX_distance() {
            return initPosition.x - bullet.getBody().getPosition().x;
        }
        public float getY_distance() {
            return initPosition.y - bullet.getBody().getPosition().y;
        }

        public Body getBodyBullet() {
            return bullet.getBody();
        }

        public int getLive() {
            return live;
        }
    }

    public static LinkedList<DistanceTrail> getList() {
        return list;
    }
}
