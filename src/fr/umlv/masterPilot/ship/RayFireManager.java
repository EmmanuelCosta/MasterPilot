package fr.umlv.masterPilot.ship;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * This will Manage the RayFire which is fire of @SpaceShip
 * it gives information of distance they have done
 * and the attempt duration live of fire
 * we will remove the fire of the world if they have done some distance
 * <p>
 * @author Babala Costa Emmanuel
 * </p>
 */
public class  RayFireManager {
    private static  final LinkedList<Distance> list = new LinkedList<>();
    public static int limitLive = 500;

    /**
     * add all RayFire to the Manager with default live of 500
     * @param initPosition
     * @param rayFire
     */
    public static void addRayFire(Vec2 initPosition,RayFire rayFire){
        list.add(new Distance(initPosition,rayFire,limitLive));
    }

    /**
     * add all RayFire to the Manager
     * @param initPosition
     * @param rayFire
     * @param live the duration live of a fire
     */
    public static void addRayFire(Vec2 initPosition,RayFire rayFire,int live){
        list.add(new Distance(initPosition,rayFire,live));
    }

    public static void remove(Body body) {
        Iterator<Distance> iterator = list.iterator();
        
        while(iterator.hasNext()){
            Distance next = iterator.next();
            if(next.getBodyRayFire().equals(body)){
                iterator.remove();
                return;
            }
        }
    }

    /**
     * This class contains information about the
     *
     */
    public  static class Distance{
         private Vec2 initPosition;
         private  RayFire rayFire;
         private  final int live;

        /**
         *
         * @param initPosition initial positiuon in the world
         * @param rayFire  the fire
         * @param limitLive his attempting live duration
         */
         private Distance(Vec2 initPosition, RayFire rayFire,int limitLive) {
             this.initPosition = initPosition;
             this.rayFire = rayFire;
             this.live=limitLive;
         }

         public float getX_distance() {
             return initPosition.x - rayFire.getBody().getPosition().x;
         }
         
         public float getY_distance() {
             return initPosition.y - rayFire.getBody().getPosition().y;
         }

         public Body getBodyRayFire() {
             return rayFire.getBody();
         }

         public int getLive() {
             return live;
         }
     }

    /**
     * retreive a list of all rayfire with his distance
     * @return
     */
    public static LinkedList<Distance> getList() {
        return list;
    }
}
